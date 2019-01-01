package dev.jriley.login

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import dev.jriley.login.AuthTokenApi.Companion.CLIENT_ID
import dev.jriley.login.AuthTokenApi.Companion.CLIENT_SECRET
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import java.util.*

class TokenRepositoryTest {

    private lateinit var testObject: TokenRepository
    private val authTokenApi: AuthTokenApi = mock()
    private val testScheduler: TestScheduler = TestScheduler()
    private val random = Random()

    @Before
    fun setUp() {
        testObject = TokenRepository(authTokenApi, testScheduler)
    }

    @Test
    fun successfulLogin() {
        val nextInt = random.nextInt(100)
        val expectedValue = nextInt % 2 == 0
        val credentials = LoginCredentials("user-$nextInt", "pw-$nextInt")
        whenever(authTokenApi.login(credentials.userName, credentials.password)).thenReturn(Single.just(AuthToken("$expectedValue", "refresh-$expectedValue")))

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
    fun failedLogin() {
        val invalidGrantException = InvalidGrantException()
        whenever(authTokenApi.login(any(), any(), eq("password"), eq(CLIENT_ID), eq(CLIENT_SECRET))).thenReturn(Single.error(invalidGrantException))

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
}