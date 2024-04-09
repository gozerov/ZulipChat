package ru.gozerov.tfs_spring.screens.channels.list.models

import ru.gozerov.tfs_spring.screens.channels.list.adapters.channel.ChannelModel

sealed interface ChannelListIntent {

    object LoadChannels : ChannelListIntent

    class ExpandItems(
        val channel: ChannelModel,
        val categoryInd: Int
    ) : ChannelListIntent

    class Search(
        val text: String,
        val categoryPosition: Int
    ) : ChannelListIntent

}