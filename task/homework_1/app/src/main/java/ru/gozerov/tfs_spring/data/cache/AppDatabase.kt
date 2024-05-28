package ru.gozerov.tfs_spring.data.cache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.gozerov.tfs_spring.data.cache.dao.MessageDao
import ru.gozerov.tfs_spring.data.cache.dao.StreamDao
import ru.gozerov.tfs_spring.data.cache.dao.TopicDao
import ru.gozerov.tfs_spring.data.cache.dao.UserDao
import ru.gozerov.tfs_spring.data.cache.entities.MessageEntity
import ru.gozerov.tfs_spring.data.cache.entities.StreamEntity
import ru.gozerov.tfs_spring.data.cache.entities.TopicEntity
import ru.gozerov.tfs_spring.data.cache.entities.UserEntity

@Database(
    entities = [
        MessageEntity::class,
        StreamEntity::class,
        TopicEntity::class,
        UserEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getMessageDao(): MessageDao
    abstract fun getStreamDao(): StreamDao
    abstract fun getTopicDao(): TopicDao
    abstract fun getUsersDao(): UserDao

    companion object {

        private var database: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            return database ?: Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                DATABASE_NAME
            ).build()
        }

        private const val DATABASE_NAME = "app_database"
        const val STREAM_TABLE_NAME = "stream"
        const val TOPIC_TABLE_NAME = "topic"
        const val MESSAGE_TABLE_NAME = "message"
        const val USERS_TABLE_NAME = "users"

    }

}