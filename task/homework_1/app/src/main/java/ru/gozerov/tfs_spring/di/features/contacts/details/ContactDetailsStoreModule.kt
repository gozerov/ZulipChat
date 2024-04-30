package ru.gozerov.tfs_spring.di.features.contacts.details

import androidx.lifecycle.Lifecycle
import dagger.Module
import dagger.Provides
import ru.gozerov.tfs_spring.di.features.FragmentScope
import ru.gozerov.tfs_spring.presentation.screens.contacts.details.elm.ContactDetailsActor
import ru.gozerov.tfs_spring.presentation.screens.contacts.details.elm.ContactDetailsReducer
import ru.gozerov.tfs_spring.presentation.screens.contacts.details.elm.models.ContactDetailsEffect
import ru.gozerov.tfs_spring.presentation.screens.contacts.details.elm.models.ContactDetailsEvent
import ru.gozerov.tfs_spring.presentation.screens.contacts.details.elm.models.ContactDetailsState
import vivid.money.elmslie.android.storeholder.LifecycleAwareStoreHolder
import vivid.money.elmslie.android.storeholder.StoreHolder
import vivid.money.elmslie.coroutines.ElmStoreCompat

@Module
interface ContactDetailsStoreModule {

    companion object {

        @FragmentScope
        @Provides
        fun provideContactDetailsStore(
            lifecycle: Lifecycle,
            contactDetailsActor: ContactDetailsActor
        ): StoreHolder<ContactDetailsEvent, ContactDetailsEffect, ContactDetailsState> =
            LifecycleAwareStoreHolder(lifecycle) {
                ElmStoreCompat(
                    initialState = ContactDetailsState(),
                    reducer = ContactDetailsReducer(),
                    actor = contactDetailsActor
                )
            }

    }

}