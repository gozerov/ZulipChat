package ru.gozerov.tfs_spring.domain.use_cases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.tfs_spring.domain.stubs.ChannelsStub
import ru.gozerov.tfs_spring.presentation.screens.channels.list.adapters.channel.ChannelDelegateItem
import ru.gozerov.tfs_spring.presentation.screens.channels.list.adapters.channel.ChannelModel
import ru.gozerov.tfs_spring.presentation.screens.channels.list.adapters.topic.TopicModel
import javax.inject.Inject

class GetChannelByIdUseCase @Inject constructor() {

    suspend operator fun invoke(topic: TopicModel, categoryPosition: Int) =
        withContext(Dispatchers.IO) {
            return@withContext (ChannelsStub.allCombinedChannels[ChannelsStub.categories[categoryPosition]]
                ?.first { it is ChannelDelegateItem && (it.content() as ChannelModel).id == topic.channelId }
                ?.content() as ChannelModel).title
        }

}