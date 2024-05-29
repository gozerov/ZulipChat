package ru.gozerov.tfs_spring.di.application

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import ru.gozerov.tfs_spring.data.cache.storage.AppStorageImpl
import javax.inject.Singleton

@Module
interface StorageModule {

    companion object {

        @Singleton
        @Provides
        fun provideSharedPrefs(context: Context): SharedPreferences {
            return context.applicationContext.getSharedPreferences(
                AppStorageImpl.APP_SHARED_PREFS,
                Context.MODE_PRIVATE
            )
        }

    }

}