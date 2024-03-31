package ru.gozerov.tfs_spring.screens.contacts.details.models

sealed class ContactDetailsViewState {

    object Empty: ContactDetailsViewState()

    class LoadedContact(
        val id: Int,
        val imageUrl: String,
        val username: String,
        val status: String,
        val isOnline: Boolean
    ): ContactDetailsViewState()

    class Error: ContactDetailsViewState()


}