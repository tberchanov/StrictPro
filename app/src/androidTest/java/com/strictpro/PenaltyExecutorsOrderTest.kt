package com.strictpro

import android.app.Activity
import android.os.strictmode.Violation
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.strictpro.StrictPro.VisibleForTestingOnly
import com.strictpro.example.ComposeContent
import com.strictpro.penalty.ViolationPenalty
import com.strictpro.penalty.executor.PenaltyExecutor
import com.strictpro.utils.VisibleForTestingOnly_DoNotUseInProductionCode
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.strictpro.example.R as exampleR

@OptIn(VisibleForTestingOnly_DoNotUseInProductionCode::class)
class PenaltyExecutorsOrderTest : VisibleForTestingOnly {
    @get:Rule val composeTestRule = createComposeRule()

    private var logPenaltyTime = 0L
    private var deathPenaltyTime = 0L

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun before() {
        logPenaltyTime = 0L
        deathPenaltyTime = 0L

        StrictPro.setPenaltyExecutors(
            object : PenaltyExecutor {
                override fun executablePenalty() = ViolationPenalty.Log
                override fun executePenalty(violation: Violation, currentActivity: Activity?) {
                    // Some possible processing time simulation.
                    Thread.sleep(500)
                    logPenaltyTime = System.currentTimeMillis()
                }
            },
            object : PenaltyExecutor {
                override fun executablePenalty() = ViolationPenalty.Death
                override fun executePenalty(violation: Violation, currentActivity: Activity?) {
                    // Some possible processing time simulation.
                    Thread.sleep(500)
                    deathPenaltyTime = System.currentTimeMillis()
                }
            },
        )

        composeTestRule.setContent {
            ComposeContent(
                context,
                threadPolicy = StrictPro.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyDeath()
                    .penaltyLog()
                    .build(),
                vmPolicy = StrictPro.VmPolicy.Builder()
                    .detectAll()
                    .penaltyDeath()
                    .penaltyLog()
                    .build(),
            )
        }
    }

    @Test
    fun penalty_executors_order_on_thread_violation() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        composeTestRule.onNodeWithText(
            context.getString(exampleR.string.Trigger_CustomViolation)
        ).performClick()

        composeTestRule.waitUntil(1100) {
            // Is expected that Log penalty must execute before Death penalty.
            logPenaltyTime < deathPenaltyTime
        }
    }

    @Test
    fun penalty_executors_order_on_vm_violation() {
        composeTestRule.onNodeWithText(
            context.getString(exampleR.string.Trigger_NonSdkApiUsage)
        ).performClick()

        composeTestRule.waitUntil(1100) {
            // Is expected that Log penalty must execute before Death penalty.
            logPenaltyTime < deathPenaltyTime
        }
    }
}