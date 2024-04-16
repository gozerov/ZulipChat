package ru.gozerov.tfs_spring.use_cases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.tfs_spring.api.ZulipApi

class RegisterEventQueueUseCase(
    private val zulipApi: ZulipApi
) {

    suspend operator fun invoke(topic: String) = withContext(Dispatchers.IO) {
        return@withContext zulipApi.registerEventQueue(
            narrow = "[\n" +
                    "   [\"topic\", \"$topic\"]\n" +
                    "]"
        )
    }

}