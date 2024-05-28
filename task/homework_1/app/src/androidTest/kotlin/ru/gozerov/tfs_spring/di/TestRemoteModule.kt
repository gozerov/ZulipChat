package ru.gozerov.tfs_spring.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.gozerov.tfs_spring.data.remote.api.ZulipApi
import ru.gozerov.tfs_spring.data.remote.api.ZulipLongPollingApi
import ru.gozerov.tfs_spring.di.application.ApiConstants
import ru.gozerov.tfs_spring.di.application.DefaultOkHttpClient
import ru.gozerov.tfs_spring.di.application.LongReadOkHttpClient
import javax.inject.Singleton

@Module
class TestRemoteModule {

    companion object {

        private val BASE_URL = "http://localhost:8080"

        @Singleton
        @Provides
        fun provideZulipApi(@DefaultOkHttpClient client: OkHttpClient): ZulipApi = Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
            .build()
            .create(ZulipApi::class.java)

        @Singleton
        @Provides
        fun provideZulipLongPollingApi(@LongReadOkHttpClient client: OkHttpClient): ZulipLongPollingApi =
            Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
                .build()
                .create(ZulipLongPollingApi::class.java)

    }

}