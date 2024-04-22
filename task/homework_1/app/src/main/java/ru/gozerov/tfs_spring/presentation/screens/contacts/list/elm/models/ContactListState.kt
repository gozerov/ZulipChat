package ru.gozerov.tfs_spring.presentation.screens.contacts.list.elm.models

import ru.gozerov.tfs_spring.data.api.models.UserContact

data class ContactListState(
    val isLoading: Boolean = false,
    val contacts: List<UserContact>? = null
)