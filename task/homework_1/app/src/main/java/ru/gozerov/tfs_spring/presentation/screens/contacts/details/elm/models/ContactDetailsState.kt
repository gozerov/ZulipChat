package ru.gozerov.tfs_spring.presentation.screens.contacts.details.elm.models

import ru.gozerov.tfs_spring.data.remote.api.models.UserContact

data class ContactDetailsState(
    val isLoading: Boolean = false,
    val userContact: UserContact? = null
)