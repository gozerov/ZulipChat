package ru.gozerov.tfs_spring.screens.profile

import ru.gozerov.tfs_spring.screens.contacts.list.models.UserContact

sealed class ProfileViewState {

    object Empty: ProfileViewState()

    class LoadedProfile(
        val userContact: UserContact
    ): ProfileViewState()

    class Error: ProfileViewState()

}