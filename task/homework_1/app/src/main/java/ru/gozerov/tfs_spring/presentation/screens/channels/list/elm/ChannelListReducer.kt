package ru.gozerov.tfs_spring.presentation.screens.channels.list.elm

import android.os.Handler
import android.os.Looper
import androidx.navigation.NavController
import ru.gozerov.tfs_spring.presentation.screens.channels.list.ChannelListFragmentDirections
import ru.gozerov.tfs_spring.presentation.screens.channels.list.elm.models.ChannelListCommand
import ru.gozerov.tfs_spring.presentation.screens.channels.list.elm.models.ChannelListEffect
import ru.gozerov.tfs_spring.presentation.screens.channels.list.elm.models.ChannelListEvent
import ru.gozerov.tfs_spring.presentation.screens.channels.list.elm.models.ChannelListState
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer
import javax.inject.Inject

class ChannelListReducer @Inject constructor(
    private val navController: NavController
) :
    DslReducer<ChannelListEvent, ChannelListState, ChannelListEffect, ChannelListCommand>() {

    override fun Result.reduce(event: ChannelListEvent): Unit = when (event) {
        is ChannelListEvent.Internal.SuccessLoadedChannels -> {
            state { copy(isLoading = false, channels = event.channels, isNavigating = false) }
        }

        is ChannelListEvent.Internal.ErrorLoadedChannels -> {
            state { copy(isLoading = false) }
            effects { +ChannelListEffect.ShowError }
        }

        is ChannelListEvent.Internal.SuccessLoadedChannel -> {
            val action =
                ChannelListFragmentDirections.actionNavChannelsToChatFragment(
                    event.topic,
                    event.channelName
                )
            Handler(Looper.getMainLooper()).post {
                navController.navigate(action)
            }
            Unit
        }

        is ChannelListEvent.Internal.ExpandedChannels -> {
            state {
                state.copy(
                    channels = state.channels?.mapValues { entry ->
                        if (entry.key == event.result.category) {
                            event.result.items
                        } else entry.value
                    }
                )
            }
        }

        is ChannelListEvent.UI.LoadChannels -> {

            state { copy(isLoading = true) }
            commands { +ChannelListCommand.LoadChannels }
        }

        is ChannelListEvent.UI.Exit -> {
            if (!state.isNavigating)
                commands { +ChannelListCommand.ClearSearch }
            Unit
        }

        is ChannelListEvent.UI.EnableSearch -> {
            state { copy(isNavigating = false) }
        }

        is ChannelListEvent.UI.ExpandItems -> {
            commands { +ChannelListCommand.ExpandItems(event.channel, event.categoryInd) }
        }

        is ChannelListEvent.UI.Search -> {
            if (!state.isNavigating) {
                state { copy(query = event.text) }
                commands { +ChannelListCommand.Search(event.text) }
            } else {
                effects { +ChannelListEffect.UpdateToolbar(state.query) }
            }
        }

        is ChannelListEvent.UI.LoadChannel -> {
            state { copy(isNavigating = true) }
            commands { +ChannelListCommand.LoadChannel(event.topic, event.categoryId) }
        }
    }

}