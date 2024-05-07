package ru.gozerov.tfs_spring.data.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.gozerov.tfs_spring.data.cache.entities.TopicEntity

@Dao
interface TopicDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTopics(streams: List<TopicEntity>)

    @Query("SELECT * FROM topic WHERE stream_id = :streamId")
    suspend fun getTopics(streamId: Int): List<TopicEntity>

    @Query("DELETE FROM topic WHERE stream_id = :streamId")
    suspend fun clear(streamId: Int)

}