package ru.gozerov.tfs_spring.presentation.screens.contacts.details.elm.models

sealed interface ContactDetailsCommand {

    class LoadProfile(
        val id: Int
    ) : ContactDetailsCommand

}