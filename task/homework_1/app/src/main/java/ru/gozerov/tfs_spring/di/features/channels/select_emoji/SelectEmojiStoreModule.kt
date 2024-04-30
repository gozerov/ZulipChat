package ru.gozerov.tfs_spring.di.features.channels.select_emoji

import androidx.lifecycle.Lifecycle
import dagger.Module
import dagger.Provides
import ru.gozerov.tfs_spring.di.features.FragmentScope
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.dialog.elm.SelectEmojiActor
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.dialog.elm.SelectEmojiReducer
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.dialog.elm.models.SelectEmojiEffect
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.dialog.elm.models.SelectEmojiEvent
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.dialog.elm.models.SelectEmojiState
import vivid.money.elmslie.android.storeholder.LifecycleAwareStoreHolder
import vivid.money.elmslie.android.storeholder.StoreHolder
import vivid.money.elmslie.coroutines.ElmStoreCompat

@Module
interface SelectEmojiStoreModule {

    companion object {

        @FragmentScope
        @Provides
        fun provideSelectEmojiStore(
            lifecycle: Lifecycle,
            selectEmojiActor: SelectEmojiActor
        ): StoreHolder<SelectEmojiEvent, SelectEmojiEffect, SelectEmojiState> = LifecycleAwareStoreHolder(lifecycle) {
            ElmStoreCompat(
                initialState = SelectEmojiState(),
                reducer = SelectEmojiReducer(),
                actor = selectEmojiActor
            )
        }

    }

}