package ru.gozerov.tfs_spring.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.gozerov.tfs_spring.api.ZulipApi
import ru.gozerov.tfs_spring.use_cases.GetOwnUserUseCase

class ProfileViewModel(
    private val zulipApi: ZulipApi
) : ViewModel() {

    private val _viewState = MutableStateFlow<ProfileViewState>(ProfileViewState.Empty)
    val viewState: StateFlow<ProfileViewState> get() = _viewState.asStateFlow()

    fun handleIntent(intent: ProfileIntent) {
        viewModelScope.launch {
            when (intent) {
                is ProfileIntent.LoadProfile -> _viewState.emit(
                    ProfileViewState.LoadedProfile(
                        GetOwnUserUseCase(zulipApi).invoke()
                    )
                )
            }
        }
    }

}