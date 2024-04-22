package ru.gozerov.tfs_spring.presentation.screens.profile.elm.models

sealed interface ProfileCommand {

    object LoadOwnProfile : ProfileCommand

}