package ru.gozerov.tfs_spring.use_cases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

object GetChannelsUseCase {

    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        delay(2000)
        return@withContext ChannelsStub.combinedChannels
    }

}