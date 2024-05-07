package ru.gozerov.tfs_spring.di.application

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.gozerov.tfs_spring.data.cache.AppDatabase
import javax.inject.Singleton

@Module
class RoomModule {

    @Singleton
    @Provides
    fun provideMessageDao(context: Context) = AppDatabase.getInstance(context).getMessageDao()

    @Singleton
    @Provides
    fun provideMessageReactionDao(context: Context) =
        AppDatabase.getInstance(context).getMessageReactionDao()

    @Singleton
    @Provides
    fun provideReactionDao(context: Context) = AppDatabase.getInstance(context).getReactionDao()

    @Singleton
    @Provides
    fun provideStreamDao(context: Context) = AppDatabase.getInstance(context).getStreamDao()

    @Singleton
    @Provides
    fun provideTopicDao(context: Context) = AppDatabase.getInstance(context).getTopicDao()

}