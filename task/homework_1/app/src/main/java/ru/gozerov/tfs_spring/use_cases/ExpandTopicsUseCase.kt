package ru.gozerov.tfs_spring.use_cases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.core.DelegateItem
import ru.gozerov.tfs_spring.screens.channels.list.adapters.channel.ChannelDelegateItem
import ru.gozerov.tfs_spring.screens.channels.list.adapters.channel.ChannelModel
import ru.gozerov.tfs_spring.screens.channels.list.adapters.topic.TopicDelegateItem

object ExpandTopicsUseCase {

    suspend operator fun invoke(channel: ChannelModel, categoryPosition: Int) =
        withContext(Dispatchers.IO) {
            val newList = mutableListOf<DelegateItem>()
            val category = ChannelsStub.categories[categoryPosition]
            val data = ChannelsStub.combinedChannels[category]!!
            data.forEachIndexed { ind, e ->
                if (e.content() is ChannelModel && (e.content() as ChannelModel).id == channel.id && ((ind != data.size - 1 && data[ind + 1] !is TopicDelegateItem) || ind == data.size - 1)) {
                    if ((e.content() as ChannelModel).topics.isNotEmpty())
                        newList.add(
                            ChannelDelegateItem(
                                e.id(),
                                (e.content() as ChannelModel).copy(isExpanded = true)
                            )
                        )
                    else
                        newList.add(e)
                    channel.topics.forEach { newList.add(it) }
                } else if (e is ChannelDelegateItem)
                    newList.add(
                        ChannelDelegateItem(
                            e.id(),
                            (e.content() as ChannelModel).copy(isExpanded = false)
                        )
                    )
            }
            ChannelsStub.combinedChannels = ChannelsStub.combinedChannels.mapValues {
                if (it.key == category) newList else it.value
            }
            return@withContext ChannelsStub.combinedChannels
        }

}