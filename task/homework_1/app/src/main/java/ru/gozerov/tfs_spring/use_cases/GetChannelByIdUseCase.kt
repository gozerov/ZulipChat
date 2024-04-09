package ru.gozerov.tfs_spring.use_cases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.tfs_spring.screens.channels.list.adapters.channel.ChannelDelegateItem
import ru.gozerov.tfs_spring.screens.channels.list.adapters.channel.ChannelModel
import ru.gozerov.tfs_spring.screens.channels.list.adapters.topic.TopicModel

object GetChannelByIdUseCase {

    suspend operator fun invoke(topic: TopicModel) = withContext(Dispatchers.IO) {
        return@withContext (ChannelsStub.combinedChannels.values.first()
            .first { it is ChannelDelegateItem && (it.content() as ChannelModel).id == topic.channelId }
            .content() as ChannelModel).title
    }

}