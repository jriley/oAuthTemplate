package dev.jriley.login

import android.support.annotation.Keep
import com.squareup.moshi.Json
import dev.jriley.login.AuthTokenApi.Companion.CLIENT_ID
import dev.jriley.login.AuthTokenApi.Companion.CLIENT_SECRET
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import retrofit2.http.Body
import retrofit2.http.POST

class TokenRepository(
    private val authTokenApi: AuthTokenApi = AuthTokenApiFactory.authTokenApi,
    private val ioScheduler: Scheduler = Schedulers.io()
) : TokenRepo {

    override fun isValid(): Single<Boolean> = Single.just(false)

    override fun logInAttempt(loginCredentials: LoginCredentials): Completable =
        authTokenApi.login(AuthTokenRequest(loginCredentials.userName, loginCredentials.password))
            .subscribeOn(ioScheduler)
            .flatMapCompletable { Completable.complete() }
}

interface TokenRepo {
    fun isValid(): Single<Boolean>
    fun logInAttempt(loginCredentials: LoginCredentials): Completable
}


//object AuthTokenApiFactory {
//    val tokenApi: AuthTokenApi  by lazy {
//        object : AuthTokenApi {
//            override fun login(authRequest: AuthTokenRequest): Single<AuthTokenResponse> = when {
//                authRequest.username == "joe@example.com" && authRequest.password == "foobarbaz" -> Single.just(
//                    AuthTokenResponse("type", 1, "refresh-123456", "sope", authRequest.username, 2, "token-123456")
//                )
//                else -> Single.error { InvalidGrantException() }
//            }
//        }
//    }
//}

interface AuthTokenApi {

    @POST("oauth/token")
    fun login(@Body authRequest: AuthTokenRequest): Single<AuthTokenResponse>

    companion object {
        const val CLIENT_ID = BuildConfig.CLIENT_ID
        const val CLIENT_SECRET = BuildConfig.CLIENT_SECRET
    }
}

@Keep
data class AuthToken(val accessToken: String, val refreshToken: String)

data class AuthTokenRequest(
    val username: String,
    val password: String,
    @field:Json(name = "grant_type") val grantType: String = "password",
    @field:Json(name = "client_id") val clientId: String = CLIENT_ID,
    @field:Json(name = "client_secret") val clientSecret: String = CLIENT_SECRET
)

data class ReAuthTokenRequest(
    @field:Json(name = "refresh_token") val refreshToken: String,
    @field:Json(name = "grant_type") val grantType: String = "refresh",
    @field:Json(name = "client_id") val clientId: String = CLIENT_ID,
    @field:Json(name = "client_secret") val clientSecret: String = CLIENT_SECRET
)

data class AuthTokenResponse(
    @field:Json(name = "token_type") val tokenType: String,
    @field:Json(name = "refresh_token_expires_in") val refreshTokenExpiresIn: Long,
    @field:Json(name = "refresh_token") val refreshToken: String,
    val scope: String,
    @field:Json(name = "resource_owner") val resourceOwner: String,
    @field:Json(name = "expires_in") val expiresIn: Long,
    @field:Json(name = "access_token") val accessToken: String
) {
    companion object
}