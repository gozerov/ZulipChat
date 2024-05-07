package ru.gozerov.tfs_spring.data.cache.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.gozerov.tfs_spring.data.cache.AppDatabase

@Entity(tableName = AppDatabase.MESSAGE_TABLE_NAME)
data class MessageEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "topic_id") val topicId: Int,
    val timestamp: Long,
    val avatar_url: String,
    val sender_id: Int,
    val sender_full_name: String,
    val is_me_message: Boolean,
    val content: String
)