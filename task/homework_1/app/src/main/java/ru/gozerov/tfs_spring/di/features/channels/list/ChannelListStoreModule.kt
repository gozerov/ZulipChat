package ru.gozerov.tfs_spring.di.features.channels.list

import androidx.lifecycle.Lifecycle
import dagger.Module
import dagger.Provides
import ru.gozerov.tfs_spring.di.features.FragmentScope
import ru.gozerov.tfs_spring.presentation.screens.channels.list.elm.ChannelListActor
import ru.gozerov.tfs_spring.presentation.screens.channels.list.elm.ChannelListReducer
import ru.gozerov.tfs_spring.presentation.screens.channels.list.elm.models.ChannelListEffect
import ru.gozerov.tfs_spring.presentation.screens.channels.list.elm.models.ChannelListEvent
import ru.gozerov.tfs_spring.presentation.screens.channels.list.elm.models.ChannelListState
import vivid.money.elmslie.android.storeholder.LifecycleAwareStoreHolder
import vivid.money.elmslie.android.storeholder.StoreHolder
import vivid.money.elmslie.coroutines.ElmStoreCompat

@Module
interface ChannelListStoreModule {

    companion object {

        @FragmentScope
        @Provides
        fun provideChannelListStore(
            lifecycle: Lifecycle,
            actor: ChannelListActor,
            reducer: ChannelListReducer

        ): StoreHolder<ChannelListEvent, ChannelListEffect, ChannelListState> =
            LifecycleAwareStoreHolder(lifecycle) {
                ElmStoreCompat(
                    initialState = ChannelListState(),
                    reducer = reducer,
                    actor = actor
                )
            }

    }

}