package ru.gozerov.tfs_spring.use_cases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.tfs_spring.api.ZulipApi

class GetContactById(
    private val zulipApi: ZulipApi
) {

    suspend operator fun invoke(id: Int) = withContext(Dispatchers.IO) {
        return@withContext zulipApi.getUserById(id).user.toUserContact()
    }

}