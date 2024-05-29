package ru.gozerov.tfs_spring.data.cache.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.gozerov.tfs_spring.data.cache.AppDatabase

@Entity(tableName = AppDatabase.MESSAGE_TABLE_NAME)
data class MessageEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "stream_name") val streamName: String,
    @ColumnInfo(name = "topic_name") val topicName: String,
    val timestamp: Long,
    @ColumnInfo(name = "avatar_url") val avatarUrl: String?,
    @ColumnInfo(name = "sender_id") val senderId: Int,
    @ColumnInfo(name = "sender_full_name") val senderName: String,
    @ColumnInfo(name = "is_me_message") val isMeMessage: Boolean,
    val content: String
)