package ru.gozerov.tfs_spring.domain.use_cases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.tfs_spring.core.DelegateItem
import ru.gozerov.tfs_spring.domain.repositories.ZulipRepository
import ru.gozerov.tfs_spring.presentation.screens.channels.list.adapters.channel.ChannelDelegateItem
import ru.gozerov.tfs_spring.presentation.screens.channels.list.adapters.channel.ChannelModel
import javax.inject.Inject

class SearchChannelsUseCase @Inject constructor(
    private val zulipRepository: ZulipRepository
) {

    suspend operator fun invoke(request: String): Map<String, List<DelegateItem>> =
        withContext(Dispatchers.IO) {
            return@withContext zulipRepository.searchStreams(query = request).mapValues { entry ->
                entry.value.map { stream ->
                    ChannelDelegateItem(
                        stream.stream_id,
                        ChannelModel(stream.stream_id, stream.name, listOf(), false)
                    )
                }
            }
        }

}