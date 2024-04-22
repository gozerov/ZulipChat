package ru.gozerov.tfs_spring.presentation.screens.contacts.list.elm.models

sealed interface ContactListEffect {

    object ShowError : ContactListEffect

}