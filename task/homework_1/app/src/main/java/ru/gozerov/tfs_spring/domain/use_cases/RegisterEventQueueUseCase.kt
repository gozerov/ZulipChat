package ru.gozerov.tfs_spring.domain.use_cases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.tfs_spring.data.remote.api.models.RegisterEventQueueResponse
import ru.gozerov.tfs_spring.domain.repositories.ZulipRepository
import javax.inject.Inject

class RegisterEventQueueUseCase @Inject constructor(
    private val zulipRepository: ZulipRepository
) {

    suspend operator fun invoke(topic: String): RegisterEventQueueResponse =
        withContext(Dispatchers.IO) {
            return@withContext zulipRepository.registerEventQueue(
                narrow = "[\n" +
                        "   [\"topic\", \"$topic\"]\n" +
                        "]"
            )
        }

}