package dev.jriley.splash

import android.support.test.espresso.Espresso
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import dev.jriley.assertTextVisible
import dev.jriley.login.R

object SplashScreen {

    fun assertShowing() {
        Espresso.onView(ViewMatchers.withId(R.id.content))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        assertTextVisible(R.id.splashLoading, R.string.splash_loading)
    }
}
