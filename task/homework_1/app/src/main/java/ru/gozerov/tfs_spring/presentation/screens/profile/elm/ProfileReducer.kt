package ru.gozerov.tfs_spring.presentation.screens.profile.elm

import ru.gozerov.tfs_spring.presentation.screens.profile.elm.models.ProfileCommand
import ru.gozerov.tfs_spring.presentation.screens.profile.elm.models.ProfileEffect
import ru.gozerov.tfs_spring.presentation.screens.profile.elm.models.ProfileEvent
import ru.gozerov.tfs_spring.presentation.screens.profile.elm.models.ProfileState
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

class ProfileReducer : DslReducer<ProfileEvent, ProfileState, ProfileEffect, ProfileCommand>() {

    override fun Result.reduce(event: ProfileEvent) = when (event) {
        is ProfileEvent.Internal.SuccessLoadedProfile -> {
            state { copy(isLoading = false, userContact = event.userContact) }
        }

        is ProfileEvent.Internal.ErrorLoadedProfile -> {
            state { copy(isLoading = false) }
            effects { +ProfileEffect.ShowError }
        }

        is ProfileEvent.UI.Init -> {
            state { copy(isLoading = true) }
            commands { +ProfileCommand.LoadOwnProfile }
        }
    }

}