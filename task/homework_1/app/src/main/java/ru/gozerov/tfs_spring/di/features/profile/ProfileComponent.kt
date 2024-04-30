package ru.gozerov.tfs_spring.di.features.profile

import androidx.lifecycle.Lifecycle
import dagger.BindsInstance
import dagger.Component
import ru.gozerov.tfs_spring.di.application.AppComponent
import ru.gozerov.tfs_spring.di.features.FragmentScope
import ru.gozerov.tfs_spring.presentation.screens.profile.ProfileFragment

@FragmentScope
@Component(
    dependencies = [AppComponent::class],
    modules = [ProfileStoreModule::class]
)
interface ProfileComponent {

    fun inject(profileFragment: ProfileFragment)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance lifecycle: Lifecycle,
            appComponent: AppComponent
        ): ProfileComponent

    }

}