package ru.gozerov.tfs_spring.screens.channels.chat.models

import ru.gozerov.core.DelegateItem

sealed interface ChatViewState {

    object Empty: ChatViewState

    class LoadedChat(
        val items: List<DelegateItem>,
        val positionToScroll: Int? = null
    ): ChatViewState

    class Error: ChatViewState

}