package ru.gozerov.tfs_spring.domain.use_cases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.tfs_spring.data.api.ZulipApi
import ru.gozerov.tfs_spring.domain.mappers.toUserContact

class GetOwnUserUseCase(
    private val zulipApi: ZulipApi
) {

    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        return@withContext zulipApi.getOwnUser().toUserContact()
    }

}