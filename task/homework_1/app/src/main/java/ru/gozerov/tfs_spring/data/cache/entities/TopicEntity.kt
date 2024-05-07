package ru.gozerov.tfs_spring.data.cache.entities

import android.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.gozerov.tfs_spring.data.cache.AppDatabase

@Entity(tableName = AppDatabase.TOPIC_TABLE_NAME)
data class TopicEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    @ColumnInfo(name = "max_id") val maxId: Int,
    @ColumnInfo(name = "stream_id") val streamId: Int
)
