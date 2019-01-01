package dev.jriley.login

import android.support.annotation.Keep
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

class TokenRepository(
    private val authTokenApi: AuthTokenApi = AuthTokenApiFactory.tokenApi,
    private val ioScheduler: Scheduler = Schedulers.io()) : TokenRepo {

    override fun isValid(): Single<Boolean> = Single.just(false)

    override fun logInAttempt(loginCredentials: LoginCredentials): Completable =
        authTokenApi.login(loginCredentials.userName, loginCredentials.password)
            .subscribeOn(ioScheduler)
            .flatMapCompletable { _ -> Completable.complete() }
}

interface TokenRepo {
    fun isValid(): Single<Boolean>
    fun logInAttempt(loginCredentials: LoginCredentials): Completable
}


object AuthTokenApiFactory {
    val tokenApi: AuthTokenApi  by lazy {
        object : AuthTokenApi {
            override fun login(userName: String, password: String, grantType: String, clientId: String, clientSecret: String): Single<AuthToken> = when {
                userName == "joe@example.com" && password == "foobarbaz" -> Single.just(AuthToken("token-123456", "refresh-123456"))
                else -> Single.error { InvalidGrantException() }
            }
        }
    }
}

interface AuthTokenApi {

    @FormUrlEncoded
    @POST("tokens")
    fun login(
        @Field("userName") userName: String,
        @Field("password") password: String,
        @Field("grantType") grantType: String = "password",
        @Field("clientId") clientId: String = CLIENT_ID,
        @Field("clientSecret") clientSecret: String = CLIENT_SECRET
    ): Single<AuthToken>

    companion object {
        const val CLIENT_ID = "57da70f0-ff86-4571-a152-9ce0302314e2"
        const val CLIENT_SECRET = "GcdFE5nScShTa2tgBevJpdGsMOyU9ltHOnjJeyFhKbK4xySUp4cdvnl8n_rnDWSHZDKF0JDoO622If76iLnFBQ"
    }
}

@Keep
data class AuthToken(val accessToken: String, val refreshToken: String)