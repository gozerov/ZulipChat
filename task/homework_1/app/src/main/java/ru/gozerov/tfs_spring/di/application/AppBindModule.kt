package ru.gozerov.tfs_spring.di.application

import dagger.Binds
import dagger.Module
import ru.gozerov.tfs_spring.data.cache.storage.AppStorage
import ru.gozerov.tfs_spring.data.cache.storage.AppStorageImpl
import ru.gozerov.tfs_spring.data.repositories.UsersRepositoryImpl
import ru.gozerov.tfs_spring.data.repositories.ZulipRepositoryImpl
import ru.gozerov.tfs_spring.domain.repositories.UsersRepository
import ru.gozerov.tfs_spring.domain.repositories.ZulipRepository
import javax.inject.Singleton

@Module
interface AppBindModule {

    @Binds
    @Singleton
    fun bindZulipRepoImpl(zulipRepositoryImpl: ZulipRepositoryImpl): ZulipRepository

    @Binds
    @Singleton
    fun bindUsersRepoImpl(usersRepositoryImpl: UsersRepositoryImpl): UsersRepository

    @Binds
    @Singleton
    fun bindAppStorageImpl(appStorageImpl: AppStorageImpl): AppStorage

}