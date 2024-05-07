package ru.gozerov.tfs_spring.data.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import ru.gozerov.tfs_spring.data.cache.entities.MessageEntity

@Dao
interface MessageDao {

    @Insert
    suspend fun saveMessages(messages: List<MessageEntity>)

}