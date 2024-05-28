package ru.gozerov.tfs_spring.domain.use_cases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.gozerov.tfs_spring.core.DelegateItem
import ru.gozerov.tfs_spring.domain.repositories.ZulipRepository
import ru.gozerov.tfs_spring.presentation.screens.channels.list.adapters.channel.ChannelDelegateItem
import ru.gozerov.tfs_spring.presentation.screens.channels.list.adapters.channel.ChannelModel
import javax.inject.Inject

class GetChannelsUseCase @Inject constructor(
    private val zulipRepository: ZulipRepository
) {

    suspend operator fun invoke(): Flow<Map<String, List<DelegateItem>>> =
        withContext(Dispatchers.IO) {
            return@withContext zulipRepository.getStreams().map { streamMap ->
                streamMap.mapValues { entry ->
                    entry.value.map { stream ->
                        ChannelDelegateItem(
                            stream.stream_id,
                            ChannelModel(stream.stream_id, stream.name, listOf())
                        )
                    }
                }
            }

        }

}