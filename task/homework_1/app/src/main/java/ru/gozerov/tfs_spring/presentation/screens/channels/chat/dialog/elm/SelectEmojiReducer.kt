package ru.gozerov.tfs_spring.presentation.screens.channels.chat.dialog.elm

import ru.gozerov.tfs_spring.presentation.screens.channels.chat.dialog.elm.models.SelectEmojiCommand
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.dialog.elm.models.SelectEmojiEffect
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.dialog.elm.models.SelectEmojiEvent
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.dialog.elm.models.SelectEmojiState
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

class SelectEmojiReducer :
    DslReducer<SelectEmojiEvent, SelectEmojiState, SelectEmojiEffect, SelectEmojiCommand>() {

    override fun Result.reduce(event: SelectEmojiEvent) = when (event) {
        is SelectEmojiEvent.Internal.SuccessLoadedReactions -> {
            state { copy(reactions = event.reactions) }
        }

        is SelectEmojiEvent.Internal.ErrorLoadedReactions -> {
            effects { +SelectEmojiEffect.ShowError }
        }

        is SelectEmojiEvent.UI.LoadEmojiList -> {
            commands { +SelectEmojiCommand.LoadEmojiList }
        }
    }

}