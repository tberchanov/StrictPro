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
class WhitelistBase64Test : VisibleForTestingOnly {
    @get:Rule val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun whitelist_base64_on_thread_policy_violation() {
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
                        base64(
                            // CustomViolation
                            "YW5kcm9pZC5vcy5zdHJpY3Rtb2RlLkN1c3RvbVZpb2xhdGlvbjogU2xvdyBjYWxsCglhdCBhbmRyb2lkLm9zLlN0cmljdE1vZGUkQW5kcm9pZEJsb2NrR3VhcmRQb2xpY3kub25DdXN0b21TbG93Q2FsbChTdHJpY3RNb2RlLmphdmE6MTY1MCkKCWF0IGFuZHJvaWQub3MuU3RyaWN0TW9kZS5ub3RlU2xvd0NhbGwoU3RyaWN0TW9kZS5qYXZhOjI3ODUpCglhdCBjb20uc3RyaWN0cHJvLmV4YW1wbGUuQ29tcG9zZUNvbnRlbnRLdCRDb21wb3NlQ29udGVudCQyJDEkMSQxJDMkMy5pbnZva2UoQ29tcG9zZUNvbnRlbnQua3Q6OTIpCglhdCBjb20uc3RyaWN0cHJvLmV4YW1wbGUuQ29tcG9zZUNvbnRlbnRLdCRDb21wb3NlQ29udGVudCQyJDEkMSQxJDMkMy5pbnZva2UoQ29tcG9zZUNvbnRlbnQua3Q6OTIpCglhdCBhbmRyb2lkeC5jb21wb3NlLmZvdW5kYXRpb24uQ2xpY2thYmxlTm9kZSRjbGlja1BvaW50ZXJJbnB1dCQzLmludm9rZS1rLTRsUTBNKENsaWNrYWJsZS5rdDo2MzkpCglhdCBhbmRyb2lkeC5jb21wb3NlLmZvdW5kYXRpb24uQ2xpY2thYmxlTm9kZSRjbGlja1BvaW50ZXJJbnB1dCQzLmludm9rZShDbGlja2FibGUua3Q6NjMzKQoJYXQgYW5kcm9pZHguY29tcG9zZS5mb3VuZGF0aW9uLmdlc3R1cmVzLlRhcEdlc3R1cmVEZXRlY3Rvckt0JGRldGVjdFRhcEFuZFByZXNzJDIkMS5pbnZva2VTdXNwZW5kKFRhcEdlc3R1cmVEZXRlY3Rvci5rdDoyNTUpCglhdCBrb3RsaW4uY29yb3V0aW5lcy5qdm0uaW50ZXJuYWwuQmFzZUNvbnRpbnVhdGlvbkltcGwucmVzdW1lV2l0aChDb250aW51YXRpb25JbXBsLmt0OjMzKQoJYXQga290bGlueC5jb3JvdXRpbmVzLkRpc3BhdGNoZWRUYXNrS3QucmVzdW1lKERpc3BhdGNoZWRUYXNrLmt0OjE3OSkKCWF0IGtvdGxpbnguY29yb3V0aW5lcy5EaXNwYXRjaGVkVGFza0t0LmRpc3BhdGNoKERpc3BhdGNoZWRUYXNrLmt0OjE2OCkKCWF0IGtvdGxpbnguY29yb3V0aW5lcy5DYW5jZWxsYWJsZUNvbnRpbnVhdGlvbkltcGwuZGlzcGF0Y2hSZXN1bWUoQ2FuY2VsbGFibGVDb250aW51YXRpb25JbXBsLmt0OjQ3NCkKCWF0IGtvdGxpbnguY29yb3V0aW5lcy5DYW5jZWxsYWJsZUNvbnRpbnVhdGlvbkltcGwucmVzdW1lSW1wbChDYW5jZWxsYWJsZUNvbnRpbnVhdGlvbkltcGwua3Q6NTA4KQoJYXQga290bGlueC5jb3JvdXRpbmVzLkNhbmNlbGxhYmxlQ29udGludWF0aW9uSW1wbC5yZXN1bWVJbXBsJGRlZmF1bHQoQ2FuY2VsbGFibGVDb250aW51YXRpb25JbXBsLmt0OjQ5NykKCWF0IGtvdGxpbnguY29yb3V0aW5lcy5DYW5jZWxsYWJsZUNvbnRpbnVhdGlvbkltcGwucmVzdW1lV2l0aChDYW5jZWxsYWJsZUNvbnRpbnVhdGlvbkltcGwua3Q6MzY4KQoJYXQgYW5kcm9pZHguY29tcG9zZS51aS5pbnB1dC5wb2ludGVyLlN1c3BlbmRpbmdQb2ludGVySW5wdXRNb2RpZmllck5vZGVJbXBsJFBvaW50ZXJFdmVudEhhbmRsZXJDb3JvdXRpbmUub2ZmZXJQb2ludGVyRXZlbnQoU3VzcGVuZGluZ1BvaW50ZXJJbnB1dEZpbHRlci5rdDo3MTkpCglhdCBhbmRyb2lkeC5jb21wb3NlLnVpLmlucHV0LnBvaW50ZXIuU3VzcGVuZGluZ1BvaW50ZXJJbnB1dE1vZGlmaWVyTm9kZUltcGwuZGlzcGF0Y2hQb2ludGVyRXZlbnQoU3VzcGVuZGluZ1BvaW50ZXJJbnB1dEZpbHRlci5rdDo1OTgpCglhdCBhbmRyb2lkeC5jb21wb3NlLnVpLmlucHV0LnBvaW50ZXIuU3VzcGVuZGluZ1BvaW50ZXJJbnB1dE1vZGlmaWVyTm9kZUltcGwub25Qb2ludGVyRXZlbnQtSDBwUnVvWShTdXNwZW5kaW5nUG9pbnRlcklucHV0RmlsdGVyLmt0OjYyMCkKCWF0IGFuZHJvaWR4LmNvbXBvc2UuZm91bmRhdGlvbi5BYnN0cmFjdENsaWNrYWJsZU5vZGUub25Qb2ludGVyRXZlbnQtSDBwUnVvWShDbGlja2FibGUua3Q6MTA0NCkKCWF0IGFuZHJvaWR4LmNvbXBvc2UudWkuaW5wdXQucG9pbnRlci5Ob2RlLmRpc3BhdGNoTWFpbkV2ZW50UGFzcyhIaXRQYXRoVHJhY2tlci5rdDozODcpCglhdCBhbmRyb2lkeC5jb21wb3NlLnVpLmlucHV0LnBvaW50ZXIuTm9kZS5kaXNwYXRjaE1haW5FdmVudFBhc3MoSGl0UGF0aFRyYWNrZXIua3Q6MzczKQoJYXQgYW5kcm9pZHguY29tcG9zZS51aS5pbnB1dC5wb2ludGVyLk5vZGVQYXJlbnQuZGlzcGF0Y2hNYWluRXZlbnRQYXNzKEhpdFBhdGhUcmFja2VyLmt0OjIyOSkKCWF0IGFuZHJvaWR4LmNvbXBvc2UudWkuaW5wdXQucG9pbnRlci5IaXRQYXRoVHJhY2tlci5kaXNwYXRjaENoYW5nZXMoSGl0UGF0aFRyYWNrZXIua3Q6MTQ0KQoJYXQgYW5kcm9pZHguY29tcG9zZS51aS5pbnB1dC5wb2ludGVyLlBvaW50ZXJJbnB1dEV2ZW50UHJvY2Vzc29yLnByb2Nlc3MtQkl6WGZvZyhQb2ludGVySW5wdXRFdmVudFByb2Nlc3Nvci5rdDoxMjApCglhdCBhbmRyb2lkeC5jb21wb3NlLnVpLnBsYXRmb3JtLkFuZHJvaWRDb21wb3NlVmlldy5zZW5kTW90aW9uRXZlbnQtOGlBc1ZUYyhBbmRyb2lkQ29tcG9zZVZpZXcuYW5kcm9pZC5rdDoxOTk0KQoJYXQgYW5kcm9pZHguY29tcG9zZS51aS5wbGF0Zm9ybS5BbmRyb2lkQ29tcG9zZVZpZXcuaGFuZGxlTW90aW9uRXZlbnQtOGlBc1ZUYyhBbmRyb2lkQ29tcG9zZVZpZXcuYW5kcm9pZC5rdDoxOTQ1KQoJYXQgYW5kcm9pZHguY29tcG9zZS51aS5wbGF0Zm9ybS5BbmRyb2lkQ29tcG9zZVZpZXcuZGlzcGF0Y2hUb3VjaEV2ZW50KEFuZHJvaWRDb21wb3NlVmlldy5hbmRyb2lkLmt0OjE4MjkpCglhdCBhbmRyb2lkLnZpZXcuVmlld0dyb3VwLmRpc3BhdGNoVHJhbnNmb3JtZWRUb3VjaEV2ZW50KFZpZXdHcm91cC5qYXZhOjMxMjIpCglhdCBhbmRyb2lkLnZpZXcuVmlld0dyb3VwLmRpc3BhdGNoVG91Y2hFdmVudChWaWV3R3JvdXAuamF2YToyODAzKQoJYXQgYW5kcm9pZC52aWV3LlZpZXdHcm91cC5kaXNwYXRjaFRyYW5zZm9ybWVkVG91Y2hFdmVudChWaWV3R3JvdXAuamF2YTozMTIyKQoJYXQgYW5kcm9pZC52aWV3LlZpZXdHcm91cC5kaXNwYXRjaFRvdWNoRXZlbnQoVmlld0dyb3VwLmphdmE6MjgwMykKCWF0IGFuZHJvaWQudmlldy5WaWV3R3JvdXAuZGlzcGF0Y2hUcmFuc2Zvcm1lZFRvdWNoRXZlbnQoVmlld0dyb3VwLmphdmE6MzEyMikKCWF0IGFuZHJvaWQudmlldy5WaWV3R3JvdXAuZGlzcGF0Y2hUb3VjaEV2ZW50KFZpZXdHcm91cC5qYXZhOjI4MDMpCglhdCBhbmRyb2lkLnZpZXcuVmlld0dyb3VwLmRpc3BhdGNoVHJhbnNmb3JtZWRUb3VjaEV2ZW50KFZpZXdHcm91cC5qYXZhOjMxMjIpCglhdCBhbmRyb2lkLnZpZXcuVmlld0dyb3VwLmRpc3BhdGNoVG91Y2hFdmVudChWaWV3R3JvdXAuamF2YToyODAzKQoJYXQgY29tLmFuZHJvaWQuaW50ZXJuYWwucG9saWN5LkRlY29yVmlldy5zdXBlckRpc3BhdGNoVG91Y2hFdmVudChEZWNvclZpZXcuamF2YTo0NTgpCglhdCBjb20uYW5kcm9pZC5pbnRlcm5hbC5wb2xpY3kuUGhvbmVXaW5kb3cuc3VwZXJEaXNwYXRjaFRvdWNoRXZlbnQoUGhvbmVXaW5kb3cuamF2YToxOTgwKQoJYXQgYW5kcm9pZC5hcHAuQWN0aXZpdHkuZGlzcGF0Y2hUb3VjaEV2ZW50KEFjdGl2aXR5LmphdmE6NDUzMykKCWF0IGNvbS5hbmRyb2lkLmludGVybmFsLnBvbGljeS5EZWNvclZpZXcuZGlzcGF0Y2hUb3VjaEV2ZW50KERlY29yVmlldy5qYXZhOjQxNikKCWF0IGFuZHJvaWQudmlldy5WaWV3LmRpc3BhdGNoUG9pbnRlckV2ZW50KFZpZXcuamF2YToxNjcyOSkKCWF0IGFuZHJvaWQudmlldy5WaWV3Um9vdEltcGwkVmlld1Bvc3RJbWVJbnB1dFN0YWdlLnByb2Nlc3NQb2ludGVyRXZlbnQoVmlld1Jvb3RJbXBsLmphdmE6Nzk0NykKCWF0IGFuZHJvaWQudmlldy5WaWV3Um9vdEltcGwkVmlld1Bvc3RJbWVJbnB1dFN0YWdlLm9uUHJvY2VzcyhWaWV3Um9vdEltcGwuamF2YTo3NzEwKQoJYXQgYW5kcm9pZC52aWV3LlZpZXdSb290SW1wbCRJbnB1dFN0YWdlLmRlbGl2ZXIoVmlld1Jvb3RJbXBsLmphdmE6NzEwNikKCWF0IGFuZHJvaWQudmlldy5WaWV3Um9vdEltcGwkSW5wdXRTdGFnZS5vbkRlbGl2ZXJUb05leHQoVmlld1Jvb3RJbXBsLmphdmE6NzE2MykKCWF0IGFuZHJvaWQudmlldy5WaWV3Um9vdEltcGwkSW5wdXRTdGFnZS5mb3J3YXJkKFZpZXdSb290SW1wbC5qYXZhOjcxMjkpCglhdCBhbmRyb2lkLnZpZXcuVmlld1Jvb3RJbXBsJEFzeW5jSW5wdXRTdGFnZS5mb3J3YXJkKFZpZXdSb290SW1wbC5qYXZhOjcyOTUpCglhdCBhbmRyb2lkLnZpZXcuVmlld1Jvb3RJbXBsJElucHV0U3RhZ2UuYXBwbHkoVmlld1Jvb3RJbXBsLmphdmE6NzEzNykKCWF0IGFuZHJvaWQudmlldy5WaWV3Um9vdEltcGwkQXN5bmNJbnB1dFN0YWdlLmFwcGx5KFZpZXdSb290SW1wbC5qYXZhOjczNTIpCglhdCBhbmRyb2lkLnZpZXcuVmlld1Jvb3RJbXBsJElucHV0U3RhZ2UuZGVsaXZlcihWaWV3Um9vdEltcGwuamF2YTo3MTEwKQoJYXQgYW5kcm9pZC52aWV3LlZpZXdSb290SW1wbCRJbnB1dFN0YWdlLm9uRGVsaXZlclRvTmV4dChWaWV3Um9vdEltcGwuamF2YTo3MTYzKQoJYXQgYW5kcm9pZC52aWV3LlZpZXdSb290SW1wbCRJbnB1dFN0YWdlLmZvcndhcmQoVmlld1Jvb3RJbXBsLmphdmE6NzEyOSkKCWF0IGFuZHJvaWQudmlldy5WaWV3Um9vdEltcGwkSW5wdXRTdGFnZS5hcHBseShWaWV3Um9vdEltcGwuamF2YTo3MTM3KQoJYXQgYW5kcm9pZC52aWV3LlZpZXdSb290SW1wbCRJbnB1dFN0YWdlLmRlbGl2ZXIoVmlld1Jvb3RJbXBsLmphdmE6NzExMCkKCWF0IGFuZHJvaWQudmlldy5WaWV3Um9vdEltcGwuZGVsaXZlcklucHV0RXZlbnQoVmlld1Jvb3RJbXBsLmphdmE6MTAyMTQpCglhdCBhbmRyb2lkLnZpZXcuVmlld1Jvb3RJbXBsLmRvUHJvY2Vzc0lucHV0RXZlbnRzKFZpZXdSb290SW1wbC5qYXZhOjEwMTY1KQoJYXQgYW5kcm9pZC52aWV3LlZpZXdSb290SW1wbC5lbnF1ZXVlSW5wdXRFdmVudChWaWV3Um9vdEltcGwuamF2YToxMDEzNCkKCWF0IGFuZHJvaWQudmlldy5WaWV3Um9vdEltcGwkV2luZG93SW5wdXRFdmVudFJlY2VpdmVyLm9uSW5wdXRFdmVudChWaWV3Um9vdEltcGwuamF2YToxMDM1NikKCWF0IGFuZHJvaWQudmlldy5JbnB1dEV2ZW50UmVjZWl2ZXIuZGlzcGF0Y2hJbnB1dEV2ZW50KElucHV0RXZlbnRSZWNlaXZlci5qYXZhOjI5NSkKCWF0IGFuZHJvaWQub3MuTWVzc2FnZVF1ZXVlLm5hdGl2ZVBvbGxPbmNlKE5hdGl2ZSBNZXRob2QpCglhdCBhbmRyb2lkLm9zLk1lc3NhZ2VRdWV1ZS5uZXh0KE1lc3NhZ2VRdWV1ZS5qYXZhOjM0NikKCWF0IGFuZHJvaWQub3MuTG9vcGVyLmxvb3BPbmNlKExvb3Blci5qYXZhOjE4OSkKCWF0IGFuZHJvaWQub3MuTG9vcGVyLmxvb3AoTG9vcGVyLmphdmE6MzE3KQoJYXQgYW5kcm9pZC5hcHAuQWN0aXZpdHlUaHJlYWQubWFpbihBY3Rpdml0eVRocmVhZC5qYXZhOjg3MDUpCglhdCBqYXZhLmxhbmcucmVmbGVjdC5NZXRob2QuaW52b2tlKE5hdGl2ZSBNZXRob2QpCglhdCBjb20uYW5kcm9pZC5pbnRlcm5hbC5vcy5SdW50aW1lSW5pdCRNZXRob2RBbmRBcmdzQ2FsbGVyLnJ1bihSdW50aW1lSW5pdC5qYXZhOjU4MCkKCWF0IGNvbS5hbmRyb2lkLmludGVybmFsLm9zLlp5Z290ZUluaXQubWFpbihaeWdvdGVJbml0LmphdmE6ODg2KQo=",
                            ViolationPenalty.Death
                        )
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
    fun whitelist_base64_on_vm_policy_violation() {
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
                        base64(
                            // NonSdkApiUsedViolation
                            "YW5kcm9pZC5vcy5zdHJpY3Rtb2RlLk5vblNka0FwaVVzZWRWaW9sYXRpb246IExhbmRyb2lkL2FwcC9BY3Rpdml0eVRocmVhZDstPmN1cnJlbnRBY3Rpdml0eVRocmVhZCgpTGFuZHJvaWQvYXBwL0FjdGl2aXR5VGhyZWFkOwoJYXQgYW5kcm9pZC5vcy5TdHJpY3RNb2RlLmxhbWJkYSRzdGF0aWMkMShTdHJpY3RNb2RlLmphdmE6NDM2KQoJYXQgYW5kcm9pZC5vcy5TdHJpY3RNb2RlJCRFeHRlcm5hbFN5bnRoZXRpY0xhbWJkYTIuYWNjZXB0KEQ4JCRTeW50aGV0aWNDbGFzczowKQoJYXQgamF2YS5sYW5nLkNsYXNzLmdldERlY2xhcmVkTWV0aG9kSW50ZXJuYWwoTmF0aXZlIE1ldGhvZCkKCWF0IGphdmEubGFuZy5DbGFzcy5nZXRQdWJsaWNNZXRob2RSZWN1cnNpdmUoQ2xhc3MuamF2YToyOTU3KQoJYXQgamF2YS5sYW5nLkNsYXNzLmdldE1ldGhvZChDbGFzcy5qYXZhOjI5NDQpCglhdCBqYXZhLmxhbmcuQ2xhc3MuZ2V0TWV0aG9kKENsYXNzLmphdmE6MjQ1MCkKCWF0IGNvbS5zdHJpY3Rwcm8uZXhhbXBsZS5VdGlsc0t0LnBlcmZvcm1Ob25TZGtBcGlVc2FnZShVdGlscy5rdDo3OSkKCWF0IGNvbS5zdHJpY3Rwcm8uZXhhbXBsZS5Db21wb3NlQ29udGVudEt0JENvbXBvc2VDb250ZW50JDIkMSQxJDEkMyQxMS5pbnZva2UoQ29tcG9zZUNvbnRlbnQua3Q6MTA0KQoJYXQgY29tLnN0cmljdHByby5leGFtcGxlLkNvbXBvc2VDb250ZW50S3QkQ29tcG9zZUNvbnRlbnQkMiQxJDEkMSQzJDExLmludm9rZShDb21wb3NlQ29udGVudC5rdDoxMDQpCglhdCBhbmRyb2lkeC5jb21wb3NlLmZvdW5kYXRpb24uQ2xpY2thYmxlTm9kZSRjbGlja1BvaW50ZXJJbnB1dCQzLmludm9rZS1rLTRsUTBNKENsaWNrYWJsZS5rdDo2MzkpCglhdCBhbmRyb2lkeC5jb21wb3NlLmZvdW5kYXRpb24uQ2xpY2thYmxlTm9kZSRjbGlja1BvaW50ZXJJbnB1dCQzLmludm9rZShDbGlja2FibGUua3Q6NjMzKQoJYXQgYW5kcm9pZHguY29tcG9zZS5mb3VuZGF0aW9uLmdlc3R1cmVzLlRhcEdlc3R1cmVEZXRlY3Rvckt0JGRldGVjdFRhcEFuZFByZXNzJDIkMS5pbnZva2VTdXNwZW5kKFRhcEdlc3R1cmVEZXRlY3Rvci5rdDoyNTUpCglhdCBrb3RsaW4uY29yb3V0aW5lcy5qdm0uaW50ZXJuYWwuQmFzZUNvbnRpbnVhdGlvbkltcGwucmVzdW1lV2l0aChDb250aW51YXRpb25JbXBsLmt0OjMzKQoJYXQga290bGlueC5jb3JvdXRpbmVzLkRpc3BhdGNoZWRUYXNrS3QucmVzdW1lKERpc3BhdGNoZWRUYXNrLmt0OjE3OSkKCWF0IGtvdGxpbnguY29yb3V0aW5lcy5EaXNwYXRjaGVkVGFza0t0LmRpc3BhdGNoKERpc3BhdGNoZWRUYXNrLmt0OjE2OCkKCWF0IGtvdGxpbnguY29yb3V0aW5lcy5DYW5jZWxsYWJsZUNvbnRpbnVhdGlvbkltcGwuZGlzcGF0Y2hSZXN1bWUoQ2FuY2VsbGFibGVDb250aW51YXRpb25JbXBsLmt0OjQ3NCkKCWF0IGtvdGxpbnguY29yb3V0aW5lcy5DYW5jZWxsYWJsZUNvbnRpbnVhdGlvbkltcGwucmVzdW1lSW1wbChDYW5jZWxsYWJsZUNvbnRpbnVhdGlvbkltcGwua3Q6NTA4KQoJYXQga290bGlueC5jb3JvdXRpbmVzLkNhbmNlbGxhYmxlQ29udGludWF0aW9uSW1wbC5yZXN1bWVJbXBsJGRlZmF1bHQoQ2FuY2VsbGFibGVDb250aW51YXRpb25JbXBsLmt0OjQ5NykKCWF0IGtvdGxpbnguY29yb3V0aW5lcy5DYW5jZWxsYWJsZUNvbnRpbnVhdGlvbkltcGwucmVzdW1lV2l0aChDYW5jZWxsYWJsZUNvbnRpbnVhdGlvbkltcGwua3Q6MzY4KQoJYXQgYW5kcm9pZHguY29tcG9zZS51aS5pbnB1dC5wb2ludGVyLlN1c3BlbmRpbmdQb2ludGVySW5wdXRNb2RpZmllck5vZGVJbXBsJFBvaW50ZXJFdmVudEhhbmRsZXJDb3JvdXRpbmUub2ZmZXJQb2ludGVyRXZlbnQoU3VzcGVuZGluZ1BvaW50ZXJJbnB1dEZpbHRlci5rdDo3MTkpCglhdCBhbmRyb2lkeC5jb21wb3NlLnVpLmlucHV0LnBvaW50ZXIuU3VzcGVuZGluZ1BvaW50ZXJJbnB1dE1vZGlmaWVyTm9kZUltcGwuZGlzcGF0Y2hQb2ludGVyRXZlbnQoU3VzcGVuZGluZ1BvaW50ZXJJbnB1dEZpbHRlci5rdDo1OTgpCglhdCBhbmRyb2lkeC5jb21wb3NlLnVpLmlucHV0LnBvaW50ZXIuU3VzcGVuZGluZ1BvaW50ZXJJbnB1dE1vZGlmaWVyTm9kZUltcGwub25Qb2ludGVyRXZlbnQtSDBwUnVvWShTdXNwZW5kaW5nUG9pbnRlcklucHV0RmlsdGVyLmt0OjYyMCkKCWF0IGFuZHJvaWR4LmNvbXBvc2UuZm91bmRhdGlvbi5BYnN0cmFjdENsaWNrYWJsZU5vZGUub25Qb2ludGVyRXZlbnQtSDBwUnVvWShDbGlja2FibGUua3Q6MTA0NCkKCWF0IGFuZHJvaWR4LmNvbXBvc2UudWkuaW5wdXQucG9pbnRlci5Ob2RlLmRpc3BhdGNoTWFpbkV2ZW50UGFzcyhIaXRQYXRoVHJhY2tlci5rdDozODcpCglhdCBhbmRyb2lkeC5jb21wb3NlLnVpLmlucHV0LnBvaW50ZXIuTm9kZS5kaXNwYXRjaE1haW5FdmVudFBhc3MoSGl0UGF0aFRyYWNrZXIua3Q6MzczKQoJYXQgYW5kcm9pZHguY29tcG9zZS51aS5pbnB1dC5wb2ludGVyLk5vZGVQYXJlbnQuZGlzcGF0Y2hNYWluRXZlbnRQYXNzKEhpdFBhdGhUcmFja2VyLmt0OjIyOSkKCWF0IGFuZHJvaWR4LmNvbXBvc2UudWkuaW5wdXQucG9pbnRlci5IaXRQYXRoVHJhY2tlci5kaXNwYXRjaENoYW5nZXMoSGl0UGF0aFRyYWNrZXIua3Q6MTQ0KQoJYXQgYW5kcm9pZHguY29tcG9zZS51aS5pbnB1dC5wb2ludGVyLlBvaW50ZXJJbnB1dEV2ZW50UHJvY2Vzc29yLnByb2Nlc3MtQkl6WGZvZyhQb2ludGVySW5wdXRFdmVudFByb2Nlc3Nvci5rdDoxMjApCglhdCBhbmRyb2lkeC5jb21wb3NlLnVpLnBsYXRmb3JtLkFuZHJvaWRDb21wb3NlVmlldy5zZW5kTW90aW9uRXZlbnQtOGlBc1ZUYyhBbmRyb2lkQ29tcG9zZVZpZXcuYW5kcm9pZC5rdDoxOTk0KQoJYXQgYW5kcm9pZHguY29tcG9zZS51aS5wbGF0Zm9ybS5BbmRyb2lkQ29tcG9zZVZpZXcuaGFuZGxlTW90aW9uRXZlbnQtOGlBc1ZUYyhBbmRyb2lkQ29tcG9zZVZpZXcuYW5kcm9pZC5rdDoxOTQ1KQoJYXQgYW5kcm9pZHguY29tcG9zZS51aS5wbGF0Zm9ybS5BbmRyb2lkQ29tcG9zZVZpZXcuZGlzcGF0Y2hUb3VjaEV2ZW50KEFuZHJvaWRDb21wb3NlVmlldy5hbmRyb2lkLmt0OjE4MjkpCglhdCBhbmRyb2lkLnZpZXcuVmlld0dyb3VwLmRpc3BhdGNoVHJhbnNmb3JtZWRUb3VjaEV2ZW50KFZpZXdHcm91cC5qYXZhOjMxMjIpCglhdCBhbmRyb2lkLnZpZXcuVmlld0dyb3VwLmRpc3BhdGNoVG91Y2hFdmVudChWaWV3R3JvdXAuamF2YToyODAzKQoJYXQgYW5kcm9pZC52aWV3LlZpZXdHcm91cC5kaXNwYXRjaFRyYW5zZm9ybWVkVG91Y2hFdmVudChWaWV3R3JvdXAuamF2YTozMTIyKQoJYXQgYW5kcm9pZC52aWV3LlZpZXdHcm91cC5kaXNwYXRjaFRvdWNoRXZlbnQoVmlld0dyb3VwLmphdmE6MjgwMykKCWF0IGFuZHJvaWQudmlldy5WaWV3R3JvdXAuZGlzcGF0Y2hUcmFuc2Zvcm1lZFRvdWNoRXZlbnQoVmlld0dyb3VwLmphdmE6MzEyMikKCWF0IGFuZHJvaWQudmlldy5WaWV3R3JvdXAuZGlzcGF0Y2hUb3VjaEV2ZW50KFZpZXdHcm91cC5qYXZhOjI4MDMpCglhdCBhbmRyb2lkLnZpZXcuVmlld0dyb3VwLmRpc3BhdGNoVHJhbnNmb3JtZWRUb3VjaEV2ZW50KFZpZXdHcm91cC5qYXZhOjMxMjIpCglhdCBhbmRyb2lkLnZpZXcuVmlld0dyb3VwLmRpc3BhdGNoVG91Y2hFdmVudChWaWV3R3JvdXAuamF2YToyODAzKQoJYXQgY29tLmFuZHJvaWQuaW50ZXJuYWwucG9saWN5LkRlY29yVmlldy5zdXBlckRpc3BhdGNoVG91Y2hFdmVudChEZWNvclZpZXcuamF2YTo0NTgpCglhdCBjb20uYW5kcm9pZC5pbnRlcm5hbC5wb2xpY3kuUGhvbmVXaW5kb3cuc3VwZXJEaXNwYXRjaFRvdWNoRXZlbnQoUGhvbmVXaW5kb3cuamF2YToxOTgwKQoJYXQgYW5kcm9pZC5hcHAuQWN0aXZpdHkuZGlzcGF0Y2hUb3VjaEV2ZW50KEFjdGl2aXR5LmphdmE6NDUzMykKCWF0IGNvbS5hbmRyb2lkLmludGVybmFsLnBvbGljeS5EZWNvclZpZXcuZGlzcGF0Y2hUb3VjaEV2ZW50KERlY29yVmlldy5qYXZhOjQxNikKCWF0IGFuZHJvaWQudmlldy5WaWV3LmRpc3BhdGNoUG9pbnRlckV2ZW50KFZpZXcuamF2YToxNjcyOSkKCWF0IGFuZHJvaWQudmlldy5WaWV3Um9vdEltcGwkVmlld1Bvc3RJbWVJbnB1dFN0YWdlLnByb2Nlc3NQb2ludGVyRXZlbnQoVmlld1Jvb3RJbXBsLmphdmE6Nzk0NykKCWF0IGFuZHJvaWQudmlldy5WaWV3Um9vdEltcGwkVmlld1Bvc3RJbWVJbnB1dFN0YWdlLm9uUHJvY2VzcyhWaWV3Um9vdEltcGwuamF2YTo3NzEwKQoJYXQgYW5kcm9pZC52aWV3LlZpZXdSb290SW1wbCRJbnB1dFN0YWdlLmRlbGl2ZXIoVmlld1Jvb3RJbXBsLmphdmE6NzEwNikKCWF0IGFuZHJvaWQudmlldy5WaWV3Um9vdEltcGwkSW5wdXRTdGFnZS5vbkRlbGl2ZXJUb05leHQoVmlld1Jvb3RJbXBsLmphdmE6NzE2MykKCWF0IGFuZHJvaWQudmlldy5WaWV3Um9vdEltcGwkSW5wdXRTdGFnZS5mb3J3YXJkKFZpZXdSb290SW1wbC5qYXZhOjcxMjkpCglhdCBhbmRyb2lkLnZpZXcuVmlld1Jvb3RJbXBsJEFzeW5jSW5wdXRTdGFnZS5mb3J3YXJkKFZpZXdSb290SW1wbC5qYXZhOjcyOTUpCglhdCBhbmRyb2lkLnZpZXcuVmlld1Jvb3RJbXBsJElucHV0U3RhZ2UuYXBwbHkoVmlld1Jvb3RJbXBsLmphdmE6NzEzNykKCWF0IGFuZHJvaWQudmlldy5WaWV3Um9vdEltcGwkQXN5bmNJbnB1dFN0YWdlLmFwcGx5KFZpZXdSb290SW1wbC5qYXZhOjczNTIpCglhdCBhbmRyb2lkLnZpZXcuVmlld1Jvb3RJbXBsJElucHV0U3RhZ2UuZGVsaXZlcihWaWV3Um9vdEltcGwuamF2YTo3MTEwKQoJYXQgYW5kcm9pZC52aWV3LlZpZXdSb290SW1wbCRJbnB1dFN0YWdlLm9uRGVsaXZlclRvTmV4dChWaWV3Um9vdEltcGwuamF2YTo3MTYzKQoJYXQgYW5kcm9pZC52aWV3LlZpZXdSb290SW1wbCRJbnB1dFN0YWdlLmZvcndhcmQoVmlld1Jvb3RJbXBsLmphdmE6NzEyOSkKCWF0IGFuZHJvaWQudmlldy5WaWV3Um9vdEltcGwkSW5wdXRTdGFnZS5hcHBseShWaWV3Um9vdEltcGwuamF2YTo3MTM3KQoJYXQgYW5kcm9pZC52aWV3LlZpZXdSb290SW1wbCRJbnB1dFN0YWdlLmRlbGl2ZXIoVmlld1Jvb3RJbXBsLmphdmE6NzExMCkKCWF0IGFuZHJvaWQudmlldy5WaWV3Um9vdEltcGwuZGVsaXZlcklucHV0RXZlbnQoVmlld1Jvb3RJbXBsLmphdmE6MTAyMTQpCglhdCBhbmRyb2lkLnZpZXcuVmlld1Jvb3RJbXBsLmRvUHJvY2Vzc0lucHV0RXZlbnRzKFZpZXdSb290SW1wbC5qYXZhOjEwMTY1KQoJYXQgYW5kcm9pZC52aWV3LlZpZXdSb290SW1wbC5lbnF1ZXVlSW5wdXRFdmVudChWaWV3Um9vdEltcGwuamF2YToxMDEzNCkKCWF0IGFuZHJvaWQudmlldy5WaWV3Um9vdEltcGwkV2luZG93SW5wdXRFdmVudFJlY2VpdmVyLm9uSW5wdXRFdmVudChWaWV3Um9vdEltcGwuamF2YToxMDM1NikKCWF0IGFuZHJvaWQudmlldy5JbnB1dEV2ZW50UmVjZWl2ZXIuZGlzcGF0Y2hJbnB1dEV2ZW50KElucHV0RXZlbnRSZWNlaXZlci5qYXZhOjI5NSkKCWF0IGFuZHJvaWQub3MuTWVzc2FnZVF1ZXVlLm5hdGl2ZVBvbGxPbmNlKE5hdGl2ZSBNZXRob2QpCglhdCBhbmRyb2lkLm9zLk1lc3NhZ2VRdWV1ZS5uZXh0KE1lc3NhZ2VRdWV1ZS5qYXZhOjM0NikKCWF0IGFuZHJvaWQub3MuTG9vcGVyLmxvb3BPbmNlKExvb3Blci5qYXZhOjE4OSkKCWF0IGFuZHJvaWQub3MuTG9vcGVyLmxvb3AoTG9vcGVyLmphdmE6MzE3KQoJYXQgYW5kcm9pZC5hcHAuQWN0aXZpdHlUaHJlYWQubWFpbihBY3Rpdml0eVRocmVhZC5qYXZhOjg3MDUpCglhdCBqYXZhLmxhbmcucmVmbGVjdC5NZXRob2QuaW52b2tlKE5hdGl2ZSBNZXRob2QpCglhdCBjb20uYW5kcm9pZC5pbnRlcm5hbC5vcy5SdW50aW1lSW5pdCRNZXRob2RBbmRBcmdzQ2FsbGVyLnJ1bihSdW50aW1lSW5pdC5qYXZhOjU4MCkKCWF0IGNvbS5hbmRyb2lkLmludGVybmFsLm9zLlp5Z290ZUluaXQubWFpbihaeWdvdGVJbml0LmphdmE6ODg2KQo=",
                            ViolationPenalty.Death
                        )
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