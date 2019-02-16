package dev.jriley.auth

import dev.jriley.login.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object TokenRepositoryFactory {
    val tokenRepository: TokenRepo by lazy { TokenRepository() }
}

object AuthTokenApiFactory {
    private val OK_HTTP_INSTANCE: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    val authTokenApi: AuthTokenApi by lazy {
        Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
            .client(OK_HTTP_INSTANCE)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(AuthTokenApi::class.java)
    }
}