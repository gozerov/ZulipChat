package ru.gozerov.tfs_spring.screens.contacts.details.models

import ru.gozerov.tfs_spring.screens.contacts.list.models.UserContact

sealed class ContactDetailsViewState {

    object Empty: ContactDetailsViewState()

    class LoadedContact(
        val userContact: UserContact
    ): ContactDetailsViewState()

    class Error: ContactDetailsViewState()


}