package ru.gozerov.tfs_spring.data.cache.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.gozerov.tfs_spring.data.cache.AppDatabase

@Entity(tableName = AppDatabase.REACTION_TABLE_NAME)
data class ReactionEntity(
    @PrimaryKey @ColumnInfo(name = "emoji_name") val emojiName: String,
    @ColumnInfo(name = "emoji_code") val emojiCode: String,
    val count: Int,
    @ColumnInfo(name = "is_selected") val isSelected: Boolean,
    @ColumnInfo(name = "user_id") val userId: Int
)
