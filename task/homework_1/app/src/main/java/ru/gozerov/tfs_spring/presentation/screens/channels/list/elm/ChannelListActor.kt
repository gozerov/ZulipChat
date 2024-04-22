package ru.gozerov.tfs_spring.presentation.screens.channels.list.elm

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.gozerov.tfs_spring.core.runCatchingNonCancellation
import ru.gozerov.tfs_spring.domain.use_cases.ExpandTopicsUseCase
import ru.gozerov.tfs_spring.domain.use_cases.GetChannelByIdUseCase
import ru.gozerov.tfs_spring.domain.use_cases.GetChannelsUseCase
import ru.gozerov.tfs_spring.domain.use_cases.SearchChannelsUseCase
import ru.gozerov.tfs_spring.presentation.screens.channels.list.elm.models.ChannelListCommand
import ru.gozerov.tfs_spring.presentation.screens.channels.list.elm.models.ChannelListEvent
import vivid.money.elmslie.coroutines.Actor

class ChannelListActor(
    private val getChannelsUseCase: GetChannelsUseCase,
    private val expandTopicsUseCase: ExpandTopicsUseCase,
    private val getSearchResultUseCase: SearchChannelsUseCase,
    private val getChannelByIdUseCase: GetChannelByIdUseCase

) : Actor<ChannelListCommand, ChannelListEvent> {

    override fun execute(command: ChannelListCommand): Flow<ChannelListEvent> = flow {
        when (command) {
            is ChannelListCommand.LoadChannels -> {
                runCatchingNonCancellation {
                    getChannelsUseCase.invoke()
                }
                    .fold(
                        onSuccess = {
                            emit(ChannelListEvent.Internal.SuccessLoadedChannels(it))
                        },
                        onFailure = {
                            emit(ChannelListEvent.Internal.ErrorLoadedChannels)
                        }
                    )
            }

            is ChannelListCommand.ExpandItems -> {
                runCatchingNonCancellation {
                    expandTopicsUseCase.invoke(command.channel, command.categoryInd)
                }
                    .fold(
                        onSuccess = {
                            emit(ChannelListEvent.Internal.SuccessLoadedChannels(it))
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
                        onSuccess = {
                            emit(ChannelListEvent.Internal.SuccessLoadedChannels(it.channels))
                        },
                        onFailure = {
                            emit(ChannelListEvent.Internal.ErrorLoadedChannels)
                        }
                    )
            }

            is ChannelListCommand.LoadChannel -> {
                runCatchingNonCancellation {
                    getChannelByIdUseCase.invoke(command.topic, command.categoryId)
                }
                    .fold(
                        onSuccess = {
                            emit(
                                ChannelListEvent.Internal.SuccessLoadedChannel(
                                    command.topic.title,
                                    it
                                )
                            )
                        },
                        onFailure = { }
                    )
            }
        }
    }

}