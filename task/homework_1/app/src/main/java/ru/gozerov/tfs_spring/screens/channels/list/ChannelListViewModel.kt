package ru.gozerov.tfs_spring.screens.channels.list

import android.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.gozerov.tfs_spring.screens.channels.list.adapters.ChannelData
import ru.gozerov.tfs_spring.screens.channels.list.models.Channel
import ru.gozerov.tfs_spring.screens.channels.list.models.ChannelListIntent
import ru.gozerov.tfs_spring.screens.channels.list.models.ChannelListViewState
import ru.gozerov.tfs_spring.screens.channels.list.models.Topic

class ChannelListViewModel : ViewModel() {

    private val _viewState = MutableStateFlow<ChannelListViewState>(ChannelListViewState.Empty)
    val viewState: StateFlow<ChannelListViewState> get() = _viewState.asStateFlow()

    fun handleIntent(intent: ChannelListIntent) {
        viewModelScope.launch {
            when (intent) {
                is ChannelListIntent.LoadChannels -> {
                    if (_viewState.value !is ChannelListViewState.LoadedChannels)
                        _viewState.emit(
                            ChannelListViewState.LoadedChannels(
                                listOf(
                                    Channel(
                                        0,
                                        "#general",
                                        listOf(
                                            Topic(1, 0, Color.valueOf(11F, 42f, 42f), "Testing", 1240),
                                            Topic(2, 0, Color.valueOf(115F, 42f, 42f), "Bruh", 24)
                                        )
                                    ),
                                    Channel(
                                        3,
                                        "#Development",
                                        listOf()
                                    ),
                                    Channel(
                                        4,
                                        "#Design",
                                        listOf()
                                    ),
                                    Channel(
                                        5,
                                        "#PR",
                                        listOf()
                                    ),
                                    Channel(
                                        6,
                                        "#general",
                                        listOf(
                                            Topic(1, 6, Color.valueOf(11F, 42f, 42f), "Testing", 1240),
                                            Topic(2, 6, Color.valueOf(115F, 42f, 42f), "Bruh", 24)
                                        )
                                    ),
                                    Channel(
                                        7,
                                        "#general",
                                        listOf(
                                            Topic(1, 7, Color.valueOf(11F, 42f, 42f), "Testing", 1240),
                                            Topic(2, 7, Color.valueOf(115F, 42f, 42f), "Bruh", 24)
                                        )
                                    ),
                                    Channel(
                                        8,
                                        "#general",
                                        listOf(
                                            Topic(1, 8, Color.valueOf(11F, 42f, 42f), "Testing", 1240),
                                            Topic(2, 8, Color.valueOf(115F, 42f, 42f), "Bruh", 24)
                                        )
                                    ),
                                    Channel(
                                        9,
                                        "#general",
                                        listOf(
                                            Topic(1, 9, Color.valueOf(11F, 42f, 42f), "Testing", 1240),
                                            Topic(2, 9, Color.valueOf(115F, 42f, 42f), "Bruh", 24)
                                        )
                                    ),
                                    Channel(
                                        10,
                                        "#general",
                                        listOf(
                                            Topic(1, 10, Color.valueOf(11F, 42f, 42f), "Testing", 1240),
                                            Topic(2, 10, Color.valueOf(115F, 42f, 42f), "Bruh", 24)
                                        )
                                    ),
                                    Channel(
                                        11,
                                        "#general",
                                        listOf(
                                            Topic(1, 11, Color.valueOf(11F, 42f, 42f), "Testing", 1240),
                                            Topic(2, 11, Color.valueOf(115F, 42f, 42f), "Bruh", 24)
                                        )
                                    ),
                                )
                            )
                        )
                }

                is ChannelListIntent.ExpandItems -> {
                    val newList = mutableListOf<ChannelData>()
                    val data = (_viewState.value as ChannelListViewState.LoadedChannels).channels
                    data.forEachIndexed { ind, e ->
                        if (e is Channel && e.id == intent.channel.id && ind != data.size - 1 && data[ind + 1] !is Topic) {
                            if (e.topics.isNotEmpty())
                                newList.add(e.copy(isExpanded = true))
                            else
                                newList.add(e)
                            intent.channel.topics.forEach { newList.add(it) }
                        } else if (e is Channel)
                            newList.add(e.copy(isExpanded = false))
                    }
                    _viewState.emit(ChannelListViewState.LoadedChannels(newList.toList()))
                }
            }
        }
    }

}