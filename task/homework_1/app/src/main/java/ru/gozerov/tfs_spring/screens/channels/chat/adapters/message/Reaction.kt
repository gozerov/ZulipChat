package ru.gozerov.tfs_spring.screens.channels.chat.adapters.message

data class Reaction(
    val emojiCode: String,
    val count: Int,
    val isSelected: Boolean
)
