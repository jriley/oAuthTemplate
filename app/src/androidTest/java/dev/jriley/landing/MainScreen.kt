package dev.jriley.landing

import android.support.test.espresso.Espresso
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import dev.jriley.assertTextVisible
import dev.jriley.login.R

object MainScreen {
    fun assertShowing() {
        Espresso.onView(ViewMatchers.withId(R.id.contentMain))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        assertTextVisible(R.id.mainText1, R.string.title_activity_main)
    }
}
