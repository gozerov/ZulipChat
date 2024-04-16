package ru.gozerov.tfs_spring.use_cases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.tfs_spring.screens.channels.list.adapters.channel.ChannelDelegateItem
import ru.gozerov.tfs_spring.screens.channels.list.adapters.channel.ChannelModel
import ru.gozerov.tfs_spring.screens.channels.list.adapters.topic.TopicDelegateItem
import kotlin.random.Random

object GetSearchResultUseCase {

    suspend operator fun invoke(request: String, categoryPosition: Int): SearchResult = withContext(Dispatchers.IO) {
        if (request.isBlank()) {
            ChannelsStub.allCombinedChannels = ChannelsStub.originalAllCombinedChannels
            return@withContext SearchResult(ChannelsStub.allCombinedChannels)
        } else {
            val category = ChannelsStub.categories[categoryPosition]
            val items = ChannelsStub.allCombinedChannels[category]?.filter {
                it is ChannelDelegateItem && (it.content() as ChannelModel).title.contains(
                    request
                ) || it is TopicDelegateItem
            }
            items?.let {
                if (Random.nextBoolean()) {
                    ChannelsStub.allCombinedChannels = ChannelsStub.allCombinedChannels.mapValues { if (it.key == category) items else it.value }
                    return@withContext SearchResult(ChannelsStub.allCombinedChannels)
                } else
                    error("ha-ha")
            } ?: error("unknown category")
        }
    }
}