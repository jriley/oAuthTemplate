package dev.jriley.login

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object TokenRepositoryFactory {
    var tokenRepository: TokenRepo = TokenRepository()
}

object AuthTokenApiFactory {
    private val OK_HTTP_INSTANCE: OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

    var authTokenApi: AuthTokenApi = Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
        .client(OK_HTTP_INSTANCE)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(AuthTokenApi::class.java)
}