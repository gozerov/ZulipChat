package ru.gozerov.tfs_spring.di.features.profile

import androidx.lifecycle.Lifecycle
import dagger.Module
import dagger.Provides
import ru.gozerov.tfs_spring.di.features.FragmentScope
import ru.gozerov.tfs_spring.presentation.screens.profile.elm.ProfileActor
import ru.gozerov.tfs_spring.presentation.screens.profile.elm.ProfileReducer
import ru.gozerov.tfs_spring.presentation.screens.profile.elm.models.ProfileEffect
import ru.gozerov.tfs_spring.presentation.screens.profile.elm.models.ProfileEvent
import ru.gozerov.tfs_spring.presentation.screens.profile.elm.models.ProfileState
import vivid.money.elmslie.android.storeholder.LifecycleAwareStoreHolder
import vivid.money.elmslie.android.storeholder.StoreHolder
import vivid.money.elmslie.coroutines.ElmStoreCompat

@Module
interface ProfileStoreModule {

    companion object {
        @FragmentScope
        @Provides
        fun provideProfileStore(
            lifecycle: Lifecycle,
            profileActor: ProfileActor,
            profileReducer: ProfileReducer
        ): StoreHolder<ProfileEvent, ProfileEffect, ProfileState> =
            LifecycleAwareStoreHolder(lifecycle) {
                ElmStoreCompat(
                    initialState = ProfileState(),
                    reducer = profileReducer,
                    actor = profileActor
                )
            }
    }

}