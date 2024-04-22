package ru.gozerov.tfs_spring.presentation.screens.contacts.details.elm

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.gozerov.tfs_spring.core.runCatchingNonCancellation
import ru.gozerov.tfs_spring.domain.use_cases.GetContactByIdUseCase
import ru.gozerov.tfs_spring.presentation.screens.contacts.details.elm.models.ContactDetailsCommand
import ru.gozerov.tfs_spring.presentation.screens.contacts.details.elm.models.ContactDetailsEvent
import vivid.money.elmslie.coroutines.Actor

class ContactDetailsActor(
    private val getContactByIdUseCase: GetContactByIdUseCase
) : Actor<ContactDetailsCommand, ContactDetailsEvent> {

    override fun execute(command: ContactDetailsCommand): Flow<ContactDetailsEvent> =
        flow {
            when (command) {
                is ContactDetailsCommand.LoadProfile -> {
                    runCatchingNonCancellation {
                        getContactByIdUseCase.invoke(command.id)
                    }
                        .fold(
                            onSuccess = {
                                emit(ContactDetailsEvent.Internal.SuccessLoadedProfile(it))
                            },
                            onFailure = {
                                emit(ContactDetailsEvent.Internal.ErrorLoadedProfile)
                            }
                        )
                }
            }
        }


}