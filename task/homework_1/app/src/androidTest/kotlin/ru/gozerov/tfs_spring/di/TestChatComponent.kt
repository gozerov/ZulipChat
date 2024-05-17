package ru.gozerov.tfs_spring.di

import androidx.lifecycle.Lifecycle
import dagger.BindsInstance
import dagger.Component
import ru.gozerov.tfs_spring.di.features.FragmentScope
import ru.gozerov.tfs_spring.di.features.channels.chat.ChatStoreModule
import ru.gozerov.tfs_spring.presentation.screens.channel.chat.ChatFragmentTest
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.ChatFragment

@FragmentScope
@Component(
    dependencies = [TestAppComponent::class],
    modules = [ChatStoreModule::class]
)
interface TestChatComponent {

    fun inject(chatFragmentTest: ChatFragmentTest)
    fun inject(chatFragment: ChatFragment)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance lifecycle: Lifecycle,
            testAppComponent: TestAppComponent
        ): TestChatComponent

    }

}