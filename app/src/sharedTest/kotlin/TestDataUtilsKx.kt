import dev.jriley.auth.AuthTokenResponse
import dev.jriley.auth.Token
import dev.jriley.login.LoginCredentials
import kotlin.random.Random

private const val REASONABLE_BOUND = 100

fun randomPositiveInt(): Int = Random.nextInt(0, REASONABLE_BOUND)
fun randomPositiveLong(): Long = Random.nextLong(0, Long.MAX_VALUE)

fun Token.Companion.test(
    expectedId: Int = randomPositiveInt(),
    accessToken: String = "access_token-$expectedId",
    username: String = "username-$expectedId",
    refreshToken: String = "refresh_token-$expectedId",
    scope: String = "scope-$expectedId"
): Token = Token(accessToken, username, refreshToken, scope)

fun AuthTokenResponse.Companion.test(
    expectedId: Int = randomPositiveInt(),
    tokenType: String = "token_type-$expectedId",
    refreshTokenExpiresIn: Long = randomPositiveLong(),
    refreshToken: String = "refresh_token-$expectedId",
    scope: String = "scope-$expectedId",
    resourceOwner: String = "resource_owner-$expectedId",
    expiresIn: Long = randomPositiveLong(),
    accessToken: String = "access_token-$expectedId"
): AuthTokenResponse =
    AuthTokenResponse(
        tokenType,
        refreshTokenExpiresIn,
        refreshToken,
        scope,
        resourceOwner,
        expiresIn,
        accessToken
    )

fun LoginCredentials.Companion.test(
    expectedId: Int = randomPositiveInt(),
    username: String = "username-$expectedId",
    password: String = "password-$expectedId"
): LoginCredentials = LoginCredentials(username, password)
