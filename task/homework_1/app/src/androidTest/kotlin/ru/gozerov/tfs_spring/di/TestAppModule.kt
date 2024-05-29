package ru.gozerov.tfs_spring.di

import dagger.Module
import ru.gozerov.tfs_spring.di.application.AppBindModule
import ru.gozerov.tfs_spring.di.application.OkHttpModule
import ru.gozerov.tfs_spring.di.application.RoomModule
import ru.gozerov.tfs_spring.di.application.StorageModule

@Module(includes = [TestRemoteModule::class, AppBindModule::class, RoomModule::class, OkHttpModule::class, StorageModule::class])
interface TestAppModule