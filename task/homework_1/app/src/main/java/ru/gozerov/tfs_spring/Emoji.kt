package ru.gozerov.tfs_spring

data class Emoji(
    val emoji: String,
    val count: Int,
    val isSelected: Boolean = false
)
