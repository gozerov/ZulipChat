package ru.gozerov.tfs_spring.di.features.channels.select_emoji

import androidx.lifecycle.Lifecycle
import dagger.BindsInstance
import dagger.Component
import ru.gozerov.tfs_spring.di.application.AppComponent
import ru.gozerov.tfs_spring.di.features.FragmentScope
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.dialog.SelectEmojiFragment

@FragmentScope
@Component(
    dependencies = [AppComponent::class],
    modules = [SelectEmojiStoreModule::class]
)
interface SelectEmojiComponent {

    fun inject(selectEmojiFragment: SelectEmojiFragment)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance lifecycle: Lifecycle,
            appComponent: AppComponent
        ): SelectEmojiComponent

    }

}