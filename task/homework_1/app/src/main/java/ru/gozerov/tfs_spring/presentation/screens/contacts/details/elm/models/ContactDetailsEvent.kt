package ru.gozerov.tfs_spring.presentation.screens.contacts.details.elm.models

import ru.gozerov.tfs_spring.data.remote.api.models.UserContact

sealed interface ContactDetailsEvent {

    sealed interface UI : ContactDetailsEvent {

        class Init(
            val id: Int
        ) : UI

    }

    sealed interface Internal : ContactDetailsEvent {

        class SuccessLoadedProfile(
            val userContact: UserContact
        ) : Internal

        object ErrorLoadedProfile : Internal

    }

}