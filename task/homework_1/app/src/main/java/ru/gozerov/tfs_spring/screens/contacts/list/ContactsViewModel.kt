package ru.gozerov.tfs_spring.screens.contacts.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.gozerov.tfs_spring.api.ZulipApi
import ru.gozerov.tfs_spring.screens.contacts.list.models.ContactsIntent
import ru.gozerov.tfs_spring.screens.contacts.list.models.ContactsViewState
import ru.gozerov.tfs_spring.use_cases.GetUsersUseCase

class ContactsViewModel(
    private val zulipApi: ZulipApi
) : ViewModel() {

    private val _viewState = MutableStateFlow<ContactsViewState>(ContactsViewState.Empty)
    val viewState: StateFlow<ContactsViewState> get() = _viewState.asStateFlow()

    fun handleIntent(intent: ContactsIntent) {
        viewModelScope.launch {
            when (intent) {
                is ContactsIntent.LoadContacts -> _viewState.emit(
                    ContactsViewState.ContactsList(GetUsersUseCase(zulipApi).invoke())
                )
            }
        }
    }

}