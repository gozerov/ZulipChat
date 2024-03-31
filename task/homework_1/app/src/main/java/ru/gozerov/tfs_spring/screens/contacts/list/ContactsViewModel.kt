package ru.gozerov.tfs_spring.screens.contacts.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.gozerov.tfs_spring.screens.contacts.list.models.ContactsIntent
import ru.gozerov.tfs_spring.screens.contacts.list.models.ContactsViewState
import ru.gozerov.tfs_spring.screens.contacts.list.models.UserContact
import kotlin.random.Random

class ContactsViewModel : ViewModel() {

    private val _viewState = MutableStateFlow<ContactsViewState>(ContactsViewState.Empty)
    val viewState: StateFlow<ContactsViewState> get() = _viewState.asStateFlow()

    fun handleIntent(intent: ContactsIntent) {
        viewModelScope.launch {
            when (intent) {
                is ContactsIntent.LoadContacts -> _viewState.emit(
                    ContactsViewState.ContactsList(
                        contacts = listOf(
                            UserContact(
                                Random.nextInt(),
                                "https://s3-alpha-sig.figma.com/img/58c8/ebf7/591d3ed13bff1e0b58fe824ee0565a0f?Expires=1712534400&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=E~6jt72KcjhAJrUtetPbPQ51IzJWNsb7ACRRjbDj7C9-dP1g2TsnODeYghRKdbX2yrcp0ext3cZN9ceqZV76xq8TGv1vhrFsoG4TEa1Jvefpr0ZH2VECIQlLy7D04EWDidtSNHqEDkg1ZvJaA-8pckUWhmf5tfVRZzmXTa3HfbtOqrIeMNInQ1q~bP6Jdx7kzYfbKi4QZf1fJadOFh84j8QMYkK1xqhs7BoGnwiBUximarnEYFgLdojAoLq93wi5Aia9eFKPJDUPcb2ylH~DMQ9IkipKibbc8oXZ~lwrM8Bb~FU8X4vGjf-etNuM9ZXx5LzQ2MSy6LR2eQEo3AE~KA__",
                                "Darrell Steward",
                                "darrel@company.com",
                                Random.nextBoolean()
                            ),
                            UserContact(
                                Random.nextInt(),
                                "https://s3-alpha-sig.figma.com/img/58c8/ebf7/591d3ed13bff1e0b58fe824ee0565a0f?Expires=1712534400&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=E~6jt72KcjhAJrUtetPbPQ51IzJWNsb7ACRRjbDj7C9-dP1g2TsnODeYghRKdbX2yrcp0ext3cZN9ceqZV76xq8TGv1vhrFsoG4TEa1Jvefpr0ZH2VECIQlLy7D04EWDidtSNHqEDkg1ZvJaA-8pckUWhmf5tfVRZzmXTa3HfbtOqrIeMNInQ1q~bP6Jdx7kzYfbKi4QZf1fJadOFh84j8QMYkK1xqhs7BoGnwiBUximarnEYFgLdojAoLq93wi5Aia9eFKPJDUPcb2ylH~DMQ9IkipKibbc8oXZ~lwrM8Bb~FU8X4vGjf-etNuM9ZXx5LzQ2MSy6LR2eQEo3AE~KA__",
                                "Darrell Steward",
                                "darrel@company.com",
                                Random.nextBoolean()
                            ),
                            UserContact(
                                Random.nextInt(),
                                "https://s3-alpha-sig.figma.com/img/58c8/ebf7/591d3ed13bff1e0b58fe824ee0565a0f?Expires=1712534400&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=E~6jt72KcjhAJrUtetPbPQ51IzJWNsb7ACRRjbDj7C9-dP1g2TsnODeYghRKdbX2yrcp0ext3cZN9ceqZV76xq8TGv1vhrFsoG4TEa1Jvefpr0ZH2VECIQlLy7D04EWDidtSNHqEDkg1ZvJaA-8pckUWhmf5tfVRZzmXTa3HfbtOqrIeMNInQ1q~bP6Jdx7kzYfbKi4QZf1fJadOFh84j8QMYkK1xqhs7BoGnwiBUximarnEYFgLdojAoLq93wi5Aia9eFKPJDUPcb2ylH~DMQ9IkipKibbc8oXZ~lwrM8Bb~FU8X4vGjf-etNuM9ZXx5LzQ2MSy6LR2eQEo3AE~KA__",
                                "Darrell Steward",
                                "darrel@company.com",
                                Random.nextBoolean()
                            ),
                            UserContact(
                                Random.nextInt(),
                                "https://s3-alpha-sig.figma.com/img/58c8/ebf7/591d3ed13bff1e0b58fe824ee0565a0f?Expires=1712534400&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=E~6jt72KcjhAJrUtetPbPQ51IzJWNsb7ACRRjbDj7C9-dP1g2TsnODeYghRKdbX2yrcp0ext3cZN9ceqZV76xq8TGv1vhrFsoG4TEa1Jvefpr0ZH2VECIQlLy7D04EWDidtSNHqEDkg1ZvJaA-8pckUWhmf5tfVRZzmXTa3HfbtOqrIeMNInQ1q~bP6Jdx7kzYfbKi4QZf1fJadOFh84j8QMYkK1xqhs7BoGnwiBUximarnEYFgLdojAoLq93wi5Aia9eFKPJDUPcb2ylH~DMQ9IkipKibbc8oXZ~lwrM8Bb~FU8X4vGjf-etNuM9ZXx5LzQ2MSy6LR2eQEo3AE~KA__",
                                "Darrell Steward",
                                "darrel@company.com",
                                Random.nextBoolean()
                            ),
                            UserContact(
                                Random.nextInt(),
                                "https://s3-alpha-sig.figma.com/img/58c8/ebf7/591d3ed13bff1e0b58fe824ee0565a0f?Expires=1712534400&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=E~6jt72KcjhAJrUtetPbPQ51IzJWNsb7ACRRjbDj7C9-dP1g2TsnODeYghRKdbX2yrcp0ext3cZN9ceqZV76xq8TGv1vhrFsoG4TEa1Jvefpr0ZH2VECIQlLy7D04EWDidtSNHqEDkg1ZvJaA-8pckUWhmf5tfVRZzmXTa3HfbtOqrIeMNInQ1q~bP6Jdx7kzYfbKi4QZf1fJadOFh84j8QMYkK1xqhs7BoGnwiBUximarnEYFgLdojAoLq93wi5Aia9eFKPJDUPcb2ylH~DMQ9IkipKibbc8oXZ~lwrM8Bb~FU8X4vGjf-etNuM9ZXx5LzQ2MSy6LR2eQEo3AE~KA__",
                                "Darrell Steward",
                                "darrel@company.com",
                                Random.nextBoolean()
                            )
                        )
                    )
                )
            }
        }
    }

}