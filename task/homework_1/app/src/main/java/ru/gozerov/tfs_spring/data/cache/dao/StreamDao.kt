package ru.gozerov.tfs_spring.data.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.gozerov.tfs_spring.data.cache.entities.StreamEntity

@Dao
interface StreamDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveStreams(streams: List<StreamEntity>)

    @Query("SELECT * FROM stream")
    suspend fun getStreams(): List<StreamEntity>

    @Query("SELECT * FROM stream WHERE is_favorite = 1")
    suspend fun getSubscribedStreams(): List<StreamEntity>

    @Query("SELECT * FROM stream WHERE id=:id")
    suspend fun getStreamById(id: Int): StreamEntity

}