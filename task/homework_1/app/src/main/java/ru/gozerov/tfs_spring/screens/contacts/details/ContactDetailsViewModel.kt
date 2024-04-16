package ru.gozerov.tfs_spring.screens.contacts.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.gozerov.tfs_spring.api.ZulipApi
import ru.gozerov.tfs_spring.screens.contacts.details.models.ContactDetailsIntent
import ru.gozerov.tfs_spring.screens.contacts.details.models.ContactDetailsViewState
import ru.gozerov.tfs_spring.use_cases.GetContactById

class ContactDetailsViewModel(
    private val zulipApi: ZulipApi
) : ViewModel() {

    private val _viewState =
        MutableStateFlow<ContactDetailsViewState>(ContactDetailsViewState.Empty)
    val viewState: StateFlow<ContactDetailsViewState> get() = _viewState.asStateFlow()

    fun handleIntent(intent: ContactDetailsIntent) {
        viewModelScope.launch {
            when (intent) {
                is ContactDetailsIntent.LoadContact -> {
                    _viewState.emit(
                        ContactDetailsViewState.LoadedContact(
                            GetContactById(zulipApi).invoke(intent.id)
                        )
                    )
                }
            }
        }
    }


}