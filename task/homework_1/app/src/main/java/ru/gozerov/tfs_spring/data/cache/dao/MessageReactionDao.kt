package ru.gozerov.tfs_spring.data.cache.dao

import androidx.room.Dao
import androidx.room.Query
import ru.gozerov.tfs_spring.data.cache.entities.MessageEntity

@Dao
interface MessageReactionDao {

    @Query("SELECT m.id, topic_id, m.timestamp, m.avatar_url, m.sender_id, m.sender_full_name, m.is_me_message, m.content " +
            "FROM message m " +
            "LEFT JOIN message_reactions ON m.id = message_reactions.message_id " +
            "LEFT JOIN reaction r ON message_reactions.reaction_name = r.emoji_name WHERE topic_id = :topicId")
    suspend fun getMessagesWithReaction(topicId: Int): List<MessageEntity>

}