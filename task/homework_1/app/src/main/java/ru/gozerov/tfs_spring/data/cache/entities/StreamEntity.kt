package ru.gozerov.tfs_spring.data.cache.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.gozerov.tfs_spring.data.cache.AppDatabase

@Entity(tableName = AppDatabase.STREAM_TABLE_NAME)
data class StreamEntity(
    @PrimaryKey val id: Int,
    val title: String,
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean
)
