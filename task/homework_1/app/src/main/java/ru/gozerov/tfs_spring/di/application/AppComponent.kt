package ru.gozerov.tfs_spring.di.application

import android.content.Context
import dagger.Component
import ru.gozerov.tfs_spring.app.TFSApp
import ru.gozerov.tfs_spring.data.api.ZulipApi
import ru.gozerov.tfs_spring.data.api.ZulipLongPollingApi
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    val zulipApi: ZulipApi
    val zulipLongPollingApi: ZulipLongPollingApi

}

val Context.appComponent
    get() = (applicationContext as TFSApp).applicationComponent