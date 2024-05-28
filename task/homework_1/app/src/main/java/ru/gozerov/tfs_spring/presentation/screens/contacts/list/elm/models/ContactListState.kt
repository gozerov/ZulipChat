package ru.gozerov.tfs_spring.presentation.screens.contacts.list.elm.models

import kotlinx.coroutines.flow.Flow
import ru.gozerov.tfs_spring.data.remote.api.models.UserContact

data class ContactListState(
    val isLoading: Boolean = false,
    val contacts: Flow<List<UserContact>>? = null
)