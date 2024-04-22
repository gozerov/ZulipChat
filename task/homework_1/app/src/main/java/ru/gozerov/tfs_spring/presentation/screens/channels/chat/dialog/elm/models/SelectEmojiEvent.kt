package ru.gozerov.tfs_spring.presentation.screens.channels.chat.dialog.elm.models

import ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters.message.Reaction

sealed interface SelectEmojiEvent {

    sealed interface UI : SelectEmojiEvent {

        object LoadEmojiList : UI

    }

    sealed interface Internal : SelectEmojiEvent {

        class SuccessLoadedReactions(
            val reactions: List<Reaction>
        ) : Internal

        object ErrorLoadedReactions : Internal

    }

}