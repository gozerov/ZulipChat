package ru.gozerov.tfs_spring.domain.use_cases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.tfs_spring.data.api.ZulipApi
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val zulipApi: ZulipApi
) {

    suspend operator fun invoke(channel: String, topic: String, content: String) =
        withContext(Dispatchers.IO) {
            return@withContext zulipApi.sendMessage("stream", channel, topic, content).id
        }

}