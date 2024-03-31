package ru.gozerov.tfs_spring.screens.profile

sealed class ProfileViewState {

    object Empty: ProfileViewState()

    class LoadedProfile(
        val imageUrl: String,
        val username: String,
        val status: String,
        val isOnline: Boolean
    ): ProfileViewState()

    class Error: ProfileViewState()

}