package ru.gozerov.tfs_spring.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _viewState = MutableStateFlow<ProfileViewState>(ProfileViewState.Empty)
    val viewState: StateFlow<ProfileViewState> get() = _viewState.asStateFlow()

    fun handleIntent(intent: ProfileIntent) {
        viewModelScope.launch {
            when (intent) {
                is ProfileIntent.LoadProfile -> _viewState.emit(
                    ProfileViewState.LoadedProfile(
                        imageUrl = "https://s3-alpha-sig.figma.com/img/58c8/ebf7/591d3ed13bff1e0b58fe824ee0565a0f?Expires=1712534400&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=E~6jt72KcjhAJrUtetPbPQ51IzJWNsb7ACRRjbDj7C9-dP1g2TsnODeYghRKdbX2yrcp0ext3cZN9ceqZV76xq8TGv1vhrFsoG4TEa1Jvefpr0ZH2VECIQlLy7D04EWDidtSNHqEDkg1ZvJaA-8pckUWhmf5tfVRZzmXTa3HfbtOqrIeMNInQ1q~bP6Jdx7kzYfbKi4QZf1fJadOFh84j8QMYkK1xqhs7BoGnwiBUximarnEYFgLdojAoLq93wi5Aia9eFKPJDUPcb2ylH~DMQ9IkipKibbc8oXZ~lwrM8Bb~FU8X4vGjf-etNuM9ZXx5LzQ2MSy6LR2eQEo3AE~KA__",
                        username = "Darrell Steward",
                        status = "In a meeting",
                        isOnline = true
                    )
                )
            }
        }
    }

}