package ru.gozerov.tfs_spring.domain.use_cases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.tfs_spring.domain.repositories.ZulipRepository
import javax.inject.Inject

class DeleteEventQueueUseCase @Inject constructor(
    private val zulipRepository: ZulipRepository
) {

    suspend operator fun invoke(): Unit = withContext(Dispatchers.IO) {
        zulipRepository.deleteEventQueue()
    }

}