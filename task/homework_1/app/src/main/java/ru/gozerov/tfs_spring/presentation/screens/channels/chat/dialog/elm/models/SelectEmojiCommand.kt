package ru.gozerov.tfs_spring.presentation.screens.channels.chat.dialog.elm.models

sealed interface SelectEmojiCommand {

    object LoadEmojiList : SelectEmojiCommand

}