package ru.gozerov.tfs_spring.screens.channels.list

import android.graphics.Color
import androidx.core.graphics.toColor
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.gozerov.core.DelegateItem
import ru.gozerov.tfs_spring.screens.channels.list.adapters.channel.ChannelDelegateItem
import ru.gozerov.tfs_spring.screens.channels.list.adapters.channel.ChannelModel
import ru.gozerov.tfs_spring.screens.channels.list.adapters.topic.TopicDelegateItem
import ru.gozerov.tfs_spring.screens.channels.list.adapters.topic.TopicModel
import ru.gozerov.tfs_spring.screens.channels.list.models.ChannelListIntent
import ru.gozerov.tfs_spring.screens.channels.list.models.ChannelListViewState

class ChannelListViewModel : ViewModel() {

    private val _viewState = MutableStateFlow<ChannelListViewState>(ChannelListViewState.Empty)
    val viewState: StateFlow<ChannelListViewState> get() = _viewState.asStateFlow()

    private val categories = listOf("Subscribed", "All streams")

    fun handleIntent(intent: ChannelListIntent) {
        viewModelScope.launch {
            when (intent) {
                is ChannelListIntent.LoadChannels -> {
                    val list = listOf(
                        ChannelDelegateItem(
                            0, ChannelModel(
                                0,
                                "#general",
                                listOf(
                                    TopicDelegateItem(
                                        0,
                                        TopicModel(
                                            1,
                                            0,
                                            Color.parseColor("#FF2A9D8F").toColor(),
                                            "Testing",
                                            1240
                                        )
                                    ),
                                    TopicDelegateItem(
                                        1,
                                        TopicModel(
                                            2,
                                            0,
                                            Color.parseColor("#FFE9C46A").toColor(),
                                            "Bruh",
                                            24
                                        )
                                    )
                                )
                            )
                        ),

                        ChannelDelegateItem(
                            1, ChannelModel(
                                3,
                                "#Development",
                                listOf()
                            )
                        ),
                        ChannelDelegateItem(
                            2,
                            ChannelModel(
                                6,
                                "#general",
                                listOf(
                                    TopicDelegateItem(
                                        2,
                                        TopicModel(
                                            3,
                                            6,
                                            Color.parseColor("#FF2A9D8F").toColor(),
                                            "Testing",
                                            1240
                                        )
                                    ),
                                    TopicDelegateItem(
                                        3,
                                        TopicModel(
                                            4,
                                            6,
                                            Color.parseColor("#FFE9C46A").toColor(),
                                            "Bruh",
                                            24
                                        )
                                    )
                                )
                            )
                        )
                    )
                    if (_viewState.value !is ChannelListViewState.LoadedChannels)
                        _viewState.emit(
                            ChannelListViewState.LoadedChannels(
                                categories.associateWith { list }
                            )
                        )
                }

                is ChannelListIntent.ExpandItems -> {
                    val newList = mutableListOf<DelegateItem>()
                    val data =
                        (_viewState.value as ChannelListViewState.LoadedChannels).channels[categories[intent.categoryInd]]!!
                    data.forEachIndexed { ind, e ->
                        if (e.content() is ChannelModel && (e.content() as ChannelModel).id == intent.channel.id && ((ind != data.size - 1 && data[ind + 1] !is TopicDelegateItem) || ind == data.size - 1)) {
                            if ((e.content() as ChannelModel).topics.isNotEmpty())
                                newList.add(
                                    ChannelDelegateItem(
                                        e.id(),
                                        (e.content() as ChannelModel).copy(isExpanded = true)
                                    )
                                )
                            else
                                newList.add(e)
                            intent.channel.topics.forEach { newList.add(it) }
                        } else if (e is ChannelDelegateItem)
                            newList.add(
                                ChannelDelegateItem(
                                    e.id(),
                                    (e.content() as ChannelModel).copy(isExpanded = false)
                                )
                            )
                    }
                    _viewState.emit(ChannelListViewState.LoadedChannels(
                        categories.associateWith { newList }
                    ))
                }
            }
        }
    }

    fun getChannelById(topic: TopicModel): String {
        return ((_viewState.value as ChannelListViewState.LoadedChannels).channels.values.first()
            .first { it is ChannelDelegateItem && (it.content() as ChannelModel).id == topic.channelId }
            .content() as ChannelModel).title
    }

}