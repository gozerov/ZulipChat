package ru.gozerov.tfs_spring.data.remote.api.models

data class ZulipEvent(
    val type: String,
    val message: Message,
    val id: Int
)