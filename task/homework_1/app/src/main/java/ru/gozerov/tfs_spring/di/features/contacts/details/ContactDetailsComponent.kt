package ru.gozerov.tfs_spring.di.features.contacts.details

import androidx.lifecycle.Lifecycle
import dagger.BindsInstance
import dagger.Component
import ru.gozerov.tfs_spring.di.application.AppComponent
import ru.gozerov.tfs_spring.di.features.FragmentScope
import ru.gozerov.tfs_spring.presentation.screens.contacts.details.ContactDetailsFragment

@FragmentScope
@Component(
    dependencies = [AppComponent::class],
    modules = [ContactDetailsStoreModule::class]
)
interface ContactDetailsComponent {

    fun inject(contactDetailsFragment: ContactDetailsFragment)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance lifecycle: Lifecycle,
            appComponent: AppComponent
        ): ContactDetailsComponent

    }

}