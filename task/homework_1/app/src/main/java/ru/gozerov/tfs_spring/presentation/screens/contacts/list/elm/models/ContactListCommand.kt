package ru.gozerov.tfs_spring.presentation.screens.contacts.list.elm.models

sealed interface ContactListCommand {

    object GetContacts : ContactListCommand

    class SearchByName(
        val name: String
    ) : ContactListCommand

}