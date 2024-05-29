package ru.gozerov.tfs_spring.di.application

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.gozerov.tfs_spring.data.cache.AppDatabase
import javax.inject.Singleton

@Module
interface RoomModule {

    companion object {

        @Singleton
        @Provides
        fun provideMessageDao(context: Context) = AppDatabase.getInstance(context).getMessageDao()

        @Singleton
        @Provides
        fun provideStreamDao(context: Context) = AppDatabase.getInstance(context).getStreamDao()

        @Singleton
        @Provides
        fun provideTopicDao(context: Context) = AppDatabase.getInstance(context).getTopicDao()

        @Singleton
        @Provides
        fun provideUsersDao(context: Context) = AppDatabase.getInstance(context).getUsersDao()

        @Singleton
        @Provides
        fun provideAppDatabase(context: Context) = AppDatabase.getInstance(context)

    }

}