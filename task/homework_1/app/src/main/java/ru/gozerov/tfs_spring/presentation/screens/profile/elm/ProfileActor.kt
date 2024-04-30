package ru.gozerov.tfs_spring.presentation.screens.profile.elm

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.gozerov.tfs_spring.core.runCatchingNonCancellation
import ru.gozerov.tfs_spring.domain.use_cases.GetOwnUserUseCase
import ru.gozerov.tfs_spring.presentation.screens.profile.elm.models.ProfileCommand
import ru.gozerov.tfs_spring.presentation.screens.profile.elm.models.ProfileEvent
import vivid.money.elmslie.coroutines.Actor
import javax.inject.Inject

class ProfileActor @Inject constructor(
    private val getOwnUserUseCase: GetOwnUserUseCase
) : Actor<ProfileCommand, ProfileEvent> {

    override fun execute(command: ProfileCommand): Flow<ProfileEvent> =
        flow {
            when (command) {
                is ProfileCommand.LoadOwnProfile -> {
                    runCatchingNonCancellation {
                        getOwnUserUseCase.invoke()
                    }
                        .fold(
                            onSuccess = {
                                emit(ProfileEvent.Internal.SuccessLoadedProfile(it))
                            },
                            onFailure = {
                                emit(ProfileEvent.Internal.ErrorLoadedProfile)
                            }
                        )
                }
            }
        }


}