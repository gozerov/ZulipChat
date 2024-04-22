package ru.gozerov.tfs_spring.domain.use_cases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.tfs_spring.domain.stubs.UserStub

object SearchContactsByNameUseCase {

    suspend operator fun invoke(query: String) = withContext(Dispatchers.IO) {
        return@withContext UserStub.users.filter { it.username.lowercase().contains(query.lowercase()) }
    }

}