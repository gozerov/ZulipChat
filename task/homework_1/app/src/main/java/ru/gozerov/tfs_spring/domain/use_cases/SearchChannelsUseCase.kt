package ru.gozerov.tfs_spring.domain.use_cases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.tfs_spring.data.api.models.SearchResult
import ru.gozerov.tfs_spring.domain.stubs.ChannelsStub
import ru.gozerov.tfs_spring.presentation.screens.channels.list.adapters.channel.ChannelDelegateItem
import ru.gozerov.tfs_spring.presentation.screens.channels.list.adapters.channel.ChannelModel
import ru.gozerov.tfs_spring.presentation.screens.channels.list.adapters.topic.TopicDelegateItem
import javax.inject.Inject

class SearchChannelsUseCase @Inject constructor() {

    suspend operator fun invoke(request: String): SearchResult =
        withContext(Dispatchers.IO) {
            if (request.isBlank()) {
                ChannelsStub.allCombinedChannels = ChannelsStub.originalAllCombinedChannels
                return@withContext SearchResult(ChannelsStub.allCombinedChannels)
            } else {
                val items = ChannelsStub.originalAllCombinedChannels.mapValues { entry ->
                    entry.value.filter {
                        (it is ChannelDelegateItem && (it.content() as ChannelModel).title.lowercase()
                            .contains(
                                request.lowercase()
                            )) || it is TopicDelegateItem
                    }
                }
                ChannelsStub.allCombinedChannels = items
                return@withContext SearchResult(items)
            }
        }

}