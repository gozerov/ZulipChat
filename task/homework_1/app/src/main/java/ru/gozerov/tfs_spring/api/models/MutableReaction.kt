package ru.gozerov.tfs_spring.api.models

data class MutableReaction(
    val emojiName: String,
    val type: String,
    val emojiCode: String,
    var count: Int,
    var isSelected: Boolean
)