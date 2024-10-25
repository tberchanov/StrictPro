package com.strictpro

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.strictpro.StrictPro.VisibleForTestingOnly
import com.strictpro.example.ExampleContent
import com.strictpro.example.R
import com.strictpro.penalty.ViolationPenalty
import com.strictpro.penalty.executor.PenaltyExecutor
import com.strictpro.utils.VisibleForTestingOnly_DoNotUseInProductionCode
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

@OptIn(VisibleForTestingOnly_DoNotUseInProductionCode::class)
class WhiteListContainsTest : VisibleForTestingOnly {
    @get:Rule val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun whitelist_contains_on_thread_policy_violation() {
        val mockLogExecutor = mockk<PenaltyExecutor>()
        every { mockLogExecutor.executablePenalty() } returns ViolationPenalty.Log
        every { mockLogExecutor.executePenalty(any(), any()) } returns Unit

        val mockDeathExecutor = mockk<PenaltyExecutor>()
        every { mockDeathExecutor.executablePenalty() } returns ViolationPenalty.Death
        every { mockDeathExecutor.executePenalty(any(), any()) } returns Unit

        StrictPro.setPenaltyExecutors(
            mockLogExecutor,
            mockDeathExecutor,
        )

        composeTestRule.setContent {
            ExampleContent(
                threadPolicy = StrictPro.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .setWhiteList {
                        contains("CustomViolation", ViolationPenalty.Death)
                    }
                    .build(),
                vmPolicy = StrictPro.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build(),
            )
        }

        composeTestRule.onNodeWithText(
            context.getString(R.string.Trigger_CustomViolation)
        ).performClick()

        InstrumentationRegistry.getInstrumentation().waitForIdle {
            verify(exactly = 1) {
                mockDeathExecutor.executePenalty(any(), any())
            }
            verify(exactly = 0) {
                mockLogExecutor.executePenalty(any(), any())
            }
        }

        composeTestRule.onNodeWithText(
            context.getString(R.string.Trigger_ExplicitGcViolation)
        ).performClick()

        InstrumentationRegistry.getInstrumentation().waitForIdle {
            verify(exactly = 1) {
                mockLogExecutor.executePenalty(any(), any())
            }
            verify(exactly = 1) {
                mockDeathExecutor.executePenalty(any(), any())
            }
        }
    }

    @Test
    fun whitelist_contains_on_vm_policy_violation() {
        val mockLogExecutor = mockk<PenaltyExecutor>()
        every { mockLogExecutor.executablePenalty() } returns ViolationPenalty.Log
        every { mockLogExecutor.executePenalty(any(), any()) } returns Unit

        val mockDeathExecutor = mockk<PenaltyExecutor>()
        every { mockDeathExecutor.executablePenalty() } returns ViolationPenalty.Death
        every { mockDeathExecutor.executePenalty(any(), any()) } returns Unit

        StrictPro.setPenaltyExecutors(
            mockLogExecutor,
            mockDeathExecutor,
        )

        composeTestRule.setContent {
            ExampleContent(
                threadPolicy = StrictPro.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build(),
                vmPolicy = StrictPro.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .setWhiteList {
                        contains("NonSdkApiUsedViolation", ViolationPenalty.Death)
                    }
                    .build(),
            )
        }

        composeTestRule.onNodeWithText(
            context.getString(R.string.Trigger_NonSdkApiUsage)
        ).performClick()

        InstrumentationRegistry.getInstrumentation().waitForIdle {
            verify(exactly = 1) {
                mockDeathExecutor.executePenalty(any(), any())
            }
            verify(exactly = 0) {
                mockLogExecutor.executePenalty(any(), any())
            }
        }

        composeTestRule.onNodeWithText(
            context.getString(R.string.Trigger_LeakedClosableViolation)
        ).performClick()

        InstrumentationRegistry.getInstrumentation().waitForIdle {
            verify(atLeast = 1) {
                mockLogExecutor.executePenalty(any(), any())
            }
            verify(exactly = 1) {
                mockDeathExecutor.executePenalty(any(), any())
            }
        }
    }
}