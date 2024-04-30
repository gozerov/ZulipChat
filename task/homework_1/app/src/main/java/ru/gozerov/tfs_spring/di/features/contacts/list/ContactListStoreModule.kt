package ru.gozerov.tfs_spring.di.features.contacts.list

import androidx.lifecycle.Lifecycle
import dagger.Module
import dagger.Provides
import ru.gozerov.tfs_spring.di.features.FragmentScope
import ru.gozerov.tfs_spring.presentation.screens.contacts.list.elm.ContactListActor
import ru.gozerov.tfs_spring.presentation.screens.contacts.list.elm.ContactListReducer
import ru.gozerov.tfs_spring.presentation.screens.contacts.list.elm.models.ContactListEffect
import ru.gozerov.tfs_spring.presentation.screens.contacts.list.elm.models.ContactListEvent
import ru.gozerov.tfs_spring.presentation.screens.contacts.list.elm.models.ContactListState
import vivid.money.elmslie.android.storeholder.LifecycleAwareStoreHolder
import vivid.money.elmslie.android.storeholder.StoreHolder
import vivid.money.elmslie.coroutines.ElmStoreCompat

@Module
interface ContactListStoreModule {

    companion object {

        @FragmentScope
        @Provides
        fun provideContactListStore(
            lifecycle: Lifecycle,
            contactListActor: ContactListActor
        ): StoreHolder<ContactListEvent, ContactListEffect, ContactListState> =
            LifecycleAwareStoreHolder(lifecycle) {
                ElmStoreCompat(
                    initialState = ContactListState(),
                    reducer = ContactListReducer(),
                    actor = contactListActor
                )
            }

    }

}