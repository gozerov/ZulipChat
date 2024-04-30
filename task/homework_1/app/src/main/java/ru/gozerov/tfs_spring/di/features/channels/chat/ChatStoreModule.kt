package ru.gozerov.tfs_spring.di.features.channels.chat

import androidx.lifecycle.Lifecycle
import dagger.Module
import dagger.Provides
import ru.gozerov.tfs_spring.di.features.FragmentScope
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.elm.ChatActor
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.elm.ChatReducer
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.elm.models.ChatEffect
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.elm.models.ChatEvent
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.elm.models.ChatState
import vivid.money.elmslie.android.storeholder.LifecycleAwareStoreHolder
import vivid.money.elmslie.android.storeholder.StoreHolder
import vivid.money.elmslie.coroutines.ElmStoreCompat

@Module
interface ChatStoreModule {

    companion object {

        @FragmentScope
        @Provides
        fun provideChatStore(
            lifecycle: Lifecycle,
            chatActor: ChatActor
        ): StoreHolder<ChatEvent, ChatEffect, ChatState> = LifecycleAwareStoreHolder(lifecycle) {
            ElmStoreCompat(
                initialState = ChatState(),
                reducer = ChatReducer(),
                actor = chatActor
            )
        }
    }

}