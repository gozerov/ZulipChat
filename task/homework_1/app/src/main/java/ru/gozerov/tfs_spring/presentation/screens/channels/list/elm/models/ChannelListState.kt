package ru.gozerov.tfs_spring.presentation.screens.channels.list.elm.models

import android.os.Parcelable
import ru.gozerov.tfs_spring.core.DelegateItem

data class ChannelListState(
    val isLoading: Boolean = false,
    val channels: Map<String, List<DelegateItem>>? = null,
    val scrollState: Parcelable? = null,
    val query: String = "",
    val isNavigating: Boolean = false
)