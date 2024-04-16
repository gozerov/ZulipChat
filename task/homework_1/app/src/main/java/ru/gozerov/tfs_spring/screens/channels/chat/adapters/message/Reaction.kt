package ru.gozerov.tfs_spring.screens.channels.chat.adapters.message

data class Reaction(
    val emojiName: String,
    val emojiCode: String,
    val emojiType: String,
    val count: Int,
    val isSelected: Boolean
)
