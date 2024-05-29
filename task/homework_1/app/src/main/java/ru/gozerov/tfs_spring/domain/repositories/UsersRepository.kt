package ru.gozerov.tfs_spring.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.gozerov.tfs_spring.data.remote.api.models.UserContact

interface UsersRepository {

    suspend fun getUsers(): Flow<List<UserContact>>

    suspend fun getCachedUsers(): List<UserContact>

    suspend fun getOwnUser(): UserContact

    suspend fun getUserById(userId: Int): UserContact

    suspend fun getOwnUserId(): Int

}