package ru.gozerov.tfs_spring.presentation.screens.channels.list.elm

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.gozerov.tfs_spring.core.runCatchingNonCancellation
import ru.gozerov.tfs_spring.domain.use_cases.ClearSearchUseCase
import ru.gozerov.tfs_spring.domain.use_cases.ExpandTopicsUseCase
import ru.gozerov.tfs_spring.domain.use_cases.GetChannelByIdUseCase
import ru.gozerov.tfs_spring.domain.use_cases.GetChannelsUseCase
import ru.gozerov.tfs_spring.domain.use_cases.LoadNewTopicsUseCase
import ru.gozerov.tfs_spring.domain.use_cases.SearchChannelsUseCase
import ru.gozerov.tfs_spring.presentation.screens.channels.list.elm.models.ChannelListCommand
import ru.gozerov.tfs_spring.presentation.screens.channels.list.elm.models.ChannelListEvent
import vivid.money.elmslie.coroutines.Actor
import javax.inject.Inject

class ChannelListActor @Inject constructor(
    private val getChannelsUseCase: GetChannelsUseCase,
    private val clearSearchUseCase: ClearSearchUseCase,
    private val expandTopicsUseCase: ExpandTopicsUseCase,
    private val getSearchResultUseCase: SearchChannelsUseCase,
    private val getChannelByIdUseCase: GetChannelByIdUseCase,
    private val loadNewTopicsUseCase: LoadNewTopicsUseCase

) : Actor<ChannelListCommand, ChannelListEvent> {

    override fun execute(command: ChannelListCommand): Flow<ChannelListEvent> = flow {
        when (command) {
            is ChannelListCommand.ClearSearch -> {
                runCatchingNonCancellation {
                    clearSearchUseCase.invoke()
                }
                    .onFailure {
                        emit(ChannelListEvent.Internal.ErrorLoadedChannels)
                    }
            }

            is ChannelListCommand.LoadChannels -> {
                runCatchingNonCancellation {
                    getChannelsUseCase.invoke()
                }
                    .onSuccess { itemsFlow ->
                        itemsFlow.collect { items ->
                            emit(ChannelListEvent.Internal.SuccessLoadedChannels(items))
                        }
                    }
                    .onFailure {
                        emit(ChannelListEvent.Internal.ErrorLoadedChannels)
                    }
            }

            is ChannelListCommand.ExpandItems -> {
                runCatchingNonCancellation {
                    expandTopicsUseCase.invoke(command.channel, command.categoryInd)
                }
                    .fold(
                        onSuccess = { result ->
                            emit(ChannelListEvent.Internal.ExpandedChannels(result))
                            loadNewTopicsUseCase.invoke(command.channel.id)
                        },
                        onFailure = {
                            emit(ChannelListEvent.Internal.ErrorLoadedChannels)
                        }
                    )
            }

            is ChannelListCommand.Search -> {
                runCatchingNonCancellation {
                    getSearchResultUseCase.invoke(command.text)
                }
                    .fold(
                        onSuccess = { channels ->
                            emit(ChannelListEvent.Internal.SuccessLoadedChannels(channels))
                        },
                        onFailure = {
                            emit(ChannelListEvent.Internal.ErrorLoadedChannels)
                        }
                    )
            }

            is ChannelListCommand.LoadChannel -> {
                runCatchingNonCancellation {
                    getChannelByIdUseCase.invoke(command.topic)
                }
                    .fold(
                        onSuccess = { channelName ->
                            emit(
                                ChannelListEvent.Internal.SuccessLoadedChannel(
                                    command.topic.title,
                                    channelName
                                )
                            )
                        },
                        onFailure = {
                            emit(ChannelListEvent.Internal.ErrorLoadedChannels)
                        }
                    )
            }
        }
    }

}