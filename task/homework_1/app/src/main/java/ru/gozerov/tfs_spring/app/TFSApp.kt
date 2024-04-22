package ru.gozerov.tfs_spring.app

import android.app.Application
import com.squareup.moshi.Moshi
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.gozerov.tfs_spring.data.api.ZulipApi
import java.util.concurrent.TimeUnit

class TFSApp : Application() {

    val zulipApi = Retrofit
        .Builder()
        .baseUrl("https://tinkoff-android-spring-2024.zulipchat.com/api/v1/")
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

    val zulipLongPollingApi = Retrofit
        .Builder()
        .baseUrl("https://tinkoff-android-spring-2024.zulipchat.com/api/v1/")
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
        .create(ru.gozerov.tfs_spring.data.api.ZulipLongPollingApi::class.java)

}