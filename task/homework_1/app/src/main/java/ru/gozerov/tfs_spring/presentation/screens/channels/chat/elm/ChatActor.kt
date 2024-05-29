package ru.gozerov.tfs_spring.presentation.screens.channels.chat.elm

import androidx.paging.Pager
import androidx.paging.PagingConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.gozerov.tfs_spring.core.DelegateItem
import ru.gozerov.tfs_spring.core.runCatchingNonCancellation
import ru.gozerov.tfs_spring.data.remote.paging.MessagePagingSource
import ru.gozerov.tfs_spring.domain.use_cases.AddReactionUseCase
import ru.gozerov.tfs_spring.domain.use_cases.DeleteEventQueueUseCase
import ru.gozerov.tfs_spring.domain.use_cases.GetEventsFromQueueUseCase
import ru.gozerov.tfs_spring.domain.use_cases.GetOwnUserIdUseCase
import ru.gozerov.tfs_spring.domain.use_cases.RegisterEventQueueUseCase
import ru.gozerov.tfs_spring.domain.use_cases.RemoveReactionUseCase
import ru.gozerov.tfs_spring.domain.use_cases.SendMessageUseCase
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.elm.models.ChatCommand
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.elm.models.ChatEvent
import vivid.money.elmslie.coroutines.Actor
import javax.inject.Inject

class ChatActor @Inject constructor(
    private val messagePagingSourceFactory: MessagePagingSource.Factory,
    private val sendMessageUseCase: SendMessageUseCase,
    private val registerEventQueueUseCase: RegisterEventQueueUseCase,
    private val addReactionUseCase: AddReactionUseCase,
    private val removeReactionUseCase: RemoveReactionUseCase,
    private val getEventsFromQueueUseCase: GetEventsFromQueueUseCase,
    private val deleteEventQueueUseCase: DeleteEventQueueUseCase,
    private val getOwnUserIdUseCase: GetOwnUserIdUseCase
) : Actor<ChatCommand, ChatEvent> {

    private var pagingSource: MessagePagingSource? = null

    override fun execute(command: ChatCommand): Flow<ChatEvent> =
        flow {

            when (command) {
                is ChatCommand.LoadChat -> {
                    runCatchingNonCancellation {
                        pagingSource = messagePagingSourceFactory.create(
                            command.stream,
                            command.topic,
                            command.fromCache
                        )
                        val pager = Pager<Int, DelegateItem>(
                            config = PagingConfig(
                                pageSize = 25,
                                initialLoadSize = 50
                            )
                        ) {
                            pagingSource!!
                        }
                        return@runCatchingNonCancellation pager.flow
                    }
                        .onSuccess { flow ->
                            emit(
                                ChatEvent.Internal.LoadChatSuccess(
                                    flow,
                                    command.fromCache,
                                    true
                                )
                            )
                        }
                        .onFailure {
                            emit(ChatEvent.Internal.LoadChatError)
                        }
                }

                is ChatCommand.RegisterEventQueue -> {
                    runCatchingNonCancellation {
                        registerEventQueueUseCase.invoke(command.topic)
                    }
                        .onSuccess { event ->
                            emit(
                                ChatEvent.Internal.RegisteredEventQueue(
                                    event.queue_id,
                                    event.last_event_id
                                )
                            )
                        }
                        .onFailure {
                            emit(ChatEvent.Internal.LoadChatError)
                        }
                }

                is ChatCommand.SendMessage -> {
                    runCatchingNonCancellation {
                        sendMessageUseCase.invoke(
                            command.channel,
                            command.topic,
                            command.content
                        )
                    }
                        .onFailure {
                            emit(ChatEvent.Internal.LoadChatError)
                        }
                }

                is ChatCommand.AddReaction -> {
                    runCatchingNonCancellation {
                        addReactionUseCase.invoke(command.messageId, command.emojiName)
                    }
                        .onFailure {
                            emit(ChatEvent.Internal.LoadChatError)
                        }
                }

                is ChatCommand.RemoveReaction -> {
                    runCatchingNonCancellation {
                        removeReactionUseCase.invoke(
                            command.messageId,
                            command.emojiName
                        )
                    }
                        .onFailure {
                            emit(ChatEvent.Internal.LoadChatError)
                        }
                }

                is ChatCommand.GetEventsFromQueue -> {
                    runCatchingNonCancellation {
                        getEventsFromQueueUseCase.invoke()
                    }
                        .onSuccess { events ->
                            val id = getOwnUserIdUseCase.invoke()
                            emit(ChatEvent.Internal.NewEventsFromQueue(events, id))
                        }
                        .onFailure {
                            emit(ChatEvent.Internal.LoadChatError)
                        }
                }

                is ChatCommand.Exit -> {
                    runCatchingNonCancellation {
                        deleteEventQueueUseCase.invoke()
                    }
                        .onFailure {
                            emit(ChatEvent.Internal.LoadChatError)
                        }
                }
            }
        }.flowOn(Dispatchers.Main)

}