package ru.gozerov.tfs_spring.screens.channels.list.adapters.channel

import ru.gozerov.tfs_spring.screens.channels.list.adapters.topic.TopicDelegateItem

data class ChannelModel(
    val id: Int,
    val title: String,
    val topics: List<TopicDelegateItem>,
    val isExpanded: Boolean = false
)