package dev.jriley.splash

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import dev.jriley.login.TokenRepository
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Test
import java.util.*
import java.util.concurrent.TimeUnit

class SplashViewModelTest {

    private val backgroundScheduler: TestScheduler = TestScheduler()
    private val tokenRepository: TokenRepository = mock()
    private lateinit var testObject: SplashViewModel

    @Test
    fun testTimeToGatherResources() {
        val expectedValue = Random().nextInt(100) % 2 == 0
        whenever(tokenRepository.isValid()).thenReturn(Single.just(expectedValue))
        testObject = SplashViewModel(backgroundScheduler, tokenRepository)

        val testObserver = testObject.loadingObservable.test()

        testObserver.apply {
            assertNoValues()
            assertNotComplete()
            assertNoErrors()
        }

        backgroundScheduler.triggerActions()
        backgroundScheduler.advanceTimeBy(3, TimeUnit.SECONDS)

        testObserver.apply {
            assertNoErrors()
            assertNotComplete()
            assertValue(expectedValue)
        }
    }
}