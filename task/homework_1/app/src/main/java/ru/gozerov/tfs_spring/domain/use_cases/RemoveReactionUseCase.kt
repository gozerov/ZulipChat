package ru.gozerov.tfs_spring.domain.use_cases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.tfs_spring.data.api.ZulipApi

class RemoveReactionUseCase(
    private val zulipApi: ZulipApi
) {
    suspend operator fun invoke(messageId: Int, emojiName: String) =
        withContext(Dispatchers.IO) {
            return@withContext zulipApi.removeReaction(messageId, emojiName)
        }

}