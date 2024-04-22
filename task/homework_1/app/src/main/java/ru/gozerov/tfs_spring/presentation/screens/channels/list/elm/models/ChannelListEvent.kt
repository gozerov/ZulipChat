package ru.gozerov.tfs_spring.presentation.screens.channels.list.elm.models

import ru.gozerov.tfs_spring.core.DelegateItem
import ru.gozerov.tfs_spring.presentation.screens.channels.list.adapters.channel.ChannelModel
import ru.gozerov.tfs_spring.presentation.screens.channels.list.adapters.topic.TopicModel

sealed interface ChannelListEvent {

    sealed interface UI : ChannelListEvent {

        object LoadChannels : UI

        class ExpandItems(
            val channel: ChannelModel,
            val categoryInd: Int
        ) : UI

        class Search(
            val text: String
        ) : UI

        class LoadChannel(
            val topic: TopicModel,
            val categoryId: Int
        ) : UI

    }

    sealed interface Internal : ChannelListEvent {

        class SuccessLoadedChannels(
            val channels: Map<String, List<DelegateItem>>
        ) : Internal

        class SuccessLoadedChannel(
            val topic: String,
            val channelName: String
        ) : Internal

        object ErrorLoadedChannels : Internal

    }

}