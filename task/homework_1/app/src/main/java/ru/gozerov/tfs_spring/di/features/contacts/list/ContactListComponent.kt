package ru.gozerov.tfs_spring.di.features.contacts.list

import androidx.lifecycle.Lifecycle
import dagger.BindsInstance
import dagger.Component
import ru.gozerov.tfs_spring.di.application.AppComponent
import ru.gozerov.tfs_spring.di.features.FragmentScope
import ru.gozerov.tfs_spring.presentation.screens.contacts.list.ContactsFragment

@FragmentScope
@Component(
    dependencies = [AppComponent::class],
    modules = [ContactListStoreModule::class]
)
interface ContactListComponent {

    fun inject(contactsFragment: ContactsFragment)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance lifecycle: Lifecycle,
            appComponent: AppComponent
        ): ContactListComponent

    }

}