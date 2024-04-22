package ru.gozerov.tfs_spring.presentation.screens.contacts.details.elm

import ru.gozerov.tfs_spring.presentation.screens.contacts.details.elm.models.ContactDetailsCommand
import ru.gozerov.tfs_spring.presentation.screens.contacts.details.elm.models.ContactDetailsEffect
import ru.gozerov.tfs_spring.presentation.screens.contacts.details.elm.models.ContactDetailsEvent
import ru.gozerov.tfs_spring.presentation.screens.contacts.details.elm.models.ContactDetailsState
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

class ContactDetailsReducer :
    DslReducer<ContactDetailsEvent, ContactDetailsState, ContactDetailsEffect, ContactDetailsCommand>() {

    override fun Result.reduce(event: ContactDetailsEvent) = when (event) {
        is ContactDetailsEvent.Internal.SuccessLoadedProfile -> {
            state { copy(isLoading = false, userContact = event.userContact) }
        }

        is ContactDetailsEvent.Internal.ErrorLoadedProfile -> {
            state { copy(isLoading = false) }
            effects { +ContactDetailsEffect.ShowError }
        }

        is ContactDetailsEvent.UI.Init -> {
            state { copy(isLoading = true) }
            commands { +ContactDetailsCommand.LoadProfile(event.id) }
        }
    }

}