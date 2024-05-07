package ru.gozerov.tfs_spring.data.repositories

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.gozerov.tfs_spring.data.cache.dao.StreamDao
import ru.gozerov.tfs_spring.data.cache.dao.TopicDao
import ru.gozerov.tfs_spring.data.cache.entities.toStream
import ru.gozerov.tfs_spring.data.cache.entities.toStreamEntity
import ru.gozerov.tfs_spring.data.cache.entities.toTopic
import ru.gozerov.tfs_spring.data.cache.entities.toTopicEntity
import ru.gozerov.tfs_spring.data.remote.api.ZulipApi
import ru.gozerov.tfs_spring.data.remote.api.ZulipLongPollingApi
import ru.gozerov.tfs_spring.data.remote.api.models.FullStream
import ru.gozerov.tfs_spring.data.remote.api.models.Message
import ru.gozerov.tfs_spring.data.remote.api.models.RegisterEventQueueResponse
import ru.gozerov.tfs_spring.data.remote.api.models.User
import ru.gozerov.tfs_spring.data.remote.api.models.ZulipEvent
import ru.gozerov.tfs_spring.domain.repositories.ZulipRepository
import javax.inject.Inject

class ZulipRepositoryImpl @Inject constructor(
    private val zulipApi: ZulipApi,
    private val zulipLongPollingApi: ZulipLongPollingApi,
    private val streamDao: StreamDao,
    private val topicDao: TopicDao
) : ZulipRepository {

    private val categories = listOf("Subscribed", "All streams")

    override suspend fun getUsers(): List<User> {
        return zulipApi.getUsers().members
    }

    override suspend fun getOwnUser(): User {
        return zulipApi.getOwnUser()
    }

    override suspend fun getUserById(userId: Int): User {
        return zulipApi.getUserById(userId).user
    }

    override suspend fun getStreams(): Flow<Map<String, List<FullStream>>> = flow {
        val cacheItems = mutableMapOf<String, List<FullStream>>()

        val cacheSubscribedStreams = streamDao.getSubscribedStreams().map { stream ->
            FullStream(
                stream.toStream(),
                topicDao.getTopics(stream.id).map { entity -> entity.toTopic() }
            )
        }
        cacheItems[categories[0]] = cacheSubscribedStreams

        val cacheStreams = streamDao.getStreams().map { stream ->
            FullStream(
                stream.toStream(),
                topicDao.getTopics(stream.id).map { entity -> entity.toTopic() }
            )
        }
        cacheItems[categories[1]] = cacheStreams

        emit(cacheItems)

        val remoteStreams = zulipApi.getAllStreams().streams
        streamDao.saveStreams(remoteStreams.map { it.toStreamEntity() })
        val remoteMappedStreams = remoteStreams.map { stream ->
            val topics = zulipApi.getStreamTopics(stream.stream_id).topics
            topicDao.clear(stream.stream_id)
            topicDao.saveTopics(topics.map { it.toTopicEntity(stream.stream_id) })
            FullStream(stream, topics)
        }
        val remoteSubscribedStreams = zulipApi.getSubscribedStreams().subscriptions
        streamDao.saveStreams(remoteSubscribedStreams.map { it.toStreamEntity(isFavorite = true) })
        val remoteMappedSubscribedStreams = remoteSubscribedStreams.map { stream ->
            val topics = zulipApi.getStreamTopics(stream.stream_id).topics
            topicDao.clear(stream.stream_id)
            topicDao.saveTopics(topics.map { it.toTopicEntity(stream.stream_id) })
            FullStream(stream, topics)
        }
        emit(
            mapOf(
                categories[0] to remoteMappedSubscribedStreams,
                categories[1] to remoteMappedStreams
            )
        )

    }

    override suspend fun getMessages(
        numBefore: Int,
        numAfter: Int,
        anchor: String,
        narrow: String
    ): List<Message> {
        return zulipApi.getMessages(numBefore, numAfter, anchor, narrow).messages
    }

    override suspend fun addReaction(messageId: Int, emojiName: String) {
        Log.e("AAAA", "adding")
        zulipApi.addReaction(messageId, emojiName)
    }

    override suspend fun removeReaction(messageId: Int, emojiName: String) {
        zulipApi.removeReaction(messageId, emojiName)
    }

    override suspend fun sendMessage(
        type: String,
        to: String,
        topic: String,
        content: String
    ): Int {
        return zulipApi.sendMessage(type, to, topic, content).id
    }

    override suspend fun registerEventQueue(narrow: String): RegisterEventQueueResponse {
        return zulipLongPollingApi.registerEventQueue(narrow = narrow)
    }

    override suspend fun getEvents(queueId: String, lastEventId: Int): List<ZulipEvent> {
        return zulipLongPollingApi.getEvents(queueId, lastEventId).events
    }

    override suspend fun deleteEventQueue(queueId: String) {
        zulipLongPollingApi.deleteEventQueue(queueId)
    }

}