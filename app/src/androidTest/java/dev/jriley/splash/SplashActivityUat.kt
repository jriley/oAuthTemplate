package dev.jriley.splash

import android.support.test.rule.ActivityTestRule
import dev.jriley.landing.MainScreen
import dev.jriley.login.LoginCredentials
import dev.jriley.login.SignInScreen
import dev.jriley.login.TokenRepo
import dev.jriley.login.TokenRepositoryFactory
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Rule
import org.junit.Test

class SplashActivityUat {

    @get:Rule
    val activityRule = ActivityTestRule(SplashActivity::class.java, false, false)

    @Test
    fun oAuthTokenIsValid_StartMainActivity() {
        TokenRepositoryFactory.tokenRepository = object: TokenRepo {
            override fun logInAttempt(loginCredentials: LoginCredentials): Completable = Completable.complete()
            override fun isValid(): Single<Boolean> = Single.just(true)
        }

        activityRule.launchActivity(null)

        SplashScreen.assertShowing()

        Thread.sleep(3000)

        MainScreen.assertShowing()
    }

    @Test
    fun oAuthTokenInvalid_startSignInActivity() {
        TokenRepositoryFactory.tokenRepository = object: TokenRepo {
            override fun logInAttempt(loginCredentials: LoginCredentials): Completable = Completable.complete()
            override fun isValid(): Single<Boolean> = Single.just(false)
        }

        activityRule.launchActivity(null)

        SplashScreen.assertShowing()

        Thread.sleep(3000)

        SignInScreen.assertShowing()
    }
}