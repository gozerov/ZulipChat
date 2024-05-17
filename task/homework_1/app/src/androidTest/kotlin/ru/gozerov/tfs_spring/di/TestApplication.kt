package ru.gozerov.tfs_spring.di

import ru.gozerov.tfs_spring.app.TFSApp

class TestApplication : TFSApp() {

    override val applicationComponent: TestAppComponent by lazy {
        DaggerTestAppComponent.factory().create(applicationContext)
    }

}