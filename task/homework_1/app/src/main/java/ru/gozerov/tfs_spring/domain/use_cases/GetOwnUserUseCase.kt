package ru.gozerov.tfs_spring.domain.use_cases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.tfs_spring.domain.mappers.toUserContact
import ru.gozerov.tfs_spring.domain.repositories.ZulipRepository
import javax.inject.Inject

class GetOwnUserUseCase @Inject constructor(
    private val zulipRepository: ZulipRepository
) {

    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        return@withContext zulipRepository.getOwnUser().toUserContact()
    }

}