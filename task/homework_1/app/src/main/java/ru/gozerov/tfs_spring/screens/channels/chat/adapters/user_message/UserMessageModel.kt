package ru.gozerov.tfs_spring.screens.channels.chat.adapters.user_message

import ru.gozerov.tfs_spring.screens.channels.chat.adapters.message.Reaction

data class UserMessageModel(
    val id: Int,
    val message: String,
    val reactions: List<Reaction>
)