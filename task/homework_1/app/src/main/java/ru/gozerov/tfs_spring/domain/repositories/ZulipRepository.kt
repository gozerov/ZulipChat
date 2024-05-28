package ru.gozerov.tfs_spring.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.gozerov.tfs_spring.data.remote.api.models.Message
import ru.gozerov.tfs_spring.data.remote.api.models.RegisterEventQueueResponse
import ru.gozerov.tfs_spring.data.remote.api.models.Stream
import ru.gozerov.tfs_spring.data.remote.api.models.StreamTopic
import ru.gozerov.tfs_spring.data.remote.api.models.ZulipEvent

interface ZulipRepository {

    suspend fun getStreams(): Flow<Map<String, List<Stream>>>

    suspend fun getTopics(streamId: Int): List<StreamTopic>

    suspend fun clearSearch()

    suspend fun getMessages(
        numBefore: Int,
        numAfter: Int,
        anchor: String,
        narrow: String
    ): List<Message>

    suspend fun addReaction(messageId: Int, emojiName: String)

    suspend fun removeReaction(messageId: Int, emojiName: String)

    suspend fun sendMessage(type: String, to: String, topic: String, content: String): Int

    suspend fun registerEventQueue(narrow: String): RegisterEventQueueResponse

    suspend fun getEvents(): List<ZulipEvent>

    suspend fun deleteEventQueue()

    suspend fun getExpandedStreams(position: Int): List<Int>

    suspend fun setStreamExpand(position: Int, streamId: Int, isExpanded: Boolean)

    suspend fun getCachedStreams(position: Int): Pair<String, List<Stream>>

    suspend fun getStreamById(id: Int): Stream

    suspend fun searchStreams(query: String): Map<String, List<Stream>>

    suspend fun loadNewTopics(streamId: Int)

}