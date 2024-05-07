package ru.gozerov.tfs_spring.domain.use_cases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.tfs_spring.domain.repositories.ZulipRepository
import javax.inject.Inject

class AddReactionUseCase @Inject constructor(
    private val zulipRepository: ZulipRepository
) {
    suspend operator fun invoke(messageId: Int, emojiName: String) = withContext(Dispatchers.IO) {
        zulipRepository.addReaction(messageId, emojiName)
    }

}