package ru.gozerov.tfs_spring.app

import android.app.Application
import ru.gozerov.tfs_spring.di.application.AppComponent
import ru.gozerov.tfs_spring.di.application.DaggerAppComponent

open class TFSApp : Application() {

    val applicationComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }

}