package ru.gozerov.tfs_spring.domain.use_cases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.tfs_spring.data.remote.api.models.UserContact
import ru.gozerov.tfs_spring.domain.repositories.UsersRepository
import javax.inject.Inject

class GetOwnUserUseCase @Inject constructor(
    private val usersRepository: UsersRepository
) {

    suspend operator fun invoke(): UserContact = withContext(Dispatchers.IO) {
        return@withContext usersRepository.getOwnUser()
    }

}