package ru.gozerov.tfs_spring.presentation.screens.channels.chat.elm.models

import ru.gozerov.tfs_spring.core.DelegateItem

data class ChatState(
    val isLoading: Boolean = false,
    val items: List<DelegateItem>? = null,
    val positionToScroll: Int? = null
)