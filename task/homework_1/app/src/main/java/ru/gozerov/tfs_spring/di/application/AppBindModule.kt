package ru.gozerov.tfs_spring.di.application

import dagger.Binds
import dagger.Module
import ru.gozerov.tfs_spring.data.repositories.ZulipRepositoryImpl
import ru.gozerov.tfs_spring.domain.repositories.ZulipRepository
import javax.inject.Singleton

@Module
interface AppBindModule {

    @Binds
    @Singleton
    fun bindZulipRepoImpl(zulipRepositoryImpl: ZulipRepositoryImpl): ZulipRepository

}