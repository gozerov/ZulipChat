package ru.gozerov.tfs_spring.di.application

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.gozerov.tfs_spring.app.BasicAuthData
import ru.gozerov.tfs_spring.data.api.ZulipApi
import ru.gozerov.tfs_spring.data.api.ZulipLongPollingApi
import ru.gozerov.tfs_spring.di.application.ApiConstants.BASE_URL
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
interface RetrofitModule {

    companion object {

        @Singleton
        @Provides
        fun provideZulipApi(): ZulipApi = Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient.Builder().addInterceptor(
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                )
                    .addInterceptor {
                        val request = it.request().newBuilder().addHeader(
                            "Authorization",
                            Credentials.basic(BasicAuthData.username, BasicAuthData.password)
                        ).build()
                        it.proceed(request)
                    }
                    .build()
            )
            .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
            .build()
            .create(ZulipApi::class.java)

        @Singleton
        @Provides
        fun provideZulipLongPollingApi(): ZulipLongPollingApi = Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient.Builder().addInterceptor(
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                )
                    .addInterceptor {
                        val request = it.request().newBuilder().addHeader(
                            "Authorization",
                            Credentials.basic(BasicAuthData.username, BasicAuthData.password)
                        ).build()
                        it.proceed(request)
                    }
                    .readTimeout(Int.MAX_VALUE.toLong(), TimeUnit.MILLISECONDS)
                    .build()
            )
            .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
            .build()
            .create(ZulipLongPollingApi::class.java)

    }

}