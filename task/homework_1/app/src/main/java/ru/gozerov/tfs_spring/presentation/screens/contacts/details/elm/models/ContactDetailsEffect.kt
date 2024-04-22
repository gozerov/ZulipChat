package ru.gozerov.tfs_spring.presentation.screens.contacts.details.elm.models

sealed interface ContactDetailsEffect {

    object ShowError : ContactDetailsEffect

}