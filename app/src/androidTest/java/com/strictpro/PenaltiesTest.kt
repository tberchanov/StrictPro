package com.strictpro

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import com.strictpro.example.ExampleContent
import org.junit.Rule
import org.junit.Test
import com.strictpro.example.R as exampleR

class PenaltiesTest {
    @get:Rule val composeTestRule = createComposeRule()

    @Test
    fun dialog_on_disk_read_violation() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        composeTestRule.setContent {
            ExampleContent()
        }

        composeTestRule.onNodeWithText(
            context.getString(exampleR.string.Trigger_DiskReadViolation)
        ).performClick()

        onView(
            withText(
                R.string.strictmode_violation_detected
            )
        ).check(matches(isDisplayed()));
    }
}