package ru.gozerov.tfs_spring.domain.use_cases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.tfs_spring.domain.stubs.UserStub
import javax.inject.Inject

class SearchContactsByNameUseCase @Inject constructor() {

    suspend operator fun invoke(query: String) = withContext(Dispatchers.IO) {
        return@withContext UserStub.users.filter {
            it.username.lowercase().contains(query.lowercase())
        }
    }

}