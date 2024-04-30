package ru.gozerov.tfs_spring.domain.use_cases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.tfs_spring.core.DelegateItem
import ru.gozerov.tfs_spring.domain.stubs.ChannelsStub
import ru.gozerov.tfs_spring.presentation.screens.channels.list.adapters.channel.ChannelDelegateItem
import ru.gozerov.tfs_spring.presentation.screens.channels.list.adapters.channel.ChannelModel
import ru.gozerov.tfs_spring.presentation.screens.channels.list.adapters.topic.TopicModel
import javax.inject.Inject

class ExpandTopicsUseCase @Inject constructor() {

    suspend operator fun invoke(channel: ChannelModel, categoryPosition: Int) =
        withContext(Dispatchers.IO) {
            val newList = mutableListOf<DelegateItem>()
            val category = ChannelsStub.categories[categoryPosition]
            val data = ChannelsStub.allCombinedChannels[category]!!
            data.forEach { e ->
                if (e.content() is ChannelModel && e.id() == channel.id) {
                    val model = e.content() as ChannelModel
                    newList.add(
                        ChannelDelegateItem(
                            e.id(),
                            (e.content() as ChannelModel).copy(isExpanded = !model.isExpanded)
                        )
                    )
                    if (!model.isExpanded) {
                        model.topics.forEach { newList.add(it) }
                    }
                } else if (e.content() is ChannelModel || (e.content() is TopicModel && (e.content() as TopicModel).channelId != channel.id))
                    newList.add(e)
            }
            ChannelsStub.allCombinedChannels = ChannelsStub.allCombinedChannels.mapValues {
                if (it.key == category) newList else it.value
            }
            return@withContext ChannelsStub.allCombinedChannels
        }

}