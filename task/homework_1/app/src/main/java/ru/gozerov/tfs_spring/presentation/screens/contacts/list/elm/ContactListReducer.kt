package ru.gozerov.tfs_spring.presentation.screens.contacts.list.elm

import ru.gozerov.tfs_spring.presentation.screens.contacts.list.elm.models.ContactListCommand
import ru.gozerov.tfs_spring.presentation.screens.contacts.list.elm.models.ContactListEffect
import ru.gozerov.tfs_spring.presentation.screens.contacts.list.elm.models.ContactListEvent
import ru.gozerov.tfs_spring.presentation.screens.contacts.list.elm.models.ContactListState
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

class ContactListReducer :
    DslReducer<ContactListEvent, ContactListState, ContactListEffect, ContactListCommand>() {

    override fun Result.reduce(event: ContactListEvent) = when (event) {
        is ContactListEvent.Internal.SuccessLoadedContacts -> {
            state { copy(isLoading = false, contacts = event.contacts) }
        }

        is ContactListEvent.Internal.ErrorLoadedContacts -> {
            state { copy(isLoading = false) }
            effects { +ContactListEffect.ShowError }
        }

        is ContactListEvent.UI.Init -> {
            state { copy(isLoading = true) }
            commands { +ContactListCommand.GetContacts }
        }

        is ContactListEvent.UI.Search -> {
            commands { +ContactListCommand.SearchByName(event.query) }
        }
    }

}