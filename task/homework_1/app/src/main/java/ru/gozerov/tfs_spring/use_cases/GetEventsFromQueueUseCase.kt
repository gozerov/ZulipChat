package ru.gozerov.tfs_spring.use_cases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.tfs_spring.api.ZulipApi

class GetEventsFromQueueUseCase(
    private val zulipApi: ZulipApi
) {

    suspend operator fun invoke(queueId: String, lastId: Int) = withContext(Dispatchers.IO) {
        return@withContext zulipApi.getEvents(queueId, lastId)
    }

}