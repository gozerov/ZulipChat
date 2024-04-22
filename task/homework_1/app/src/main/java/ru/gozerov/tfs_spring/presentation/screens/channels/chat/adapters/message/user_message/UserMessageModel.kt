package ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters.message.user_message

import ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters.message.Reaction

data class UserMessageModel(
    val id: Int,
    val author: String,
    val authorId: Int,
    val message: String,
    val reactions: List<Reaction>,
    val avatarUrl: String?
)