package dev.jriley.login

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.*
import dev.jriley.*

object SignInScreen {
    fun assertShowing() {
        onView(ViewMatchers.withId(R.id.contentSignIn))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        assertTextVisible(R.id.loginBtn, R.string.sign_in)
        assertViewVisibleEnabled(R.id.userName)
        assertViewVisibleEnabled(R.id.password)
    }

    fun clickSignIn() {
        assertViewVisibleEnabled(R.id.loginBtn)
        onView(withId(R.id.loginBtn)).perform(click())
    }

    fun assertUserNameError(errorMessage: String?) = assertTextInputLayoutError(R.id.userNameLayout, errorMessage)
    fun assertUserNameHint(hint: String?) = assertTextInputLayoutHint(R.id.userNameLayout, hint)
    fun assertPasswordError(errorMessage: String?) = assertTextInputLayoutError(R.id.passwordLayout, errorMessage)
    fun assertPasswordHint(hint: String?) = assertTextInputLayoutHint(R.id.passwordLayout, hint)

    fun enterUserName(userName: String) = setTextNoKeyBoard_Quick(R.id.userName, userName)
    fun enterPassword(password: String) = setTextNoKeyBoard_Quick(R.id.password, password)
    fun assertAlertDialog(title: String, message: String) {
        onView(withText(title)).check(matches(isDisplayed()))
        onView(withText(message)).check(matches(isDisplayed()))
    }

    fun dismissDialog(): ViewInteraction = onView(withText(android.R.string.ok)).perform(click())
}
