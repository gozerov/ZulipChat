package ru.gozerov.tfs_spring.presentation.screens.contacts.list.elm.models

import ru.gozerov.tfs_spring.data.remote.api.models.UserContact


sealed interface ContactListEvent {

    sealed interface UI : ContactListEvent {

        object Init : UI

        class Search(
            val query: String
        ) : UI

    }

    sealed interface Internal : ContactListEvent {

        class SuccessLoadedContacts(
            val contacts: List<UserContact>
        ) : Internal

        object ErrorLoadedContacts : Internal

    }

}