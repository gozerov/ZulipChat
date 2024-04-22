package ru.gozerov.tfs_spring.presentation.screens.channels.list.elm.models

import ru.gozerov.tfs_spring.core.DelegateItem

data class ChannelListState(
    val isLoading: Boolean = false,
    val channels: Map<String, List<DelegateItem>>? = null
)