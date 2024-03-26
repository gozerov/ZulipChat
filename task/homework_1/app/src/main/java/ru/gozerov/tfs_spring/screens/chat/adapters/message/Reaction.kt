package ru.gozerov.tfs_spring.screens.chat.adapters.message

data class Reaction(
    val emojiCode: String,
    val count: Int,
    val isSelected: Boolean
)
