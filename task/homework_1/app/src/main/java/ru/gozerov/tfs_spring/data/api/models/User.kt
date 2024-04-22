package ru.gozerov.tfs_spring.data.api.models

data class User(
    val user_id: Int,
    val full_name: String,
    val delivery_email: String?,
    val avatar_url: String?,
    val is_active: Boolean
)
