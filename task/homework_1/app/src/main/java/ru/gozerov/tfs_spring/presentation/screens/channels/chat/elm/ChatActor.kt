package ru.gozerov.tfs_spring.presentation.screens.channels.chat.elm

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import ru.gozerov.tfs_spring.core.DelegateItem
import ru.gozerov.tfs_spring.core.runCatchingNonCancellation
import ru.gozerov.tfs_spring.data.cache.dao.MessageDao
import ru.gozerov.tfs_spring.data.remote.paging.MessagePagingSource
import ru.gozerov.tfs_spring.domain.stubs.ChannelsStub
import ru.gozerov.tfs_spring.domain.use_cases.AddReactionUseCase
import ru.gozerov.tfs_spring.domain.use_cases.DeleteEventQueueUseCase
import ru.gozerov.tfs_spring.domain.use_cases.GetEventsFromQueueUseCase
import ru.gozerov.tfs_spring.domain.use_cases.RegisterEventQueueUseCase
import ru.gozerov.tfs_spring.domain.use_cases.RemoveReactionUseCase
import ru.gozerov.tfs_spring.domain.use_cases.SendMessageUseCase
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.elm.models.ChatCommand
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.elm.models.ChatEvent
import vivid.money.elmslie.coroutines.Actor
import javax.inject.Inject

class ChatActor @Inject constructor(
    private val messageDao: MessageDao,
    private val messagePagingSourceFactory: MessagePagingSource.Factory,
    private val sendMessageUseCase: SendMessageUseCase,
    private val registerEventQueueUseCase: RegisterEventQueueUseCase,
    private val addReactionUseCase: AddReactionUseCase,
    private val removeReactionUseCase: RemoveReactionUseCase,
    private val getEventsFromQueueUseCase: GetEventsFromQueueUseCase,
    private val deleteEventQueueUseCase: DeleteEventQueueUseCase
) : Actor<ChatCommand, ChatEvent> {

    private var pagingSource: MessagePagingSource? = null

    @OptIn(ExperimentalPagingApi::class)
    override fun execute(command: ChatCommand): Flow<ChatEvent> =
        flow {

            when (command) {
                is ChatCommand.LoadChat -> {
                    pagingSource = messagePagingSourceFactory.create(command.stream, command.topic, command.fromCache)
                    runCatchingNonCancellation {
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
                        .fold(
                            onSuccess = { flow ->
                                emit(ChatEvent.Internal.LoadChatSuccess(flow, command.fromCache))
                            },
                            onFailure = {
                                emit(ChatEvent.Internal.LoadChatError)
                            }
                        )

                }

                is ChatCommand.RegisterEventQueue -> {
                    runCatchingNonCancellation {
                        registerEventQueueUseCase.invoke(command.topic)
                    }
                        .fold(
                            onSuccess = {
                                emit(
                                    ChatEvent.Internal.RegisteredEventQueue(
                                        it.queue_id,
                                        it.last_event_id
                                    )
                                )
                                coroutineScope {
                                    launch {
                                        val events =
                                            getEventsFromQueueUseCase.invoke(
                                                it.queue_id,
                                                it.last_event_id
                                            )
                                        emit(ChatEvent.Internal.NewEventsFromQueue(events))
                                    }
                                }

                            },
                            onFailure = {
                                emit(ChatEvent.Internal.LoadChatError)
                            }
                        )
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
                }

                is ChatCommand.RemoveReaction -> {
                    runCatchingNonCancellation {
                        removeReactionUseCase.invoke(
                            command.messageId,
                            command.emojiName
                        )
                    }
                }

                is ChatCommand.GetEventsFromQueue -> {
                    runCatchingNonCancellation {
                        getEventsFromQueueUseCase.invoke(command.queueId, command.lastId)
                    }
                        .map {
                            emit(ChatEvent.Internal.NewEventsFromQueue(it))
                        }
                        .onFailure {
                            emit(ChatEvent.Internal.LoadChatError)
                        }
                }

                is ChatCommand.Exit -> {
                    runCatchingNonCancellation {
                        deleteEventQueueUseCase.invoke(ru.gozerov.tfs_spring.data.remote.api.EventQueueData.queueId)
                    }
                        .onFailure {
                            emit(ChatEvent.Internal.LoadChatError)
                        }
                }
            }
        }.flowOn(Dispatchers.Main)

}