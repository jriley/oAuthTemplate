package dev.jriley

import android.support.design.widget.TextInputLayout
import android.view.View
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

fun hasTextInputLayoutErrorText(expectedError: String?): Matcher<View> = object : TypeSafeMatcher<View>() {
    override fun describeTo(description: Description?) {
        description?.appendText("Expected Error string: $expectedError")
    }

    override fun matchesSafely(item: View?): Boolean = when (item) {
        is TextInputLayout -> {
            if (expectedError.isNullOrBlank() && item.error == null) true else expectedError == item.error.toString()
        }
        else -> false
    }
}

fun hasTextInputLayoutHintText(expectedHint: String?): Matcher<View> = object : TypeSafeMatcher<View>() {
    override fun describeTo(description: Description?) {
        description?.appendText("Expected Hint string: $expectedHint")
    }

    override fun matchesSafely(item: View?): Boolean = when (item) {
        is TextInputLayout -> {
            if (expectedHint.isNullOrBlank() && item.hint == null) true else expectedHint == item.hint.toString()
        }
        else -> false
    }
}