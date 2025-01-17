package ru.gozerov.tfs_spring.di.application

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.gozerov.tfs_spring.app.TFSApp
import ru.gozerov.tfs_spring.data.cache.AppDatabase
import ru.gozerov.tfs_spring.data.cache.dao.MessageDao
import ru.gozerov.tfs_spring.data.cache.storage.AppStorage
import ru.gozerov.tfs_spring.data.remote.api.ZulipApi
import ru.gozerov.tfs_spring.data.remote.api.ZulipLongPollingApi
import ru.gozerov.tfs_spring.domain.repositories.UsersRepository
import ru.gozerov.tfs_spring.domain.repositories.ZulipRepository
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    val zulipRepository: ZulipRepository
    val usersRepository: UsersRepository
    val zulipApi: ZulipApi
    val zulipLongPollingApi: ZulipLongPollingApi
    val appStorage: AppStorage

    val appDatabase: AppDatabase
    val messageDao: MessageDao

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

}

val Context.appComponent
    get() = (applicationContext as TFSApp).applicationComponent