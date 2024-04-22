package ru.gozerov.tfs_spring.presentation.screens.channels.list.elm.models

import ru.gozerov.tfs_spring.presentation.screens.channels.list.adapters.channel.ChannelModel
import ru.gozerov.tfs_spring.presentation.screens.channels.list.adapters.topic.TopicModel

sealed interface ChannelListCommand {

    object LoadChannels : ChannelListCommand

    class ExpandItems(
        val channel: ChannelModel,
        val categoryInd: Int
    ) : ChannelListCommand

    class Search(
        val text: String
    ) : ChannelListCommand

    class LoadChannel(
        val topic: TopicModel,
        val categoryId: Int
    ) : ChannelListCommand

}