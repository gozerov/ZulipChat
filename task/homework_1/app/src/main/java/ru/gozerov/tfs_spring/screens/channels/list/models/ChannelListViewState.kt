package ru.gozerov.tfs_spring.screens.channels.list.models

import ru.gozerov.core.DelegateItem

sealed class ChannelListViewState {

    object Empty : ChannelListViewState()

    class LoadedChannels(
        val channels: Map<String, List<DelegateItem>>
    ) : ChannelListViewState()

    class Error : ChannelListViewState()

}