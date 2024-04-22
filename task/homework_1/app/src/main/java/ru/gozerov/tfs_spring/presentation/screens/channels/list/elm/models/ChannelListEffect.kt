package ru.gozerov.tfs_spring.presentation.screens.channels.list.elm.models

sealed interface ChannelListEffect {

    object ShowError : ChannelListEffect

    class LoadedChannel(
        val title: String,
        val channelName: String
    ) : ChannelListEffect

}