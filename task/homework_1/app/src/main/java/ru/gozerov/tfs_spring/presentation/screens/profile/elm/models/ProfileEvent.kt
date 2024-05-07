package ru.gozerov.tfs_spring.presentation.screens.profile.elm.models

import ru.gozerov.tfs_spring.data.remote.api.models.UserContact

sealed interface ProfileEvent {

    sealed interface UI : ProfileEvent {

        object Init : UI

    }

    sealed interface Internal : ProfileEvent {

        class SuccessLoadedProfile(
            val userContact: UserContact
        ) : Internal

        object ErrorLoadedProfile : Internal

    }

}