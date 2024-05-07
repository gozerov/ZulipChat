package ru.gozerov.tfs_spring.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.gozerov.tfs_spring.data.remote.api.models.FullStream
import ru.gozerov.tfs_spring.data.remote.api.models.Message
import ru.gozerov.tfs_spring.data.remote.api.models.RegisterEventQueueResponse
import ru.gozerov.tfs_spring.data.remote.api.models.User
import ru.gozerov.tfs_spring.data.remote.api.models.ZulipEvent

interface ZulipRepository {

    suspend fun getUsers(): List<User>

    suspend fun getOwnUser(): User

    suspend fun getUserById(userId: Int): User

    suspend fun getStreams(): Flow<Map<String, List<FullStream>>>

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

    suspend fun getEvents(queueId: String, lastEventId: Int): List<ZulipEvent>

    suspend fun deleteEventQueue(queueId: String)

}