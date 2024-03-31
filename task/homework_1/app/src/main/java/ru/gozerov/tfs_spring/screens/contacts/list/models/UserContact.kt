package ru.gozerov.tfs_spring.screens.contacts.list.models

data class UserContact(
    val id: Int,
    val imageUrl: String,
    val username: String,
    val email: String,
    val isOnline: Boolean
)