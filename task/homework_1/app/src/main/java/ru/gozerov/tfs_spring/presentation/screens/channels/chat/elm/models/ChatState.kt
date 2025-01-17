package ru.gozerov.tfs_spring.presentation.screens.channels.chat.elm.models

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.gozerov.tfs_spring.core.DelegateItem

data class ChatState(
    val isLoading: Boolean = false,
    val fromCache: Boolean = true,
    val flowItems: Flow<PagingData<DelegateItem>>? = null,
    val items: PagingData<DelegateItem>? = null,
    val isFirstPage: Boolean = false,
    val isError: Boolean = false
)