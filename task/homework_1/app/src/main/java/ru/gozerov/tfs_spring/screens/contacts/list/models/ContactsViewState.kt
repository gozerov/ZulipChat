package ru.gozerov.tfs_spring.screens.contacts.list.models

sealed class ContactsViewState {

    object Empty : ContactsViewState()

    class ContactsList(
        val contacts: List<UserContact>
    ) : ContactsViewState()

    class Error() : ContactsViewState()
}