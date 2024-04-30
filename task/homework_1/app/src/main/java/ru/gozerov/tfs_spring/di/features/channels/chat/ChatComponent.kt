package ru.gozerov.tfs_spring.di.features.channels.chat

import androidx.lifecycle.Lifecycle
import dagger.BindsInstance
import dagger.Component
import ru.gozerov.tfs_spring.di.application.AppComponent
import ru.gozerov.tfs_spring.di.features.FragmentScope
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.ChatFragment

@FragmentScope
@Component(
    dependencies = [AppComponent::class],
    modules = [ChatStoreModule::class]
)
interface ChatComponent {

    fun inject(chatFragment: ChatFragment)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance lifecycle: Lifecycle,
            appComponent: AppComponent
        ): ChatComponent

    }

}