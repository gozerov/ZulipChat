package ru.gozerov.tfs_spring.data.api.models

data class ZulipReaction(
    val emoji_name: String,
    val reaction_type: String,
    val emoji_code: String,
    val user_id: Int
)
