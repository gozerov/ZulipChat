package ru.gozerov.tfs_spring.screens.channels.list.models

sealed class ChannelListIntent {

    object LoadChannels : ChannelListIntent()

    class ExpandItems(val channel: Channel) : ChannelListIntent()

}