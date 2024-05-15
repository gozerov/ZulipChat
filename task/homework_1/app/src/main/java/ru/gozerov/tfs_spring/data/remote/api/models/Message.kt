package ru.gozerov.tfs_spring.data.remote.api.models

data class Message(
    val id: Int,
    val timestamp: Long,
    val avatar_url: String?,
    val sender_id: Int,
    val sender_full_name: String,
    val is_me_message: Boolean,
    val content: String,
    val reactions: List<ZulipReaction>
)
