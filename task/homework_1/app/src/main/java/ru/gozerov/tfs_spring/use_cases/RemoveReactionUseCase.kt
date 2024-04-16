package ru.gozerov.tfs_spring.use_cases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.tfs_spring.api.ZulipApi

class RemoveReactionUseCase(
    private val zulipApi: ZulipApi
) {

    //TODO: убрать проверку на пустоту

    suspend operator fun invoke(messageId: Int, emojiName: String, reactionType: String) =
        withContext(Dispatchers.IO) {
            if (emojiName.isNotBlank())
                zulipApi.removeReaction(messageId, emojiName, reactionType)
        }

}