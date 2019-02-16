package dev.jriley.login

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import dev.jriley.landing.MainScreen
import io.reactivex.Completable
import io.reactivex.Single
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
    fun successfulLogin() {
        TokenRepositoryFactory.tokenRepository = object : TokenRepo {
            override fun isValid(): Single<Boolean> = Single.just(false)
            override fun logInAttempt(loginCredentials: LoginCredentials): Completable = Completable.complete()
        }
        activityRule.launchActivity(null)

        SignInScreen.assertShowing()

        SignInScreen.enterUserName("foo@bar.baz")

        SignInScreen.enterPassword("bar")

        SignInScreen.clickSignIn()

        MainScreen.assertShowing()
    }

    @Test
    fun failedLogin() {
        TokenRepositoryFactory.tokenRepository = object : TokenRepo {
            override fun isValid(): Single<Boolean> = Single.just(false)
            override fun logInAttempt(loginCredentials: LoginCredentials): Completable =
                Completable.error(InvalidGrantException())
        }
        activityRule.launchActivity(null)

        SignInScreen.assertShowing()

        SignInScreen.enterUserName("foo@bar.baz")

        SignInScreen.enterPassword("bar")

        SignInScreen.clickSignIn()

        activityRule.activity.apply {
            SignInScreen.assertAlertDialog(
                getString(R.string.failed_login_title),
                getString(R.string.failed_login)
            )
        }

        SignInScreen.dismissDialog()
    }
}
