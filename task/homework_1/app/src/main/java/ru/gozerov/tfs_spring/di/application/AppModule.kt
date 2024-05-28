package ru.gozerov.tfs_spring.di.application

import dagger.Module

@Module(includes = [RetrofitModule::class, AppBindModule::class, RoomModule::class, OkHttpModule::class])
interface AppModule