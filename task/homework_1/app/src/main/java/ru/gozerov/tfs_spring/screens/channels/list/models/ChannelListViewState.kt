package ru.gozerov.tfs_spring.screens.channels.list.models

import ru.gozerov.tfs_spring.screens.channels.list.adapters.ChannelData

sealed class ChannelListViewState {

    object Empty : ChannelListViewState()

    class LoadedChannels(
        val channels: List<ChannelData>
    ) : ChannelListViewState()

    class Error : ChannelListViewState()

}