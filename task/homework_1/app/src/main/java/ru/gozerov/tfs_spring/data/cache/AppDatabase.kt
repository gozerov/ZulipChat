package ru.gozerov.tfs_spring.data.cache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.gozerov.tfs_spring.data.cache.dao.MessageDao
import ru.gozerov.tfs_spring.data.cache.dao.MessageReactionDao
import ru.gozerov.tfs_spring.data.cache.dao.ReactionDao
import ru.gozerov.tfs_spring.data.cache.dao.StreamDao
import ru.gozerov.tfs_spring.data.cache.dao.TopicDao
import ru.gozerov.tfs_spring.data.cache.entities.MessageEntity
import ru.gozerov.tfs_spring.data.cache.entities.MessageReactionEntity
import ru.gozerov.tfs_spring.data.cache.entities.ReactionEntity
import ru.gozerov.tfs_spring.data.cache.entities.StreamEntity
import ru.gozerov.tfs_spring.data.cache.entities.TopicEntity

@Database(
    entities = [
        MessageEntity::class,
        MessageReactionEntity::class,
        ReactionEntity::class,
        StreamEntity::class,
        TopicEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getMessageDao(): MessageDao
    abstract fun getMessageReactionDao(): MessageReactionDao
    abstract fun getReactionDao(): ReactionDao
    abstract fun getStreamDao(): StreamDao
    abstract fun getTopicDao(): TopicDao

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
        const val REACTION_TABLE_NAME = "reaction"
        const val MESSAGE_AND_REACTIONS_TABLE_NAME = "message_reactions"

    }

}