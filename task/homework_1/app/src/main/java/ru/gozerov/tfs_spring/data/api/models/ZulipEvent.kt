package ru.gozerov.tfs_spring.data.api.models

data class ZulipEvent(
    val type: String,
    val message: Message,
    val id: Int
)
