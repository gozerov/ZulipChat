package ru.gozerov.tfs_spring.di.application

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.gozerov.tfs_spring.data.remote.api.ZulipApi
import ru.gozerov.tfs_spring.data.remote.api.ZulipLongPollingApi
import ru.gozerov.tfs_spring.di.application.ApiConstants.BASE_URL
import javax.inject.Singleton

@Module
interface RetrofitModule {

    companion object {

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