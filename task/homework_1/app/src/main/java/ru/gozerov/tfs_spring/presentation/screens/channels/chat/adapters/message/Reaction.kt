package ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters.message

data class Reaction(
    val emojiName: String,
    val emojiCode: String,
    val count: Int,
    val isSelected: Boolean
)
