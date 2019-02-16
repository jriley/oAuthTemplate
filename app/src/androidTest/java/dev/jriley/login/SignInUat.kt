package dev.jriley.login

import android.os.Build
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import dev.jriley.auth.InvalidGrantException
import dev.jriley.auth.TokenRepo
import dev.jriley.auth.TokenRepositoryFactory
import dev.jriley.landing.MainScreen
import io.reactivex.Completable
import io.reactivex.CompletableEmitter
import io.reactivex.Single
import org.junit.Assume.assumeTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignInUat {

    @get:Rule
    val activityRule = ActivityTestRule(SignInActivity::class.java, false, false)

    @Test
    fun emptyUserNameError() {
        activityRule.launchActivity(null)

        SignInScreen.assertShowing()

        SignInScreen.assertUserNameHint(activityRule.activity.getString(R.string.email_or_user_name))

        SignInScreen.assertPasswordHint(activityRule.activity.getString(R.string.password))

        SignInScreen.clickSignIn()

        SignInScreen.assertUserNameError(activityRule.activity.getString(R.string.error_empty_field))

        SignInScreen.enterUserName("foo@bar.baz")

        SignInScreen.clickSignIn()

        SignInScreen.assertUserNameError(null)
    }

    @Test
    fun emptyPassword() {
        activityRule.launchActivity(null)

        SignInScreen.assertShowing()

        SignInScreen.enterUserName("foo@bar.baz")

        SignInScreen.clickSignIn()

        SignInScreen.assertPasswordError(activityRule.activity.getString(R.string.error_empty_field))
    }

    @Test
    fun invalidUserNameError() {
        assumeTrue("The username is expected to be an email.", BuildConfig.IS_USER_EMAIL)
        activityRule.launchActivity(null)

        SignInScreen.assertShowing()

        SignInScreen.enterUserName("foo")

        SignInScreen.enterPassword("bar")

        SignInScreen.clickSignIn()

        SignInScreen.assertUserNameError(activityRule.activity.getString(R.string.error_invalid_email))
    }

    @Test
    fun successfulLogin() {
        assumeTrue("Please only run on [Build.VERSION_CODES.N] ot higher.", Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        lateinit var completableEmitter: CompletableEmitter
        TokenRepositoryFactory.tokenRepository = object : TokenRepo {
            override fun isValid(): Single<Boolean> = Single.just(false)
            override fun logInAttempt(loginCredentials: LoginCredentials): Completable =
                Completable.create { emitter -> completableEmitter = emitter }
        }
        activityRule.launchActivity(null)

        SignInScreen.assertShowing()

        SignInScreen.enterUserName("foo@bar.baz")

        SignInScreen.enterPassword("bar")

        SignInScreen.clickSignIn()

        completableEmitter.onComplete()

        MainScreen.assertShowing()
    }

    @Test
    fun failedLogin() {
        assumeTrue("Please only run on [Build.VERSION_CODES.N] ot higher.", Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        lateinit var completableEmitter: CompletableEmitter
        TokenRepositoryFactory.tokenRepository = object : TokenRepo {
            override fun isValid(): Single<Boolean> = Single.just(false)
            override fun logInAttempt(loginCredentials: LoginCredentials): Completable =
                Completable.create { emitter -> completableEmitter = emitter }
        }
        activityRule.launchActivity(null)

        SignInScreen.assertShowing()

        SignInScreen.enterUserName("foo@bar.baz")

        SignInScreen.enterPassword("bar")

        SignInScreen.clickSignIn()

        completableEmitter.onError(InvalidGrantException())

        activityRule.activity.apply {
            SignInScreen.assertAlertDialog(
                getString(R.string.failed_login_title),
                getString(R.string.failed_login)
            )
        }

        SignInScreen.dismissDialog()
    }
}
