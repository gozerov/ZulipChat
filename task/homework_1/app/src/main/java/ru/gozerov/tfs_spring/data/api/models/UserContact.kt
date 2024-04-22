package ru.gozerov.tfs_spring.data.api.models

data class UserContact(
    val id: Int,
    val imageUrl: String,
    val username: String,
    val email: String,
    val isOnline: Boolean
)