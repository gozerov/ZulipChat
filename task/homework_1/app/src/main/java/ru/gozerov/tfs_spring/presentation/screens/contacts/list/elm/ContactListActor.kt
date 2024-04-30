package ru.gozerov.tfs_spring.presentation.screens.contacts.list.elm

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.gozerov.tfs_spring.core.runCatchingNonCancellation
import ru.gozerov.tfs_spring.domain.stubs.UserStub
import ru.gozerov.tfs_spring.domain.use_cases.GetContactsUseCase
import ru.gozerov.tfs_spring.domain.use_cases.SearchContactsByNameUseCase
import ru.gozerov.tfs_spring.presentation.screens.contacts.list.elm.models.ContactListCommand
import ru.gozerov.tfs_spring.presentation.screens.contacts.list.elm.models.ContactListEvent
import vivid.money.elmslie.coroutines.Actor
import javax.inject.Inject

class ContactListActor @Inject constructor(
    private val getContactsUseCase: GetContactsUseCase,
    private val searchContactsByNameUseCase: SearchContactsByNameUseCase
) : Actor<ContactListCommand, ContactListEvent> {

    override fun execute(command: ContactListCommand): Flow<ContactListEvent> = flow {
        when (command) {
            is ContactListCommand.GetContacts -> {
                runCatchingNonCancellation {
                    getContactsUseCase.invoke()
                }
                    .fold(
                        onSuccess = {
                            UserStub.users = it
                            emit(ContactListEvent.Internal.SuccessLoadedContacts(it))
                        },
                        onFailure = {
                            emit(ContactListEvent.Internal.ErrorLoadedContacts)
                        }
                    )
            }

            is ContactListCommand.SearchByName -> {
                runCatchingNonCancellation {
                    searchContactsByNameUseCase.invoke(command.name)
                }
                    .fold(
                        onSuccess = {
                            emit(ContactListEvent.Internal.SuccessLoadedContacts(it))
                        },
                        onFailure = {
                            emit(ContactListEvent.Internal.ErrorLoadedContacts)
                        }
                    )
            }
        }
    }

}