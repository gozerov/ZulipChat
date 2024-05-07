package ru.gozerov.tfs_spring.presentation.screens.profile.elm.models

import ru.gozerov.tfs_spring.data.remote.api.models.UserContact

data class ProfileState(
    val isLoading: Boolean = false,
    val userContact: UserContact? = null
)