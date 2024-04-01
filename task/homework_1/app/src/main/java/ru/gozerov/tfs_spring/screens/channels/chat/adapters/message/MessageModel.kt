package ru.gozerov.tfs_spring.screens.channels.chat.adapters.message

data class MessageModel(
    val id: Int,
    val author: String,
    val authorId: Int,
    val message: String,
    val reactions: List<Reaction>
)