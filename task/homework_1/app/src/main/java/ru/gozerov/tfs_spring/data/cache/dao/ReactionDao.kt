package ru.gozerov.tfs_spring.data.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import ru.gozerov.tfs_spring.data.cache.entities.ReactionEntity

@Dao
interface ReactionDao {

    @Insert
    suspend fun saveReactions(reactions: List<ReactionEntity>)

}