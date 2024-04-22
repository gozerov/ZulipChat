package ru.gozerov.tfs_spring.presentation.screens.channels.chat.dialog.elm

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.gozerov.tfs_spring.core.runCatchingNonCancellation
import ru.gozerov.tfs_spring.domain.use_cases.GetReactionsUseCase
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.dialog.elm.models.SelectEmojiCommand
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.dialog.elm.models.SelectEmojiEvent
import vivid.money.elmslie.coroutines.Actor

class SelectEmojiActor(
    private val getReactionsUseCase: GetReactionsUseCase
) : Actor<SelectEmojiCommand, SelectEmojiEvent> {

    override fun execute(command: SelectEmojiCommand): Flow<SelectEmojiEvent> = flow {
        when (command) {
            is SelectEmojiCommand.LoadEmojiList -> {
                runCatchingNonCancellation {
                    getReactionsUseCase.invoke()
                }
                    .fold(
                        onSuccess = {
                            emit(SelectEmojiEvent.Internal.SuccessLoadedReactions(it))
                        },
                        onFailure = {
                            emit(SelectEmojiEvent.Internal.ErrorLoadedReactions)
                        }
                    )
            }
        }
    }

}