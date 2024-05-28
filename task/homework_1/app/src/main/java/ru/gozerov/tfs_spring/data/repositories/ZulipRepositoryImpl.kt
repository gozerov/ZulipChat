package ru.gozerov.tfs_spring.data.repositories

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
import ru.gozerov.tfs_spring.data.remote.api.models.Message
import ru.gozerov.tfs_spring.data.remote.api.models.RegisterEventQueueResponse
import ru.gozerov.tfs_spring.data.remote.api.models.Stream
import ru.gozerov.tfs_spring.data.remote.api.models.StreamTopic
import ru.gozerov.tfs_spring.data.remote.api.models.ZulipEvent
import ru.gozerov.tfs_spring.domain.repositories.ZulipRepository
import javax.inject.Inject

class ZulipRepositoryImpl @Inject constructor(
    private val zulipApi: ZulipApi,
    private val zulipLongPollingApi: ZulipLongPollingApi,
    private val streamDao: StreamDao,
    private val topicDao: TopicDao
) : ZulipRepository {

    private var searchedStreams = mapOf<String, List<Stream>>()

    private val expandedStreamIds = listOf(mutableListOf<Int>(), mutableListOf())

    private var eventQueueId = ""
    private var lastEventId = -1

    override suspend fun getStreams(): Flow<Map<String, List<Stream>>> = flow {
        val cacheItems = mutableMapOf<String, List<Stream>>()

        val cacheSubscribedStreams = streamDao.getSubscribedStreams().map { stream ->
            stream.toStream()
        }
        cacheItems[CATEGORY_SUBSCRIBED] = cacheSubscribedStreams

        val cacheStreams = streamDao.getStreams().map { stream -> stream.toStream() }
        cacheItems[CATEGORY_ALL] = cacheStreams

        searchedStreams = cacheItems
        emit(cacheItems)

        val remoteStreams = zulipApi.getAllStreams().streams
        streamDao.saveStreams(remoteStreams.map { it.toStreamEntity() })

        val remoteSubscribedStreams = zulipApi.getSubscribedStreams().subscriptions
        streamDao.saveStreams(remoteSubscribedStreams.map { it.toStreamEntity(isFavorite = true) })

        emit(
            mapOf(
                CATEGORY_SUBSCRIBED to remoteSubscribedStreams.sortedBy { it.stream_id },
                CATEGORY_ALL to remoteStreams.sortedBy { it.stream_id }
            ).apply {
                searchedStreams = this
            }
        )
    }

    override suspend fun getTopics(streamId: Int): List<StreamTopic> {
        val cachedTopics = topicDao.getTopics(streamId).map { topicEntity -> topicEntity.toTopic() }
        if (cachedTopics.isNotEmpty()) return cachedTopics

        val remoteTopics = zulipApi.getStreamTopics(streamId).topics
        topicDao.clear(streamId)
        topicDao.saveTopics(remoteTopics.map { topic -> topic.toTopicEntity(streamId) })
        return remoteTopics
    }

    override suspend fun clearSearch() {
        val cacheItems = mutableMapOf<String, List<Stream>>()

        val cacheSubscribedStreams = streamDao.getSubscribedStreams().map { stream ->
            stream.toStream()
        }
        cacheItems[CATEGORY_SUBSCRIBED] = cacheSubscribedStreams

        val cacheStreams = streamDao.getStreams().map { stream -> stream.toStream() }
        cacheItems[CATEGORY_ALL] = cacheStreams

        searchedStreams = cacheItems
    }

    override suspend fun loadNewTopics(streamId: Int) {
        val remoteTopics = zulipApi.getStreamTopics(streamId).topics
        topicDao.clear(streamId)
        topicDao.saveTopics(remoteTopics.map { topic -> topic.toTopicEntity(streamId) })
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
        val response = zulipLongPollingApi.registerEventQueue(narrow = narrow)
        eventQueueId = response.queue_id
        lastEventId = response.last_event_id
        return response
    }

    override suspend fun getEvents(): List<ZulipEvent> {
        val response = zulipLongPollingApi.getEvents(eventQueueId, lastEventId).events
        response.forEach { event ->
            lastEventId = event.id
        }
        return response
    }

    override suspend fun deleteEventQueue() {
        zulipLongPollingApi.deleteEventQueue(eventQueueId)
        lastEventId = -1
        eventQueueId = ""
    }

    override suspend fun getExpandedStreams(position: Int): List<Int> {
        return expandedStreamIds[position]
    }

    override suspend fun setStreamExpand(position: Int, streamId: Int, isExpanded: Boolean) {
        if (!isExpanded) expandedStreamIds[position].add(streamId) else expandedStreamIds[position].removeIf { id -> id == streamId }
    }

    override suspend fun getCachedStreams(position: Int): Pair<String, List<Stream>> {
        return when (position) {
            POSITION_SUBSCRIBED -> CATEGORY_SUBSCRIBED to
                    searchedStreams.getValue(CATEGORY_SUBSCRIBED)

            POSITION_ALL -> CATEGORY_ALL to
                    searchedStreams.getValue(CATEGORY_ALL)

            else -> error("Unexpected stream category")
        }
    }

    override suspend fun getStreamById(id: Int): Stream {
        return streamDao.getStreamById(id).toStream()
    }

    override suspend fun searchStreams(query: String): Map<String, List<Stream>> {
        val cacheSubscribedStreams = CATEGORY_SUBSCRIBED to streamDao.getSubscribedStreams().map { stream -> stream.toStream() }
        val cacheAllStreams = CATEGORY_ALL to streamDao.getStreams().map { stream -> stream.toStream() }

        return if (query.isBlank()) {
            mapOf(cacheSubscribedStreams, cacheAllStreams).apply {
                searchedStreams = this
            }
        } else {
            val searchedSubscribedStreams = cacheSubscribedStreams.second.filter { stream ->
                stream.name.lowercase().contains(query.lowercase())
            }
            val searchedAllStreams = cacheAllStreams.second.filter { stream ->
                stream.name.lowercase().contains(query.lowercase())
            }
            mapOf(
                cacheSubscribedStreams.first to searchedSubscribedStreams,
                cacheAllStreams.first to searchedAllStreams
            ).apply {
                searchedStreams = this
            }
        }
    }

    companion object {

        const val POSITION_SUBSCRIBED = 0
        const val POSITION_ALL = 1
        const val CATEGORY_SUBSCRIBED = "Subscribed"
        const val CATEGORY_ALL = "All streams"

    }

}