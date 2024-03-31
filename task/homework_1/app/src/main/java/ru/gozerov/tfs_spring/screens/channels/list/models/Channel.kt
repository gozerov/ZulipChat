package ru.gozerov.tfs_spring.screens.channels.list.models

import ru.gozerov.tfs_spring.screens.channels.list.adapters.ChannelData

data class Channel(
    val id: Int,
    val title: String,
    val topics: List<Topic>,
    val isExpanded: Boolean = false
): ChannelData