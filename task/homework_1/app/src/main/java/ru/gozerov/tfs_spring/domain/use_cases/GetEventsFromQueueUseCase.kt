package ru.gozerov.tfs_spring.domain.use_cases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.tfs_spring.data.api.ZulipLongPollingApi

class GetEventsFromQueueUseCase(
    private val zulipLongPollingApi: ZulipLongPollingApi
) {

    suspend operator fun invoke(queueId: String, lastId: Int) = withContext(Dispatchers.IO) {
        return@withContext zulipLongPollingApi.getEvents(queueId, lastId)
    }

}