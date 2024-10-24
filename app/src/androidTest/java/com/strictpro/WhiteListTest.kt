package com.strictpro

import android.os.strictmode.CustomViolation
import android.os.strictmode.NonSdkApiUsedViolation
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.strictpro.StrictPro.VisibleForTestingOnly
import com.strictpro.example.ComposeContent
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
class WhiteListTest : VisibleForTestingOnly {
    @get:Rule val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun whitelist_condition_on_thread_policy_violation() {
        val mockLogExecutor = mockk<PenaltyExecutor>(name = "MockLogPenaltyExecutor")
        every { mockLogExecutor.executablePenalty() } returns ViolationPenalty.Log
        every { mockLogExecutor.executePenalty(any(), any()) } returns Unit

        val mockDeathExecutor = mockk<PenaltyExecutor>(name = "MockDeathPenaltyExecutor")
        every { mockDeathExecutor.executablePenalty() } returns ViolationPenalty.Death
        every { mockDeathExecutor.executePenalty(any(), any()) } returns Unit

        StrictPro.setPenaltyExecutors(
            mockLogExecutor,
            mockDeathExecutor,
        )

        composeTestRule.setContent {
            ComposeContent(
                context,
                threadPolicy = StrictPro.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .setWhiteList {
                        condition { violation ->
                            if (violation is CustomViolation) ViolationPenalty.Death else null
                        }
                    }
                    .build(),
                vmPolicy = StrictPro.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build(),
            )
        }
        composeTestRule.waitUntil(1000) { true }

        composeTestRule.onNodeWithText(
            context.getString(R.string.Trigger_CustomViolation)
        ).performClick()
        composeTestRule.waitUntil(1000) { true }

        verify(exactly = 1) {
            mockDeathExecutor.executePenalty(any(), any())
        }
        verify(exactly = 0) {
            mockLogExecutor.executePenalty(any(), any())
        }

        composeTestRule.onNodeWithText(
            context.getString(R.string.Trigger_ExplicitGcViolation)
        ).performClick()
        composeTestRule.waitUntil(1000) { true }

        verify(exactly = 1) {
            mockLogExecutor.executePenalty(any(), any())
        }
        verify(exactly = 1) {
            mockDeathExecutor.executePenalty(any(), any())
        }
    }

    @Test
    fun whitelist_condition_on_vm_policy_violation() {
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
            ComposeContent(
                context,
                threadPolicy = StrictPro.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build(),
                vmPolicy = StrictPro.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .setWhiteList {
                        condition { violation ->
                            if (violation is NonSdkApiUsedViolation) ViolationPenalty.Death else null
                        }
                    }
                    .build(),
            )
        }
        composeTestRule.waitUntil(1000) { true }

        composeTestRule.onNodeWithText(
            context.getString(R.string.Trigger_NonSdkApiUsage)
        ).performClick()
        composeTestRule.waitUntil(1000) { true }

        verify(exactly = 1) {
            mockDeathExecutor.executePenalty(any(), any())
        }
        verify(exactly = 0) {
            mockLogExecutor.executePenalty(any(), any())
        }

//        composeTestRule.onNodeWithText(
//            context.getString(R.string.Trigger_LeakedClosableViolation)
//        ).performClick()
//        composeTestRule.waitUntil(1000) { true }
//
//        verify(atLeast = 1) {
//            mockLogExecutor.executePenalty(any(), any())
//        }
//        verify(exactly = 1) {
//            mockDeathExecutor.executePenalty(any(), any())
//        }
    }

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
            ComposeContent(
                context,
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
        composeTestRule.waitUntil(1000) { true }

        composeTestRule.onNodeWithText(
            context.getString(R.string.Trigger_CustomViolation)
        ).performClick()
        composeTestRule.waitUntil(1000) { true }

        verify(exactly = 1) {
            mockDeathExecutor.executePenalty(any(), any())
        }
        verify(exactly = 0) {
            mockLogExecutor.executePenalty(any(), any())
        }

        composeTestRule.onNodeWithText(
            context.getString(R.string.Trigger_ExplicitGcViolation)
        ).performClick()
        composeTestRule.waitUntil(1000) { true }

        verify(exactly = 1) {
            mockLogExecutor.executePenalty(any(), any())
        }
        verify(exactly = 1) {
            mockDeathExecutor.executePenalty(any(), any())
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
            ComposeContent(
                context,
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
        composeTestRule.waitUntil(1000) { true }

        composeTestRule.onNodeWithText(
            context.getString(R.string.Trigger_NonSdkApiUsage)
        ).performClick()
        composeTestRule.waitUntil(1000) { true }

        verify(exactly = 1) {
            mockDeathExecutor.executePenalty(any(), any())
        }
        verify(exactly = 0) {
            mockLogExecutor.executePenalty(any(), any())
        }

//        composeTestRule.onNodeWithText(
//            context.getString(R.string.Trigger_LeakedClosableViolation)
//        ).performClick()
//        composeTestRule.waitUntil(1000) { true }
//
//        verify(atLeast = 1) {
//            mockLogExecutor.executePenalty(any(), any())
//        }
//        verify(exactly = 1) {
//            mockDeathExecutor.executePenalty(any(), any())
//        }
    }
}