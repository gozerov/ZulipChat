package ru.gozerov.tfs_spring.presentation.screens.channels.chat.elm.models

sealed interface ChatEffect {

    object ShowError : ChatEffect

}