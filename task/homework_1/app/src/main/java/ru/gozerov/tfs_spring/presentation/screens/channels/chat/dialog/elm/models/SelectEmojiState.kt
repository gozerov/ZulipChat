package ru.gozerov.tfs_spring.presentation.screens.channels.chat.dialog.elm.models

import ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters.message.Reaction

data class SelectEmojiState(
    val reactions: List<Reaction>? = null
)
