package ru.gozerov.tfs_spring.domain.use_cases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.tfs_spring.data.remote.api.models.UserContact
import ru.gozerov.tfs_spring.domain.repositories.ZulipRepository
import javax.inject.Inject

class GetContactsUseCase @Inject constructor(
    private val zulipRepository: ZulipRepository
) {

    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        return@withContext zulipRepository.getUsers().map {
            UserContact(
                it.user_id,
                it.avatar_url,
                it.full_name,
                it.delivery_email,
                it.is_active
            )
        }
    }

}