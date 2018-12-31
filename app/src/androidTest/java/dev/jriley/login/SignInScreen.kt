package dev.jriley.login

import android.support.test.espresso.Espresso
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import dev.jriley.assertTextVisible

object SignInScreen {
    fun assertShowing() {
        Espresso.onView(ViewMatchers.withId(R.id.contentSignIn))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        assertTextVisible(R.id.loginBtn, R.string.sign_in)
    }
}
