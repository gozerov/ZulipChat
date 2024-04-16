package ru.gozerov.tfs_spring.use_cases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.tfs_spring.api.ZulipApi
import ru.gozerov.tfs_spring.screens.contacts.list.models.UserContact

class GetUsersUseCase(
    private val zulipApi: ZulipApi
) {

    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        return@withContext zulipApi.getUsers().members.map { UserContact(it.user_id, it.avatar_url ?: "", it.full_name, it.delivery_email ?: "", it.is_active) }
    }

}