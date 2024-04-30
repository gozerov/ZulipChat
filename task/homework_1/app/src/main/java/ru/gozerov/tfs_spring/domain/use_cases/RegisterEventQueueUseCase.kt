package ru.gozerov.tfs_spring.domain.use_cases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.tfs_spring.data.api.ZulipLongPollingApi
import javax.inject.Inject

class RegisterEventQueueUseCase @Inject constructor(
    private val zulipLongPollingApi: ZulipLongPollingApi
) {

    suspend operator fun invoke(topic: String) = withContext(Dispatchers.IO) {
        return@withContext zulipLongPollingApi.registerEventQueue(
            narrow = "[\n" +
                    "   [\"topic\", \"$topic\"]\n" +
                    "]"
        )
    }

}