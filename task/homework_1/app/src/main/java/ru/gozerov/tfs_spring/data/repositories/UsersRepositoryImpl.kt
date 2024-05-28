package ru.gozerov.tfs_spring.data.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.gozerov.tfs_spring.data.cache.dao.UserDao
import ru.gozerov.tfs_spring.data.cache.entities.toUserContact
import ru.gozerov.tfs_spring.data.cache.entities.toUserEntity
import ru.gozerov.tfs_spring.data.remote.api.ZulipApi
import ru.gozerov.tfs_spring.data.remote.api.models.UserContact
import ru.gozerov.tfs_spring.domain.mappers.toUserContact
import ru.gozerov.tfs_spring.domain.repositories.UsersRepository
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val zulipApi: ZulipApi,
    private val usersDao: UserDao
) : UsersRepository {

    override suspend fun getUsers(): Flow<List<UserContact>> = flow {

        val cachedUsers = usersDao.getUsers().map { userEntity -> userEntity.toUserContact() }
        emit(cachedUsers)
        try {
            val remoteUsers = zulipApi.getUsers().members.map { user -> user.toUserContact() }
            usersDao.saveUsers(remoteUsers.map { user -> user.toUserEntity() })
            emit(remoteUsers.sortedBy { user -> user.id })
        } catch (_: Exception) {
        }
    }

    override suspend fun getCachedUsers(): List<UserContact> {
        return usersDao.getUsers().map { userEntity -> userEntity.toUserContact() }
    }

    override suspend fun getOwnUser(): UserContact {
        return zulipApi.getOwnUser().toUserContact()
    }

    override suspend fun getUserById(userId: Int): UserContact {
        return zulipApi.getUserById(userId).user.toUserContact()
    }

}