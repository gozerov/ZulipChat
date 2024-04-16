package ru.gozerov.tfs_spring.screens.channels.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.gozerov.core.runCatchingNonCancellation
import ru.gozerov.tfs_spring.api.ZulipApi
import ru.gozerov.tfs_spring.screens.channels.list.adapters.topic.TopicModel
import ru.gozerov.tfs_spring.screens.channels.list.models.ChannelListIntent
import ru.gozerov.tfs_spring.screens.channels.list.models.ChannelListViewState
import ru.gozerov.tfs_spring.use_cases.ExpandTopicsUseCase
import ru.gozerov.tfs_spring.use_cases.GetChannelByIdUseCase
import ru.gozerov.tfs_spring.use_cases.GetChannelsUseCase
import ru.gozerov.tfs_spring.use_cases.GetSearchResultUseCase

class ChannelListViewModel(
    private val zulipApi: ZulipApi
) : ViewModel() {

    private val _viewState = MutableStateFlow<ChannelListViewState>(ChannelListViewState.Empty)
    val viewState: StateFlow<ChannelListViewState> get() = _viewState.asStateFlow()

    fun handleIntent(intent: ChannelListIntent) {
        viewModelScope.launch {
            when (intent) {
                is ChannelListIntent.LoadChannels -> {
                    runCatchingNonCancellation {
                        GetChannelsUseCase(zulipApi).invoke()
                    }
                        .map {
                            _viewState.emit(ChannelListViewState.LoadedChannels(it))
                        }
                        .onFailure {
                            _viewState.emit(ChannelListViewState.Error())
                        }
                }

                is ChannelListIntent.ExpandItems -> {
                    runCatchingNonCancellation {
                        ExpandTopicsUseCase(intent.channel, intent.categoryInd)
                    }
                        .map {
                            _viewState.emit(ChannelListViewState.LoadedChannels(it))
                        }
                        .onFailure {
                            _viewState.emit(ChannelListViewState.Error())
                        }
                }

                is ChannelListIntent.Search -> {
                    runCatchingNonCancellation {
                        GetSearchResultUseCase(intent.text, intent.categoryPosition)
                    }
                        .map {
                            _viewState.emit(ChannelListViewState.LoadedChannels(it.channels))
                        }
                        .onFailure {
                            _viewState.emit(ChannelListViewState.Error())
                        }
                }
            }
        }
    }

    suspend fun getChannelById(topic: TopicModel, categoryId: Int): String {
        return GetChannelByIdUseCase.invoke(topic, categoryId)
    }

}