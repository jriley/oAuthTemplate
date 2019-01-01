package dev.jriley

import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.PerformException
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.espresso.util.HumanReadables
import android.support.test.espresso.util.TreeIterables
import android.view.View
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


fun assertTextVisible(viewId: Int, textRefId: Int) {
    Espresso.onView(ViewMatchers.withId(viewId))
        .check(ViewAssertions.matches(Matchers.allOf(ViewMatchers.isDisplayed(), ViewMatchers.withText(textRefId))))
}

fun assertViewVisibleEnabled(viewId: Int){
    onView(withId(viewId)).check(matches(allOf(isDisplayed(), isEnabled())))
}

fun assertTextInputLayoutError(textInputLayoutId: Int, expectedError: String?){
    onView(withId(textInputLayoutId)).check(matches(hasTextInputLayoutErrorText(expectedError)))
}

fun assertTextInputLayoutHint(textInputLayoutId: Int, expectedHint: String?){
    onView(withId(textInputLayoutId)).check(matches(hasTextInputLayoutHintText(expectedHint)))
}

fun typeTextAndCloseKeyboard_Slow(viewId: Int, text: String) {
    onView(withId(viewId)).perform(ViewActions.clearText(), ViewActions.typeText(text))
    Espresso.closeSoftKeyboard()
}

fun setTextNoKeyBoard_Quick(viewId: Int, text: String) {
    onView(withId(viewId)).perform(ViewActions.replaceText(text))
}

fun assertTextVisibleWait(viewId: Int, textRefId: Int, millis: Long){
    onView(ViewMatchers.withId(viewId)).perform(waitId(textRefId, TimeUnit.SECONDS.toMillis(millis)))
}

/** Perform action of waiting for a specific view id.  */
fun waitId(viewId: Int, millis: Long): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return ViewMatchers.withId(viewId)
        }

        override fun getDescription(): String {
            return "wait for a specific view with id <$viewId> during $millis millis."
        }

        override fun perform(uiController: UiController, view: View) {
            uiController.loopMainThreadUntilIdle()
            val startTime = System.currentTimeMillis()
            val endTime = startTime + millis
            val viewMatcher = withId(viewId)

            do {
                for (child in TreeIterables.breadthFirstViewTraversal(view)) {
                    // found view with required ID
                    if (viewMatcher.matches(child)) {
                        return
                    }
                }

                uiController.loopMainThreadForAtLeast(50)
            } while (System.currentTimeMillis() < endTime)

            // timeout happens
            throw PerformException.Builder()
                .withActionDescription(this.description)
                .withViewDescription(HumanReadables.describe(view))
                .withCause(TimeoutException())
                .build()
        }
    }
}
