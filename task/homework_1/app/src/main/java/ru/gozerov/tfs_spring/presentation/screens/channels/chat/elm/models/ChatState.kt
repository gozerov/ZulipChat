package ru.gozerov.tfs_spring.presentation.screens.channels.chat.elm.models

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.gozerov.tfs_spring.core.DelegateItem

data class ChatState(
    val isLoading: Boolean = false,
    val flowItems: Flow<PagingData<DelegateItem>>? = null,
    val items: PagingData<DelegateItem>? = null,
    val positionToScroll: Int? = null
)