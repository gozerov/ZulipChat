package ru.gozerov.tfs_spring.di.application

import dagger.Module
import dagger.Provides
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ru.gozerov.tfs_spring.app.BasicAuthData
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
annotation class DefaultOkHttpClient

@Qualifier
annotation class LongReadOkHttpClient

@Module
interface OkHttpModule {

    companion object {

        @DefaultOkHttpClient
        @Singleton
        @Provides
        fun provideDefaultOkHttp(): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .addInterceptor { chain ->
                val request = chain.request().newBuilder().addHeader(
                    "Authorization",
                    Credentials.basic(BasicAuthData.username, BasicAuthData.password)
                ).build()
                chain.proceed(request)
            }
            .build()

        @LongReadOkHttpClient
        @Singleton
        @Provides
        fun provideLongReadOkHttp(): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .addInterceptor { chain ->
                val request = chain.request().newBuilder().addHeader(
                    "Authorization",
                    Credentials.basic(BasicAuthData.username, BasicAuthData.password)
                ).build()
                chain.proceed(request)
            }
            .readTimeout(3, TimeUnit.MINUTES)
            .build()

    }


}