package ru.gozerov.tfs_spring.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.gozerov.tfs_spring.data.cache.AppDatabase
import ru.gozerov.tfs_spring.data.cache.dao.MessageDao
import ru.gozerov.tfs_spring.data.cache.storage.AppStorage
import ru.gozerov.tfs_spring.data.remote.api.ZulipApi
import ru.gozerov.tfs_spring.data.remote.api.ZulipLongPollingApi
import ru.gozerov.tfs_spring.di.application.AppComponent
import ru.gozerov.tfs_spring.domain.repositories.ZulipRepository
import javax.inject.Singleton

@Singleton
@Component(modules = [TestAppModule::class])
interface TestAppComponent: AppComponent {

   override val zulipRepository: ZulipRepository
   override val zulipApi: ZulipApi
   override val zulipLongPollingApi: ZulipLongPollingApi
   override val appStorage: AppStorage

   override val appDatabase: AppDatabase
   override val messageDao: MessageDao

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): TestAppComponent
    }

}