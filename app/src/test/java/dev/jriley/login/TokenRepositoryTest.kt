package dev.jriley.login

import android.database.sqlite.SQLiteDatabaseLockedException
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import dev.jriley.auth.*
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import randomPositiveInt
import test
import java.lang.RuntimeException

class TokenRepositoryTest {

    private lateinit var testObject: TokenRepository
    private val authTokenApi: AuthTokenApi = mock()
    private val authTokenData: AuthTokenData = mock()
    private val testScheduler: TestScheduler = TestScheduler()

    @Before
    fun setUp() {
        testObject = TokenRepository(authTokenApi, authTokenData, testScheduler)
    }

    @Test
    fun `failed login from the api`() {
        val invalidGrantException = InvalidGrantException()
        whenever(authTokenApi.login(any())).thenReturn(Single.error(invalidGrantException))

        testObject.logInAttempt(LoginCredentials("foo", "bar")).test().apply {
            assertNoValues()
            assertNoErrors()
            assertNotComplete()

            testScheduler.triggerActions()

            assertNoValues()
            assertNotComplete()
            assertError { t -> t is InvalidGrantException }
        }
    }

    @Test
    fun `successful login`() {
        val nextInt = randomPositiveInt()
        val credentials = LoginCredentials("user-$nextInt", "pw-$nextInt")
        val expectedResponse = AuthTokenResponse.test()
        val expectedToken = Token(
            expectedResponse.accessToken,
            expectedResponse.resourceOwner,
            expectedResponse.refreshToken,
            expectedResponse.scope
        )
        whenever(
            authTokenApi.login(
                AuthTokenRequest(
                    credentials.userName,
                    credentials.password
                )
            )
        ).thenReturn(Single.just(expectedResponse))
        whenever(authTokenData.insert(expectedToken)).thenReturn(Completable.complete())

        testObject.logInAttempt(credentials).test().apply {
            assertNoValues()
            assertNotComplete()
            assertNoErrors()

            testScheduler.triggerActions()

            assertComplete()
            assertNoErrors()
        }
    }

    @Test
    fun `failed Login from the data insert`() {
        whenever(authTokenApi.login(any())).thenReturn(Single.just(AuthTokenResponse.test()))
        whenever(authTokenData.insert(any())).thenReturn(Completable.error(SQLiteDatabaseLockedException("Oh No")))

        testObject.logInAttempt(LoginCredentials("user", "pw")).test().apply {
            assertNoValues()
            assertNoErrors()
            assertNotComplete()

            testScheduler.triggerActions()

            assertNoValues()
            assertNotComplete()
            assertError { t -> t is SQLiteDatabaseLockedException }
        }
    }

    @Test
    fun `is valid`() {
        whenever(authTokenData.currentToken()).thenReturn(Maybe.just(Token.test()))

        testObject.isValid().test().apply {
            assertNoErrors()
            assertNoValues()
            assertNotComplete()

            testScheduler.triggerActions()

            assertValue(true)
            assertNoErrors()
            assertComplete()
        }
    }

    @Test
    fun `is valid errors`() {
        whenever(authTokenData.currentToken()).thenReturn(Maybe.error(RuntimeException("Oh No")))

        testObject.isValid().test().apply {
            assertNoErrors()
            assertNoValues()
            assertNotComplete()

            testScheduler.triggerActions()

            assertValue(false)
            assertNoErrors()
            assertComplete()
        }
    }

    @Test
    fun `the token has an empty access token`() {
        whenever(authTokenData.currentToken()).thenReturn(Maybe.just(Token.test(accessToken = " ")))

        testObject.isValid().test().apply {
            assertNoErrors()
            assertNoValues()
            assertNotComplete()

            testScheduler.triggerActions()

            assertValue(false)
            assertNoErrors()
            assertComplete()
        }
    }
}