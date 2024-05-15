package ru.gozerov.tfs_spring.data.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.gozerov.tfs_spring.data.cache.entities.MessageEntity

@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMessages(messages: List<MessageEntity>)

    @Query("SELECT * FROM message WHERE stream_name=:streamName AND topic_name=:topicName")
    suspend fun getMessages(streamName: String, topicName: String): List<MessageEntity>

    @Query("DELETE FROM message WHERE stream_name=:streamName AND topic_name=:topicName")
    suspend fun clear(streamName: String, topicName: String)

}