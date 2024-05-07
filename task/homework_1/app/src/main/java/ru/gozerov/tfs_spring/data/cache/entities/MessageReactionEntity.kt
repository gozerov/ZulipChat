package ru.gozerov.tfs_spring.data.cache.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.gozerov.tfs_spring.data.cache.AppDatabase

@Entity(tableName = AppDatabase.MESSAGE_AND_REACTIONS_TABLE_NAME)
data class MessageReactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo("message_id") val messageId: Int,
    @ColumnInfo("reaction_name") val reactionName: String
)