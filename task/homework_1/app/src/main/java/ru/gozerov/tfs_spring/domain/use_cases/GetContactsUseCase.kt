package ru.gozerov.tfs_spring.domain.use_cases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.tfs_spring.data.api.ZulipApi
import ru.gozerov.tfs_spring.data.api.models.UserContact

class GetContactsUseCase(
    private val zulipApi: ZulipApi
) {

    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        return@withContext zulipApi.getUsers().members.map {
            UserContact(
                it.user_id,
                it.avatar_url ?: "",
                it.full_name,
                it.delivery_email ?: "",
                it.is_active
            )
        }
    }

}