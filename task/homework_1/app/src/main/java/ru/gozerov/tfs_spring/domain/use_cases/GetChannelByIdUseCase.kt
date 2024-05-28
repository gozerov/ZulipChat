package ru.gozerov.tfs_spring.domain.use_cases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.tfs_spring.domain.repositories.ZulipRepository
import ru.gozerov.tfs_spring.presentation.screens.channels.list.adapters.topic.TopicModel
import javax.inject.Inject

class GetChannelByIdUseCase @Inject constructor(
    private val zulipRepository: ZulipRepository
) {

    suspend operator fun invoke(topic: TopicModel): String = withContext(Dispatchers.IO) {
        return@withContext zulipRepository.getStreamById(topic.channelId).name
    }

}