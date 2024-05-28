package ru.gozerov.tfs_spring.data.cache.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.gozerov.tfs_spring.data.cache.AppDatabase


@Entity(tableName = AppDatabase.USERS_TABLE_NAME)
data class UserEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "image_url") val imageUrl: String?,
    val username: String,
    val email: String?,
    @ColumnInfo(name = "is_online") val isOnline: Boolean
)