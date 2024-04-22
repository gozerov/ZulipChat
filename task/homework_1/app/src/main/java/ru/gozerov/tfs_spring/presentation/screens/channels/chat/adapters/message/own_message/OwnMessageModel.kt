package ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters.message.own_message

import ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters.message.Reaction

data class OwnMessageModel(
    val id: Int,
    val message: String,
    val reactions: List<Reaction>
)