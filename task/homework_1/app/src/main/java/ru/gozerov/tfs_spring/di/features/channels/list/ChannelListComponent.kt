package ru.gozerov.tfs_spring.di.features.channels.list

import androidx.lifecycle.Lifecycle
import dagger.BindsInstance
import dagger.Component
import ru.gozerov.tfs_spring.di.application.AppComponent
import ru.gozerov.tfs_spring.di.features.FragmentScope
import ru.gozerov.tfs_spring.presentation.screens.channels.list.ChannelListFragment

@FragmentScope
@Component(
    dependencies = [AppComponent::class],
    modules = [ChannelListStoreModule::class]
)
interface ChannelListComponent {

    fun inject(channelListFragment: ChannelListFragment)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance lifecycle: Lifecycle,
            appComponent: AppComponent
        ): ChannelListComponent

    }

}