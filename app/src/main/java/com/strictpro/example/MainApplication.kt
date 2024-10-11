package com.strictpro.example

import android.app.Application
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy
import android.util.Log
import androidx.core.content.ContextCompat
import com.kirillr.strictmodehelper.StrictModeCompat
import com.strictpro.StrictPro
import com.strictpro.penalty.ViolationPenalty

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

//        setupStrictMode()
        setupStrictPro()
//        setupSrictCompat()
    }

    private fun setupStrictPro() {
        StrictPro.setVmPolicy(
            StrictPro.VmPolicy.Builder()
                .detectAll()
                .penaltyDialog()
                .penaltyLog()
                .build()
        )

        StrictPro.setThreadPolicy(
            StrictPro.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .penaltyDropBox()
                .penaltyDialog()
                .penaltyFlashScreen()
                .penaltyDeathOnNetwork()
//                .penaltyDeath()
                .penaltyListener(ContextCompat.getMainExecutor(this)) { violation ->
                    Log.e("StrictPro", "Detected ThreadPolicy violation: $violation")
                }
                .setWhiteList {
//                    base64(
//                        "YW5kcm9pZC5vcy5zdHJpY3Rtb2RlLkRpc2tSZWFkVmlvbGF0aW9uCglhdCBhbmRyb2lkLm9zLlN0cmljdE1vZGUkQW5kcm9pZEJsb2NrR3VhcmRQb2xpY3kub25SZWFkRnJvbURpc2soU3RyaWN0TW9kZS5qYXZhOjE2ODMpCglhdCBhbmRyb2lkLmFwcC5TaGFyZWRQcmVmZXJlbmNlc0ltcGwuYXdhaXRMb2FkZWRMb2NrZWQoU2hhcmVkUHJlZmVyZW5jZXNJbXBsLmphdmE6MjgzKQoJYXQgYW5kcm9pZC5hcHAuU2hhcmVkUHJlZmVyZW5jZXNJbXBsLmVkaXQoU2hhcmVkUHJlZmVyZW5jZXNJbXBsLmphdmE6Mzc2KQoJYXQgY29tLnN0cmljdHByby5leGFtcGxlLk1haW5BY3Rpdml0eSRvbkNyZWF0ZSQxJDEkMSQxJDEkMS5pbnZva2UoTWFpbkFjdGl2aXR5Lmt0OjMyKQoJYXQgY29tLnN0cmljdHByby5leGFtcGxlLk1haW5BY3Rpdml0eSRvbkNyZWF0ZSQxJDEkMSQxJDEkMS5pbnZva2UoTWFpbkFjdGl2aXR5Lmt0OjMwKQoJYXQgYW5kcm9pZHguY29tcG9zZS5mb3VuZGF0aW9uLkNsaWNrYWJsZVBvaW50ZXJJbnB1dE5vZGUkcG9pbnRlcklucHV0JDMuaW52b2tlLWstNGxRME0oQ2xpY2thYmxlLmt0Ojk4NykKCWF0IGFuZHJvaWR4LmNvbXBvc2UuZm91bmRhdGlvbi5DbGlja2FibGVQb2ludGVySW5wdXROb2RlJHBvaW50ZXJJbnB1dCQzLmludm9rZShDbGlja2FibGUua3Q6OTgxKQoJYXQgYW5kcm9pZHguY29tcG9zZS5mb3VuZGF0aW9uLmdlc3R1cmVzLlRhcEdlc3R1cmVEZXRlY3Rvckt0JGRldGVjdFRhcEFuZFByZXNzJDIkMS5pbnZva2VTdXNwZW5kKFRhcEdlc3R1cmVEZXRlY3Rvci5rdDoyNTUpCglhdCBrb3RsaW4uY29yb3V0aW5lcy5qdm0uaW50ZXJuYWwuQmFzZUNvbnRpbnVhdGlvbkltcGwucmVzdW1lV2l0aChDb250aW51YXRpb25JbXBsLmt0OjMzKQoJYXQga290bGlueC5jb3JvdXRpbmVzLkRpc3BhdGNoZWRUYXNrS3QucmVzdW1lKERpc3BhdGNoZWRUYXNrLmt0OjE3OSkKCWF0IGtvdGxpbnguY29yb3V0aW5lcy5EaXNwYXRjaGVkVGFza0t0LmRpc3BhdGNoKERpc3BhdGNoZWRUYXNrLmt0OjE2OCkKCWF0IGtvdGxpbnguY29yb3V0aW5lcy5DYW5jZWxsYWJsZUNvbnRpbnVhdGlvbkltcGwuZGlzcGF0Y2hSZXN1bWUoQ2FuY2VsbGFibGVDb250aW51YXRpb25JbXBsLmt0OjQ3NCkKCWF0IGtvdGxpbnguY29yb3V0aW5lcy5DYW5jZWxsYWJsZUNvbnRpbnVhdGlvbkltcGwucmVzdW1lSW1wbChDYW5jZWxsYWJsZUNvbnRpbnVhdGlvbkltcGwua3Q6NTA4KQoJYXQga290bGlueC5jb3JvdXRpbmVzLkNhbmNlbGxhYmxlQ29udGludWF0aW9uSW1wbC5yZXN1bWVJbXBsJGRlZmF1bHQoQ2FuY2VsbGFibGVDb250aW51YXRpb25JbXBsLmt0OjQ5NykKCWF0IGtvdGxpbnguY29yb3V0aW5lcy5DYW5jZWxsYWJsZUNvbnRpbnVhdGlvbkltcGwucmVzdW1lV2l0aChDYW5jZWxsYWJsZUNvbnRpbnVhdGlvbkltcGwua3Q6MzY4KQoJYXQgYW5kcm9pZHguY29tcG9zZS51aS5pbnB1dC5wb2ludGVyLlN1c3BlbmRpbmdQb2ludGVySW5wdXRNb2RpZmllck5vZGVJbXBsJFBvaW50ZXJFdmVudEhhbmRsZXJDb3JvdXRpbmUub2ZmZXJQb2ludGVyRXZlbnQoU3VzcGVuZGluZ1BvaW50ZXJJbnB1dEZpbHRlci5rdDo2NjUpCglhdCBhbmRyb2lkeC5jb21wb3NlLnVpLmlucHV0LnBvaW50ZXIuU3VzcGVuZGluZ1BvaW50ZXJJbnB1dE1vZGlmaWVyTm9kZUltcGwuZGlzcGF0Y2hQb2ludGVyRXZlbnQoU3VzcGVuZGluZ1BvaW50ZXJJbnB1dEZpbHRlci5rdDo1NDQpCglhdCBhbmRyb2lkeC5jb21wb3NlLnVpLmlucHV0LnBvaW50ZXIuU3VzcGVuZGluZ1BvaW50ZXJJbnB1dE1vZGlmaWVyTm9kZUltcGwub25Qb2ludGVyRXZlbnQtSDBwUnVvWShTdXNwZW5kaW5nUG9pbnRlcklucHV0RmlsdGVyLmt0OjU2NikKCWF0IGFuZHJvaWR4LmNvbXBvc2UuZm91bmRhdGlvbi5BYnN0cmFjdENsaWNrYWJsZVBvaW50ZXJJbnB1dE5vZGUub25Qb2ludGVyRXZlbnQtSDBwUnVvWShDbGlja2FibGUua3Q6OTQ3KQoJYXQgYW5kcm9pZHguY29tcG9zZS5mb3VuZGF0aW9uLkFic3RyYWN0Q2xpY2thYmxlTm9kZS5vblBvaW50ZXJFdmVudC1IMHBSdW9ZKENsaWNrYWJsZS5rdDo3OTUpCglhdCBhbmRyb2lkeC5jb21wb3NlLnVpLmlucHV0LnBvaW50ZXIuTm9kZS5kaXNwYXRjaE1haW5FdmVudFBhc3MoSGl0UGF0aFRyYWNrZXIua3Q6MzE3KQoJYXQgYW5kcm9pZHguY29tcG9zZS51aS5pbnB1dC5wb2ludGVyLk5vZGUuZGlzcGF0Y2hNYWluRXZlbnRQYXNzKEhpdFBhdGhUcmFja2VyLmt0OjMwMykKCWF0IGFuZHJvaWR4LmNvbXBvc2UudWkuaW5wdXQucG9pbnRlci5Ob2RlLmRpc3BhdGNoTWFpbkV2ZW50UGFzcyhIaXRQYXRoVHJhY2tlci5rdDozMDMpCglhdCBhbmRyb2lkeC5jb21wb3NlLnVpLmlucHV0LnBvaW50ZXIuTm9kZVBhcmVudC5kaXNwYXRjaE1haW5FdmVudFBhc3MoSGl0UGF0aFRyYWNrZXIua3Q6MTg1KQoJYXQgYW5kcm9pZHguY29tcG9zZS51aS5pbnB1dC5wb2ludGVyLkhpdFBhdGhUcmFja2VyLmRpc3BhdGNoQ2hhbmdlcyhIaXRQYXRoVHJhY2tlci5rdDoxMDQpCglhdCBhbmRyb2lkeC5jb21wb3NlLnVpLmlucHV0LnBvaW50ZXIuUG9pbnRlcklucHV0RXZlbnRQcm9jZXNzb3IucHJvY2Vzcy1CSXpYZm9nKFBvaW50ZXJJbnB1dEV2ZW50UHJvY2Vzc29yLmt0OjExMykKCWF0IGFuZHJvaWR4LmNvbXBvc2UudWkucGxhdGZvcm0uQW5kcm9pZENvbXBvc2VWaWV3LnNlbmRNb3Rpb25FdmVudC04aUFzVlRjKEFuZHJvaWRDb21wb3NlVmlldy5hbmRyb2lkLmt0OjE1NzYpCglhdCBhbmRyb2lkeC5jb21wb3NlLnVpLnBsYXRmb3JtLkFuZHJvaWRDb21wb3NlVmlldy5oYW5kbGVNb3Rpb25FdmVudC04aUFzVlRjKEFuZHJvaWRDb21wb3NlVmlldy5hbmRyb2lkLmt0OjE1MjcpCglhdCBhbmRyb2lkeC5jb21wb3NlLnVpLnBsYXRmb3JtLkFuZHJvaWRDb21wb3NlVmlldy5kaXNwYXRjaFRvdWNoRXZlbnQoQW5kcm9pZENvbXBvc2VWaWV3LmFuZHJvaWQua3Q6MTQ2NikKCWF0IGFuZHJvaWQudmlldy5WaWV3R3JvdXAuZGlzcGF0Y2hUcmFuc2Zvcm1lZFRvdWNoRXZlbnQoVmlld0dyb3VwLmphdmE6MzEyMikKCWF0IGFuZHJvaWQudmlldy5WaWV3R3JvdXAuZGlzcGF0Y2hUb3VjaEV2ZW50KFZpZXdHcm91cC5qYXZhOjI4MDMpCglhdCBhbmRyb2lkLnZpZXcuVmlld0dyb3VwLmRpc3BhdGNoVHJhbnNmb3JtZWRUb3VjaEV2ZW50KFZpZXdHcm91cC5qYXZhOjMxMjIpCglhdCBhbmRyb2lkLnZpZXcuVmlld0dyb3VwLmRpc3BhdGNoVG91Y2hFdmVudChWaWV3R3JvdXAuamF2YToyODAzKQoJYXQgYW5kcm9pZC52aWV3LlZpZXdHcm91cC5kaXNwYXRjaFRyYW5zZm9ybWVkVG91Y2hFdmVudChWaWV3R3JvdXAuamF2YTozMTIyKQoJYXQgYW5kcm9pZC52aWV3LlZpZXdHcm91cC5kaXNwYXRjaFRvdWNoRXZlbnQoVmlld0dyb3VwLmphdmE6MjgwMykKCWF0IGFuZHJvaWQudmlldy5WaWV3R3JvdXAuZGlzcGF0Y2hUcmFuc2Zvcm1lZFRvdWNoRXZlbnQoVmlld0dyb3VwLmphdmE6MzEyMikKCWF0IGFuZHJvaWQudmlldy5WaWV3R3JvdXAuZGlzcGF0Y2hUb3VjaEV2ZW50KFZpZXdHcm91cC5qYXZhOjI4MDMpCglhdCBjb20uYW5kcm9pZC5pbnRlcm5hbC5wb2xpY3kuRGVjb3JWaWV3LnN1cGVyRGlzcGF0Y2hUb3VjaEV2ZW50KERlY29yVmlldy5qYXZhOjQ1OCkKCWF0IGNvbS5hbmRyb2lkLmludGVybmFsLnBvbGljeS5QaG9uZVdpbmRvdy5zdXBlckRpc3BhdGNoVG91Y2hFdmVudChQaG9uZVdpbmRvdy5qYXZhOjE5ODApCglhdCBhbmRyb2lkLmFwcC5BY3Rpdml0eS5kaXNwYXRjaFRvdWNoRXZlbnQoQWN0aXZpdHkuamF2YTo0NTMzKQoJYXQgY29tLmFuZHJvaWQuaW50ZXJuYWwucG9saWN5LkRlY29yVmlldy5kaXNwYXRjaFRvdWNoRXZlbnQoRGVjb3JWaWV3LmphdmE6NDE2KQoJYXQgYW5kcm9pZC52aWV3LlZpZXcuZGlzcGF0Y2hQb2ludGVyRXZlbnQoVmlldy5qYXZhOjE2NzI5KQoJYXQgYW5kcm9pZC52aWV3LlZpZXdSb290SW1wbCRWaWV3UG9zdEltZUlucHV0U3RhZ2UucHJvY2Vzc1BvaW50ZXJFdmVudChWaWV3Um9vdEltcGwuamF2YTo3OTQ3KQoJYXQgYW5kcm9pZC52aWV3LlZpZXdSb290SW1wbCRWaWV3UG9zdEltZUlucHV0U3RhZ2Uub25Qcm9jZXNzKFZpZXdSb290SW1wbC5qYXZhOjc3MTApCglhdCBhbmRyb2lkLnZpZXcuVmlld1Jvb3RJbXBsJElucHV0U3RhZ2UuZGVsaXZlcihWaWV3Um9vdEltcGwuamF2YTo3MTA2KQoJYXQgYW5kcm9pZC52aWV3LlZpZXdSb290SW1wbCRJbnB1dFN0YWdlLm9uRGVsaXZlclRvTmV4dChWaWV3Um9vdEltcGwuamF2YTo3MTYzKQoJYXQgYW5kcm9pZC52aWV3LlZpZXdSb290SW1wbCRJbnB1dFN0YWdlLmZvcndhcmQoVmlld1Jvb3RJbXBsLmphdmE6NzEyOSkKCWF0IGFuZHJvaWQudmlldy5WaWV3Um9vdEltcGwkQXN5bmNJbnB1dFN0YWdlLmZvcndhcmQoVmlld1Jvb3RJbXBsLmphdmE6NzI5NSkKCWF0IGFuZHJvaWQudmlldy5WaWV3Um9vdEltcGwkSW5wdXRTdGFnZS5hcHBseShWaWV3Um9vdEltcGwuamF2YTo3MTM3KQoJYXQgYW5kcm9pZC52aWV3LlZpZXdSb290SW1wbCRBc3luY0lucHV0U3RhZ2UuYXBwbHkoVmlld1Jvb3RJbXBsLmphdmE6NzM1MikKCWF0IGFuZHJvaWQudmlldy5WaWV3Um9vdEltcGwkSW5wdXRTdGFnZS5kZWxpdmVyKFZpZXdSb290SW1wbC5qYXZhOjcxMTApCglhdCBhbmRyb2lkLnZpZXcuVmlld1Jvb3RJbXBsJElucHV0U3RhZ2Uub25EZWxpdmVyVG9OZXh0KFZpZXdSb290SW1wbC5qYXZhOjcxNjMpCglhdCBhbmRyb2lkLnZpZXcuVmlld1Jvb3RJbXBsJElucHV0U3RhZ2UuZm9yd2FyZChWaWV3Um9vdEltcGwuamF2YTo3MTI5KQoJYXQgYW5kcm9pZC52aWV3LlZpZXdSb290SW1wbCRJbnB1dFN0YWdlLmFwcGx5KFZpZXdSb290SW1wbC5qYXZhOjcxMzcpCglhdCBhbmRyb2lkLnZpZXcuVmlld1Jvb3RJbXBsJElucHV0U3RhZ2UuZGVsaXZlcihWaWV3Um9vdEltcGwuamF2YTo3MTEwKQoJYXQgYW5kcm9pZC52aWV3LlZpZXdSb290SW1wbC5kZWxpdmVySW5wdXRFdmVudChWaWV3Um9vdEltcGwuamF2YToxMDIxNCkKCWF0IGFuZHJvaWQudmlldy5WaWV3Um9vdEltcGwuZG9Qcm9jZXNzSW5wdXRFdmVudHMoVmlld1Jvb3RJbXBsLmphdmE6MTAxNjUpCglhdCBhbmRyb2lkLnZpZXcuVmlld1Jvb3RJbXBsLmVucXVldWVJbnB1dEV2ZW50KFZpZXdSb290SW1wbC5qYXZhOjEwMTM0KQoJYXQgYW5kcm9pZC52aWV3LlZpZXdSb290SW1wbCRXaW5kb3dJbnB1dEV2ZW50UmVjZWl2ZXIub25JbnB1dEV2ZW50KFZpZXdSb290SW1wbC5qYXZhOjEwMzU2KQoJYXQgYW5kcm9pZC52aWV3LklucHV0RXZlbnRSZWNlaXZlci5kaXNwYXRjaElucHV0RXZlbnQoSW5wdXRFdmVudFJlY2VpdmVyLmphdmE6Mjk1KQoJYXQgYW5kcm9pZC5vcy5NZXNzYWdlUXVldWUubmF0aXZlUG9sbE9uY2UoTmF0aXZlIE1ldGhvZCkKCWF0IGFuZHJvaWQub3MuTWVzc2FnZVF1ZXVlLm5leHQoTWVzc2FnZVF1ZXVlLmphdmE6MzQ2KQoJYXQgYW5kcm9pZC5vcy5Mb29wZXIubG9vcE9uY2UoTG9vcGVyLmphdmE6MTg5KQoJYXQgYW5kcm9pZC5vcy5Mb29wZXIubG9vcChMb29wZXIuamF2YTozMTcpCglhdCBhbmRyb2lkLmFwcC5BY3Rpdml0eVRocmVhZC5tYWluKEFjdGl2aXR5VGhyZWFkLmphdmE6ODcwNSkKCWF0IGphdmEubGFuZy5yZWZsZWN0Lk1ldGhvZC5pbnZva2UoTmF0aXZlIE1ldGhvZCkKCWF0IGNvbS5hbmRyb2lkLmludGVybmFsLm9zLlJ1bnRpbWVJbml0JE1ldGhvZEFuZEFyZ3NDYWxsZXIucnVuKFJ1bnRpbWVJbml0LmphdmE6NTgwKQoJYXQgY29tLmFuZHJvaWQuaW50ZXJuYWwub3MuWnlnb3RlSW5pdC5tYWluKFp5Z290ZUluaXQuamF2YTo4ODYpCg==",
//                        ViolationPenalty.DropBox,
//                    )
//                    base64(
//                        "YW5kcm9pZC5vcy5zdHJpY3Rtb2RlLkRpc2tSZWFkVmlvbGF0aW9uCglhdCBhbmRyb2lkLm9zLlN0cmljdE1vZGUkQW5kcm9pZEJsb2NrR3VhcmRQb2xpY3kub25SZWFkRnJvbURpc2soU3RyaWN0TW9kZS5qYXZhOjE2ODMpCglhdCBsaWJjb3JlLmlvLkJsb2NrR3VhcmRPcy5hY2Nlc3MoQmxvY2tHdWFyZE9zLmphdmE6NzQpCglhdCBsaWJjb3JlLmlvLkZvcndhcmRpbmdPcy5hY2Nlc3MoRm9yd2FyZGluZ09zLmphdmE6MTI4KQoJYXQgYW5kcm9pZC5hcHAuQWN0aXZpdHlUaHJlYWQkQW5kcm9pZE9zLmFjY2VzcyhBY3Rpdml0eVRocmVhZC5qYXZhOjg1ODIpCglhdCBqYXZhLmlvLlVuaXhGaWxlU3lzdGVtLmNoZWNrQWNjZXNzKFVuaXhGaWxlU3lzdGVtLmphdmE6MzMyKQoJYXQgamF2YS5pby5GaWxlLmV4aXN0cyhGaWxlLmphdmE6ODI5KQoJYXQgYW5kcm9pZC5hcHAuU2hhcmVkUHJlZmVyZW5jZXNJbXBsLndyaXRlVG9GaWxlKFNoYXJlZFByZWZlcmVuY2VzSW1wbC5qYXZhOjc0NikKCWF0IGFuZHJvaWQuYXBwLlNoYXJlZFByZWZlcmVuY2VzSW1wbC4tJCROZXN0JG13cml0ZVRvRmlsZShVbmtub3duIFNvdXJjZTowKQoJYXQgYW5kcm9pZC5hcHAuU2hhcmVkUHJlZmVyZW5jZXNJbXBsJDEucnVuKFNoYXJlZFByZWZlcmVuY2VzSW1wbC5qYXZhOjY4MCkKCWF0IGFuZHJvaWQuYXBwLlNoYXJlZFByZWZlcmVuY2VzSW1wbC5lbnF1ZXVlRGlza1dyaXRlKFNoYXJlZFByZWZlcmVuY2VzSW1wbC5qYXZhOjY5OSkKCWF0IGFuZHJvaWQuYXBwLlNoYXJlZFByZWZlcmVuY2VzSW1wbC4tJCROZXN0JG1lbnF1ZXVlRGlza1dyaXRlKFVua25vd24gU291cmNlOjApCglhdCBhbmRyb2lkLmFwcC5TaGFyZWRQcmVmZXJlbmNlc0ltcGwkRWRpdG9ySW1wbC5jb21taXQoU2hhcmVkUHJlZmVyZW5jZXNJbXBsLmphdmE6NjEyKQoJYXQgY29tLnN0cmljdHByby5leGFtcGxlLk1haW5BY3Rpdml0eSRvbkNyZWF0ZSQxJDEkMSQxJDEkMS5pbnZva2UoTWFpbkFjdGl2aXR5Lmt0OjM0KQoJYXQgY29tLnN0cmljdHByby5leGFtcGxlLk1haW5BY3Rpdml0eSRvbkNyZWF0ZSQxJDEkMSQxJDEkMS5pbnZva2UoTWFpbkFjdGl2aXR5Lmt0OjMwKQoJYXQgYW5kcm9pZHguY29tcG9zZS5mb3VuZGF0aW9uLkNsaWNrYWJsZVBvaW50ZXJJbnB1dE5vZGUkcG9pbnRlcklucHV0JDMuaW52b2tlLWstNGxRME0oQ2xpY2thYmxlLmt0Ojk4NykKCWF0IGFuZHJvaWR4LmNvbXBvc2UuZm91bmRhdGlvbi5DbGlja2FibGVQb2ludGVySW5wdXROb2RlJHBvaW50ZXJJbnB1dCQzLmludm9rZShDbGlja2FibGUua3Q6OTgxKQoJYXQgYW5kcm9pZHguY29tcG9zZS5mb3VuZGF0aW9uLmdlc3R1cmVzLlRhcEdlc3R1cmVEZXRlY3Rvckt0JGRldGVjdFRhcEFuZFByZXNzJDIkMS5pbnZva2VTdXNwZW5kKFRhcEdlc3R1cmVEZXRlY3Rvci5rdDoyNTUpCglhdCBrb3RsaW4uY29yb3V0aW5lcy5qdm0uaW50ZXJuYWwuQmFzZUNvbnRpbnVhdGlvbkltcGwucmVzdW1lV2l0aChDb250aW51YXRpb25JbXBsLmt0OjMzKQoJYXQga290bGlueC5jb3JvdXRpbmVzLkRpc3BhdGNoZWRUYXNrS3QucmVzdW1lKERpc3BhdGNoZWRUYXNrLmt0OjE3OSkKCWF0IGtvdGxpbnguY29yb3V0aW5lcy5EaXNwYXRjaGVkVGFza0t0LmRpc3BhdGNoKERpc3BhdGNoZWRUYXNrLmt0OjE2OCkKCWF0IGtvdGxpbnguY29yb3V0aW5lcy5DYW5jZWxsYWJsZUNvbnRpbnVhdGlvbkltcGwuZGlzcGF0Y2hSZXN1bWUoQ2FuY2VsbGFibGVDb250aW51YXRpb25JbXBsLmt0OjQ3NCkKCWF0IGtvdGxpbnguY29yb3V0aW5lcy5DYW5jZWxsYWJsZUNvbnRpbnVhdGlvbkltcGwucmVzdW1lSW1wbChDYW5jZWxsYWJsZUNvbnRpbnVhdGlvbkltcGwua3Q6NTA4KQoJYXQga290bGlueC5jb3JvdXRpbmVzLkNhbmNlbGxhYmxlQ29udGludWF0aW9uSW1wbC5yZXN1bWVJbXBsJGRlZmF1bHQoQ2FuY2VsbGFibGVDb250aW51YXRpb25JbXBsLmt0OjQ5NykKCWF0IGtvdGxpbnguY29yb3V0aW5lcy5DYW5jZWxsYWJsZUNvbnRpbnVhdGlvbkltcGwucmVzdW1lV2l0aChDYW5jZWxsYWJsZUNvbnRpbnVhdGlvbkltcGwua3Q6MzY4KQoJYXQgYW5kcm9pZHguY29tcG9zZS51aS5pbnB1dC5wb2ludGVyLlN1c3BlbmRpbmdQb2ludGVySW5wdXRNb2RpZmllck5vZGVJbXBsJFBvaW50ZXJFdmVudEhhbmRsZXJDb3JvdXRpbmUub2ZmZXJQb2ludGVyRXZlbnQoU3VzcGVuZGluZ1BvaW50ZXJJbnB1dEZpbHRlci5rdDo2NjUpCglhdCBhbmRyb2lkeC5jb21wb3NlLnVpLmlucHV0LnBvaW50ZXIuU3VzcGVuZGluZ1BvaW50ZXJJbnB1dE1vZGlmaWVyTm9kZUltcGwuZGlzcGF0Y2hQb2ludGVyRXZlbnQoU3VzcGVuZGluZ1BvaW50ZXJJbnB1dEZpbHRlci5rdDo1NDQpCglhdCBhbmRyb2lkeC5jb21wb3NlLnVpLmlucHV0LnBvaW50ZXIuU3VzcGVuZGluZ1BvaW50ZXJJbnB1dE1vZGlmaWVyTm9kZUltcGwub25Qb2ludGVyRXZlbnQtSDBwUnVvWShTdXNwZW5kaW5nUG9pbnRlcklucHV0RmlsdGVyLmt0OjU2NikKCWF0IGFuZHJvaWR4LmNvbXBvc2UuZm91bmRhdGlvbi5BYnN0cmFjdENsaWNrYWJsZVBvaW50ZXJJbnB1dE5vZGUub25Qb2ludGVyRXZlbnQtSDBwUnVvWShDbGlja2FibGUua3Q6OTQ3KQoJYXQgYW5kcm9pZHguY29tcG9zZS5mb3VuZGF0aW9uLkFic3RyYWN0Q2xpY2thYmxlTm9kZS5vblBvaW50ZXJFdmVudC1IMHBSdW9ZKENsaWNrYWJsZS5rdDo3OTUpCglhdCBhbmRyb2lkeC5jb21wb3NlLnVpLmlucHV0LnBvaW50ZXIuTm9kZS5kaXNwYXRjaE1haW5FdmVudFBhc3MoSGl0UGF0aFRyYWNrZXIua3Q6MzE3KQoJYXQgYW5kcm9pZHguY29tcG9zZS51aS5pbnB1dC5wb2ludGVyLk5vZGUuZGlzcGF0Y2hNYWluRXZlbnRQYXNzKEhpdFBhdGhUcmFja2VyLmt0OjMwMykKCWF0IGFuZHJvaWR4LmNvbXBvc2UudWkuaW5wdXQucG9pbnRlci5Ob2RlLmRpc3BhdGNoTWFpbkV2ZW50UGFzcyhIaXRQYXRoVHJhY2tlci5rdDozMDMpCglhdCBhbmRyb2lkeC5jb21wb3NlLnVpLmlucHV0LnBvaW50ZXIuTm9kZVBhcmVudC5kaXNwYXRjaE1haW5FdmVudFBhc3MoSGl0UGF0aFRyYWNrZXIua3Q6MTg1KQoJYXQgYW5kcm9pZHguY29tcG9zZS51aS5pbnB1dC5wb2ludGVyLkhpdFBhdGhUcmFja2VyLmRpc3BhdGNoQ2hhbmdlcyhIaXRQYXRoVHJhY2tlci5rdDoxMDQpCglhdCBhbmRyb2lkeC5jb21wb3NlLnVpLmlucHV0LnBvaW50ZXIuUG9pbnRlcklucHV0RXZlbnRQcm9jZXNzb3IucHJvY2Vzcy1CSXpYZm9nKFBvaW50ZXJJbnB1dEV2ZW50UHJvY2Vzc29yLmt0OjExMykKCWF0IGFuZHJvaWR4LmNvbXBvc2UudWkucGxhdGZvcm0uQW5kcm9pZENvbXBvc2VWaWV3LnNlbmRNb3Rpb25FdmVudC04aUFzVlRjKEFuZHJvaWRDb21wb3NlVmlldy5hbmRyb2lkLmt0OjE1NzYpCglhdCBhbmRyb2lkeC5jb21wb3NlLnVpLnBsYXRmb3JtLkFuZHJvaWRDb21wb3NlVmlldy5oYW5kbGVNb3Rpb25FdmVudC04aUFzVlRjKEFuZHJvaWRDb21wb3NlVmlldy5hbmRyb2lkLmt0OjE1MjcpCglhdCBhbmRyb2lkeC5jb21wb3NlLnVpLnBsYXRmb3JtLkFuZHJvaWRDb21wb3NlVmlldy5kaXNwYXRjaFRvdWNoRXZlbnQoQW5kcm9pZENvbXBvc2VWaWV3LmFuZHJvaWQua3Q6MTQ2NikKCWF0IGFuZHJvaWQudmlldy5WaWV3R3JvdXAuZGlzcGF0Y2hUcmFuc2Zvcm1lZFRvdWNoRXZlbnQoVmlld0dyb3VwLmphdmE6MzEyMikKCWF0IGFuZHJvaWQudmlldy5WaWV3R3JvdXAuZGlzcGF0Y2hUb3VjaEV2ZW50KFZpZXdHcm91cC5qYXZhOjI4MDMpCglhdCBhbmRyb2lkLnZpZXcuVmlld0dyb3VwLmRpc3BhdGNoVHJhbnNmb3JtZWRUb3VjaEV2ZW50KFZpZXdHcm91cC5qYXZhOjMxMjIpCglhdCBhbmRyb2lkLnZpZXcuVmlld0dyb3VwLmRpc3BhdGNoVG91Y2hFdmVudChWaWV3R3JvdXAuamF2YToyODAzKQoJYXQgYW5kcm9pZC52aWV3LlZpZXdHcm91cC5kaXNwYXRjaFRyYW5zZm9ybWVkVG91Y2hFdmVudChWaWV3R3JvdXAuamF2YTozMTIyKQoJYXQgYW5kcm9pZC52aWV3LlZpZXdHcm91cC5kaXNwYXRjaFRvdWNoRXZlbnQoVmlld0dyb3VwLmphdmE6MjgwMykKCWF0IGFuZHJvaWQudmlldy5WaWV3R3JvdXAuZGlzcGF0Y2hUcmFuc2Zvcm1lZFRvdWNoRXZlbnQoVmlld0dyb3VwLmphdmE6MzEyMikKCWF0IGFuZHJvaWQudmlldy5WaWV3R3JvdXAuZGlzcGF0Y2hUb3VjaEV2ZW50KFZpZXdHcm91cC5qYXZhOjI4MDMpCglhdCBjb20uYW5kcm9pZC5pbnRlcm5hbC5wb2xpY3kuRGVjb3JWaWV3LnN1cGVyRGlzcGF0Y2hUb3VjaEV2ZW50KERlY29yVmlldy5qYXZhOjQ1OCkKCWF0IGNvbS5hbmRyb2lkLmludGVybmFsLnBvbGljeS5QaG9uZVdpbmRvdy5zdXBlckRpc3BhdGNoVG91Y2hFdmVudChQaG9uZVdpbmRvdy5qYXZhOjE5ODApCglhdCBhbmRyb2lkLmFwcC5BY3Rpdml0eS5kaXNwYXRjaFRvdWNoRXZlbnQoQWN0aXZpdHkuamF2YTo0NTMzKQoJYXQgY29tLmFuZHJvaWQuaW50ZXJuYWwucG9saWN5LkRlY29yVmlldy5kaXNwYXRjaFRvdWNoRXZlbnQoRGVjb3JWaWV3LmphdmE6NDE2KQoJYXQgYW5kcm9pZC52aWV3LlZpZXcuZGlzcGF0Y2hQb2ludGVyRXZlbnQoVmlldy5qYXZhOjE2NzI5KQoJYXQgYW5kcm9pZC52aWV3LlZpZXdSb290SW1wbCRWaWV3UG9zdEltZUlucHV0U3RhZ2UucHJvY2Vzc1BvaW50ZXJFdmVudChWaWV3Um9vdEltcGwuamF2YTo3OTQ3KQoJYXQgYW5kcm9pZC52aWV3LlZpZXdSb290SW1wbCRWaWV3UG9zdEltZUlucHV0U3RhZ2Uub25Qcm9jZXNzKFZpZXdSb290SW1wbC5qYXZhOjc3MTApCglhdCBhbmRyb2lkLnZpZXcuVmlld1Jvb3RJbXBsJElucHV0U3RhZ2UuZGVsaXZlcihWaWV3Um9vdEltcGwuamF2YTo3MTA2KQoJYXQgYW5kcm9pZC52aWV3LlZpZXdSb290SW1wbCRJbnB1dFN0YWdlLm9uRGVsaXZlclRvTmV4dChWaWV3Um9vdEltcGwuamF2YTo3MTYzKQoJYXQgYW5kcm9pZC52aWV3LlZpZXdSb290SW1wbCRJbnB1dFN0YWdlLmZvcndhcmQoVmlld1Jvb3RJbXBsLmphdmE6NzEyOSkKCWF0IGFuZHJvaWQudmlldy5WaWV3Um9vdEltcGwkQXN5bmNJbnB1dFN0YWdlLmZvcndhcmQoVmlld1Jvb3RJbXBsLmphdmE6NzI5NSkKCWF0IGFuZHJvaWQudmlldy5WaWV3Um9vdEltcGwkSW5wdXRTdGFnZS5hcHBseShWaWV3Um9vdEltcGwuamF2YTo3MTM3KQoJYXQgYW5kcm9pZC52aWV3LlZpZXdSb290SW1wbCRBc3luY0lucHV0U3RhZ2UuYXBwbHkoVmlld1Jvb3RJbXBsLmphdmE6NzM1MikKCWF0IGFuZHJvaWQudmlldy5WaWV3Um9vdEltcGwkSW5wdXRTdGFnZS5kZWxpdmVyKFZpZXdSb290SW1wbC5qYXZhOjcxMTApCglhdCBhbmRyb2lkLnZpZXcuVmlld1Jvb3RJbXBsJElucHV0U3RhZ2Uub25EZWxpdmVyVG9OZXh0KFZpZXdSb290SW1wbC5qYXZhOjcxNjMpCglhdCBhbmRyb2lkLnZpZXcuVmlld1Jvb3RJbXBsJElucHV0U3RhZ2UuZm9yd2FyZChWaWV3Um9vdEltcGwuamF2YTo3MTI5KQoJYXQgYW5kcm9pZC52aWV3LlZpZXdSb290SW1wbCRJbnB1dFN0YWdlLmFwcGx5KFZpZXdSb290SW1wbC5qYXZhOjcxMzcpCglhdCBhbmRyb2lkLnZpZXcuVmlld1Jvb3RJbXBsJElucHV0U3RhZ2UuZGVsaXZlcihWaWV3Um9vdEltcGwuamF2YTo3MTEwKQoJYXQgYW5kcm9pZC52aWV3LlZpZXdSb290SW1wbC5kZWxpdmVySW5wdXRFdmVudChWaWV3Um9vdEltcGwuamF2YToxMDIxNCkKCWF0IGFuZHJvaWQudmlldy5WaWV3Um9vdEltcGwuZG9Qcm9jZXNzSW5wdXRFdmVudHMoVmlld1Jvb3RJbXBsLmphdmE6MTAxNjUpCglhdCBhbmRyb2lkLnZpZXcuVmlld1Jvb3RJbXBsLmVucXVldWVJbnB1dEV2ZW50KFZpZXdSb290SW1wbC5qYXZhOjEwMTM0KQoJYXQgYW5kcm9pZC52aWV3LlZpZXdSb290SW1wbCRXaW5kb3dJbnB1dEV2ZW50UmVjZWl2ZXIub25JbnB1dEV2ZW50KFZpZXdSb290SW1wbC5qYXZhOjEwMzU2KQoJYXQgYW5kcm9pZC52aWV3LklucHV0RXZlbnRSZWNlaXZlci5kaXNwYXRjaElucHV0RXZlbnQoSW5wdXRFdmVudFJlY2VpdmVyLmphdmE6Mjk1KQoJYXQgYW5kcm9pZC5vcy5NZXNzYWdlUXVldWUubmF0aXZlUG9sbE9uY2UoTmF0aXZlIE1ldGhvZCkKCWF0IGFuZHJvaWQub3MuTWVzc2FnZVF1ZXVlLm5leHQoTWVzc2FnZVF1ZXVlLmphdmE6MzQ2KQoJYXQgYW5kcm9pZC5vcy5Mb29wZXIubG9vcE9uY2UoTG9vcGVyLmphdmE6MTg5KQoJYXQgYW5kcm9pZC5vcy5Mb29wZXIubG9vcChMb29wZXIuamF2YTozMTcpCglhdCBhbmRyb2lkLmFwcC5BY3Rpdml0eVRocmVhZC5tYWluKEFjdGl2aXR5VGhyZWFkLmphdmE6ODcwNSkKCWF0IGphdmEubGFuZy5yZWZsZWN0Lk1ldGhvZC5pbnZva2UoTmF0aXZlIE1ldGhvZCkKCWF0IGNvbS5hbmRyb2lkLmludGVybmFsLm9zLlJ1bnRpbWVJbml0JE1ldGhvZEFuZEFyZ3NDYWxsZXIucnVuKFJ1bnRpbWVJbml0LmphdmE6NTgwKQoJYXQgY29tLmFuZHJvaWQuaW50ZXJuYWwub3MuWnlnb3RlSW5pdC5tYWluKFp5Z290ZUluaXQuamF2YTo4ODYpCg==",
//                        ViolationPenalty.DropBox,
//                    )
//                    base64(
//                        "YW5kcm9pZC5vcy5zdHJpY3Rtb2RlLkRpc2tSZWFkVmlvbGF0aW9uCglhdCBhbmRyb2lkLm9zLlN0cmljdE1vZGUkQW5kcm9pZEJsb2NrR3VhcmRQb2xpY3kub25SZWFkRnJvbURpc2soU3RyaWN0TW9kZS5qYXZhOjE2ODMpCglhdCBsaWJjb3JlLmlvLkJsb2NrR3VhcmRPcy5hY2Nlc3MoQmxvY2tHdWFyZE9zLmphdmE6NzQpCglhdCBsaWJjb3JlLmlvLkZvcndhcmRpbmdPcy5hY2Nlc3MoRm9yd2FyZGluZ09zLmphdmE6MTI4KQoJYXQgYW5kcm9pZC5hcHAuQWN0aXZpdHlUaHJlYWQkQW5kcm9pZE9zLmFjY2VzcyhBY3Rpdml0eVRocmVhZC5qYXZhOjg1ODIpCglhdCBqYXZhLmlvLlVuaXhGaWxlU3lzdGVtLmNoZWNrQWNjZXNzKFVuaXhGaWxlU3lzdGVtLmphdmE6MzMyKQoJYXQgamF2YS5pby5GaWxlLmV4aXN0cyhGaWxlLmphdmE6ODI5KQoJYXQgYW5kcm9pZC5hcHAuQ29udGV4dEltcGwuZW5zdXJlUHJpdmF0ZURpckV4aXN0cyhDb250ZXh0SW1wbC5qYXZhOjgxOSkKCWF0IGFuZHJvaWQuYXBwLkNvbnRleHRJbXBsLmVuc3VyZVByaXZhdGVEaXJFeGlzdHMoQ29udGV4dEltcGwuamF2YTo4MTApCglhdCBhbmRyb2lkLmFwcC5Db250ZXh0SW1wbC5nZXRQcmVmZXJlbmNlc0RpcihDb250ZXh0SW1wbC5qYXZhOjc2NikKCWF0IGFuZHJvaWQuYXBwLkNvbnRleHRJbXBsLmdldFNoYXJlZFByZWZlcmVuY2VzUGF0aChDb250ZXh0SW1wbC5qYXZhOjk5MSkKCWF0IGFuZHJvaWQuYXBwLkNvbnRleHRJbXBsLmdldFNoYXJlZFByZWZlcmVuY2VzKENvbnRleHRJbXBsLmphdmE6NjA2KQoJYXQgYW5kcm9pZC5jb250ZW50LkNvbnRleHRXcmFwcGVyLmdldFNoYXJlZFByZWZlcmVuY2VzKENvbnRleHRXcmFwcGVyLmphdmE6MjIzKQoJYXQgYW5kcm9pZC5hcHAuQWN0aXZpdHkuZ2V0UHJlZmVyZW5jZXMoQWN0aXZpdHkuamF2YTo3NjkxKQoJYXQgY29tLnN0cmljdHByby5leGFtcGxlLk1haW5BY3Rpdml0eSRvbkNyZWF0ZSQxJDEkMSQxJDEkMS5pbnZva2UoTWFpbkFjdGl2aXR5Lmt0OjMxKQoJYXQgY29tLnN0cmljdHByby5leGFtcGxlLk1haW5BY3Rpdml0eSRvbkNyZWF0ZSQxJDEkMSQxJDEkMS5pbnZva2UoTWFpbkFjdGl2aXR5Lmt0OjMwKQoJYXQgYW5kcm9pZHguY29tcG9zZS5mb3VuZGF0aW9uLkNsaWNrYWJsZVBvaW50ZXJJbnB1dE5vZGUkcG9pbnRlcklucHV0JDMuaW52b2tlLWstNGxRME0oQ2xpY2thYmxlLmt0Ojk4NykKCWF0IGFuZHJvaWR4LmNvbXBvc2UuZm91bmRhdGlvbi5DbGlja2FibGVQb2ludGVySW5wdXROb2RlJHBvaW50ZXJJbnB1dCQzLmludm9rZShDbGlja2FibGUua3Q6OTgxKQoJYXQgYW5kcm9pZHguY29tcG9zZS5mb3VuZGF0aW9uLmdlc3R1cmVzLlRhcEdlc3R1cmVEZXRlY3Rvckt0JGRldGVjdFRhcEFuZFByZXNzJDIkMS5pbnZva2VTdXNwZW5kKFRhcEdlc3R1cmVEZXRlY3Rvci5rdDoyNTUpCglhdCBrb3RsaW4uY29yb3V0aW5lcy5qdm0uaW50ZXJuYWwuQmFzZUNvbnRpbnVhdGlvbkltcGwucmVzdW1lV2l0aChDb250aW51YXRpb25JbXBsLmt0OjMzKQoJYXQga290bGlueC5jb3JvdXRpbmVzLkRpc3BhdGNoZWRUYXNrS3QucmVzdW1lKERpc3BhdGNoZWRUYXNrLmt0OjE3OSkKCWF0IGtvdGxpbnguY29yb3V0aW5lcy5EaXNwYXRjaGVkVGFza0t0LmRpc3BhdGNoKERpc3BhdGNoZWRUYXNrLmt0OjE2OCkKCWF0IGtvdGxpbnguY29yb3V0aW5lcy5DYW5jZWxsYWJsZUNvbnRpbnVhdGlvbkltcGwuZGlzcGF0Y2hSZXN1bWUoQ2FuY2VsbGFibGVDb250aW51YXRpb25JbXBsLmt0OjQ3NCkKCWF0IGtvdGxpbnguY29yb3V0aW5lcy5DYW5jZWxsYWJsZUNvbnRpbnVhdGlvbkltcGwucmVzdW1lSW1wbChDYW5jZWxsYWJsZUNvbnRpbnVhdGlvbkltcGwua3Q6NTA4KQoJYXQga290bGlueC5jb3JvdXRpbmVzLkNhbmNlbGxhYmxlQ29udGludWF0aW9uSW1wbC5yZXN1bWVJbXBsJGRlZmF1bHQoQ2FuY2VsbGFibGVDb250aW51YXRpb25JbXBsLmt0OjQ5NykKCWF0IGtvdGxpbnguY29yb3V0aW5lcy5DYW5jZWxsYWJsZUNvbnRpbnVhdGlvbkltcGwucmVzdW1lV2l0aChDYW5jZWxsYWJsZUNvbnRpbnVhdGlvbkltcGwua3Q6MzY4KQoJYXQgYW5kcm9pZHguY29tcG9zZS51aS5pbnB1dC5wb2ludGVyLlN1c3BlbmRpbmdQb2ludGVySW5wdXRNb2RpZmllck5vZGVJbXBsJFBvaW50ZXJFdmVudEhhbmRsZXJDb3JvdXRpbmUub2ZmZXJQb2ludGVyRXZlbnQoU3VzcGVuZGluZ1BvaW50ZXJJbnB1dEZpbHRlci5rdDo2NjUpCglhdCBhbmRyb2lkeC5jb21wb3NlLnVpLmlucHV0LnBvaW50ZXIuU3VzcGVuZGluZ1BvaW50ZXJJbnB1dE1vZGlmaWVyTm9kZUltcGwuZGlzcGF0Y2hQb2ludGVyRXZlbnQoU3VzcGVuZGluZ1BvaW50ZXJJbnB1dEZpbHRlci5rdDo1NDQpCglhdCBhbmRyb2lkeC5jb21wb3NlLnVpLmlucHV0LnBvaW50ZXIuU3VzcGVuZGluZ1BvaW50ZXJJbnB1dE1vZGlmaWVyTm9kZUltcGwub25Qb2ludGVyRXZlbnQtSDBwUnVvWShTdXNwZW5kaW5nUG9pbnRlcklucHV0RmlsdGVyLmt0OjU2NikKCWF0IGFuZHJvaWR4LmNvbXBvc2UuZm91bmRhdGlvbi5BYnN0cmFjdENsaWNrYWJsZVBvaW50ZXJJbnB1dE5vZGUub25Qb2ludGVyRXZlbnQtSDBwUnVvWShDbGlja2FibGUua3Q6OTQ3KQoJYXQgYW5kcm9pZHguY29tcG9zZS5mb3VuZGF0aW9uLkFic3RyYWN0Q2xpY2thYmxlTm9kZS5vblBvaW50ZXJFdmVudC1IMHBSdW9ZKENsaWNrYWJsZS5rdDo3OTUpCglhdCBhbmRyb2lkeC5jb21wb3NlLnVpLmlucHV0LnBvaW50ZXIuTm9kZS5kaXNwYXRjaE1haW5FdmVudFBhc3MoSGl0UGF0aFRyYWNrZXIua3Q6MzE3KQoJYXQgYW5kcm9pZHguY29tcG9zZS51aS5pbnB1dC5wb2ludGVyLk5vZGUuZGlzcGF0Y2hNYWluRXZlbnRQYXNzKEhpdFBhdGhUcmFja2VyLmt0OjMwMykKCWF0IGFuZHJvaWR4LmNvbXBvc2UudWkuaW5wdXQucG9pbnRlci5Ob2RlLmRpc3BhdGNoTWFpbkV2ZW50UGFzcyhIaXRQYXRoVHJhY2tlci5rdDozMDMpCglhdCBhbmRyb2lkeC5jb21wb3NlLnVpLmlucHV0LnBvaW50ZXIuTm9kZVBhcmVudC5kaXNwYXRjaE1haW5FdmVudFBhc3MoSGl0UGF0aFRyYWNrZXIua3Q6MTg1KQoJYXQgYW5kcm9pZHguY29tcG9zZS51aS5pbnB1dC5wb2ludGVyLkhpdFBhdGhUcmFja2VyLmRpc3BhdGNoQ2hhbmdlcyhIaXRQYXRoVHJhY2tlci5rdDoxMDQpCglhdCBhbmRyb2lkeC5jb21wb3NlLnVpLmlucHV0LnBvaW50ZXIuUG9pbnRlcklucHV0RXZlbnRQcm9jZXNzb3IucHJvY2Vzcy1CSXpYZm9nKFBvaW50ZXJJbnB1dEV2ZW50UHJvY2Vzc29yLmt0OjExMykKCWF0IGFuZHJvaWR4LmNvbXBvc2UudWkucGxhdGZvcm0uQW5kcm9pZENvbXBvc2VWaWV3LnNlbmRNb3Rpb25FdmVudC04aUFzVlRjKEFuZHJvaWRDb21wb3NlVmlldy5hbmRyb2lkLmt0OjE1NzYpCglhdCBhbmRyb2lkeC5jb21wb3NlLnVpLnBsYXRmb3JtLkFuZHJvaWRDb21wb3NlVmlldy5oYW5kbGVNb3Rpb25FdmVudC04aUFzVlRjKEFuZHJvaWRDb21wb3NlVmlldy5hbmRyb2lkLmt0OjE1MjcpCglhdCBhbmRyb2lkeC5jb21wb3NlLnVpLnBsYXRmb3JtLkFuZHJvaWRDb21wb3NlVmlldy5kaXNwYXRjaFRvdWNoRXZlbnQoQW5kcm9pZENvbXBvc2VWaWV3LmFuZHJvaWQua3Q6MTQ2NikKCWF0IGFuZHJvaWQudmlldy5WaWV3R3JvdXAuZGlzcGF0Y2hUcmFuc2Zvcm1lZFRvdWNoRXZlbnQoVmlld0dyb3VwLmphdmE6MzEyMikKCWF0IGFuZHJvaWQudmlldy5WaWV3R3JvdXAuZGlzcGF0Y2hUb3VjaEV2ZW50KFZpZXdHcm91cC5qYXZhOjI4MDMpCglhdCBhbmRyb2lkLnZpZXcuVmlld0dyb3VwLmRpc3BhdGNoVHJhbnNmb3JtZWRUb3VjaEV2ZW50KFZpZXdHcm91cC5qYXZhOjMxMjIpCglhdCBhbmRyb2lkLnZpZXcuVmlld0dyb3VwLmRpc3BhdGNoVG91Y2hFdmVudChWaWV3R3JvdXAuamF2YToyODAzKQoJYXQgYW5kcm9pZC52aWV3LlZpZXdHcm91cC5kaXNwYXRjaFRyYW5zZm9ybWVkVG91Y2hFdmVudChWaWV3R3JvdXAuamF2YTozMTIyKQoJYXQgYW5kcm9pZC52aWV3LlZpZXdHcm91cC5kaXNwYXRjaFRvdWNoRXZlbnQoVmlld0dyb3VwLmphdmE6MjgwMykKCWF0IGFuZHJvaWQudmlldy5WaWV3R3JvdXAuZGlzcGF0Y2hUcmFuc2Zvcm1lZFRvdWNoRXZlbnQoVmlld0dyb3VwLmphdmE6MzEyMikKCWF0IGFuZHJvaWQudmlldy5WaWV3R3JvdXAuZGlzcGF0Y2hUb3VjaEV2ZW50KFZpZXdHcm91cC5qYXZhOjI4MDMpCglhdCBjb20uYW5kcm9pZC5pbnRlcm5hbC5wb2xpY3kuRGVjb3JWaWV3LnN1cGVyRGlzcGF0Y2hUb3VjaEV2ZW50KERlY29yVmlldy5qYXZhOjQ1OCkKCWF0IGNvbS5hbmRyb2lkLmludGVybmFsLnBvbGljeS5QaG9uZVdpbmRvdy5zdXBlckRpc3BhdGNoVG91Y2hFdmVudChQaG9uZVdpbmRvdy5qYXZhOjE5ODApCglhdCBhbmRyb2lkLmFwcC5BY3Rpdml0eS5kaXNwYXRjaFRvdWNoRXZlbnQoQWN0aXZpdHkuamF2YTo0NTMzKQoJYXQgY29tLmFuZHJvaWQuaW50ZXJuYWwucG9saWN5LkRlY29yVmlldy5kaXNwYXRjaFRvdWNoRXZlbnQoRGVjb3JWaWV3LmphdmE6NDE2KQoJYXQgYW5kcm9pZC52aWV3LlZpZXcuZGlzcGF0Y2hQb2ludGVyRXZlbnQoVmlldy5qYXZhOjE2NzI5KQoJYXQgYW5kcm9pZC52aWV3LlZpZXdSb290SW1wbCRWaWV3UG9zdEltZUlucHV0U3RhZ2UucHJvY2Vzc1BvaW50ZXJFdmVudChWaWV3Um9vdEltcGwuamF2YTo3OTQ3KQoJYXQgYW5kcm9pZC52aWV3LlZpZXdSb290SW1wbCRWaWV3UG9zdEltZUlucHV0U3RhZ2Uub25Qcm9jZXNzKFZpZXdSb290SW1wbC5qYXZhOjc3MTApCglhdCBhbmRyb2lkLnZpZXcuVmlld1Jvb3RJbXBsJElucHV0U3RhZ2UuZGVsaXZlcihWaWV3Um9vdEltcGwuamF2YTo3MTA2KQoJYXQgYW5kcm9pZC52aWV3LlZpZXdSb290SW1wbCRJbnB1dFN0YWdlLm9uRGVsaXZlclRvTmV4dChWaWV3Um9vdEltcGwuamF2YTo3MTYzKQoJYXQgYW5kcm9pZC52aWV3LlZpZXdSb290SW1wbCRJbnB1dFN0YWdlLmZvcndhcmQoVmlld1Jvb3RJbXBsLmphdmE6NzEyOSkKCWF0IGFuZHJvaWQudmlldy5WaWV3Um9vdEltcGwkQXN5bmNJbnB1dFN0YWdlLmZvcndhcmQoVmlld1Jvb3RJbXBsLmphdmE6NzI5NSkKCWF0IGFuZHJvaWQudmlldy5WaWV3Um9vdEltcGwkSW5wdXRTdGFnZS5hcHBseShWaWV3Um9vdEltcGwuamF2YTo3MTM3KQoJYXQgYW5kcm9pZC52aWV3LlZpZXdSb290SW1wbCRBc3luY0lucHV0U3RhZ2UuYXBwbHkoVmlld1Jvb3RJbXBsLmphdmE6NzM1MikKCWF0IGFuZHJvaWQudmlldy5WaWV3Um9vdEltcGwkSW5wdXRTdGFnZS5kZWxpdmVyKFZpZXdSb290SW1wbC5qYXZhOjcxMTApCglhdCBhbmRyb2lkLnZpZXcuVmlld1Jvb3RJbXBsJElucHV0U3RhZ2Uub25EZWxpdmVyVG9OZXh0KFZpZXdSb290SW1wbC5qYXZhOjcxNjMpCglhdCBhbmRyb2lkLnZpZXcuVmlld1Jvb3RJbXBsJElucHV0U3RhZ2UuZm9yd2FyZChWaWV3Um9vdEltcGwuamF2YTo3MTI5KQoJYXQgYW5kcm9pZC52aWV3LlZpZXdSb290SW1wbCRJbnB1dFN0YWdlLmFwcGx5KFZpZXdSb290SW1wbC5qYXZhOjcxMzcpCglhdCBhbmRyb2lkLnZpZXcuVmlld1Jvb3RJbXBsJElucHV0U3RhZ2UuZGVsaXZlcihWaWV3Um9vdEltcGwuamF2YTo3MTEwKQoJYXQgYW5kcm9pZC52aWV3LlZpZXdSb290SW1wbC5kZWxpdmVySW5wdXRFdmVudChWaWV3Um9vdEltcGwuamF2YToxMDIxNCkKCWF0IGFuZHJvaWQudmlldy5WaWV3Um9vdEltcGwuZG9Qcm9jZXNzSW5wdXRFdmVudHMoVmlld1Jvb3RJbXBsLmphdmE6MTAxNjUpCglhdCBhbmRyb2lkLnZpZXcuVmlld1Jvb3RJbXBsLmVucXVldWVJbnB1dEV2ZW50KFZpZXdSb290SW1wbC5qYXZhOjEwMTM0KQoJYXQgYW5kcm9pZC52aWV3LlZpZXdSb290SW1wbCRXaW5kb3dJbnB1dEV2ZW50UmVjZWl2ZXIub25JbnB1dEV2ZW50KFZpZXdSb290SW1wbC5qYXZhOjEwMzU2KQoJYXQgYW5kcm9pZC52aWV3LklucHV0RXZlbnRSZWNlaXZlci5kaXNwYXRjaElucHV0RXZlbnQoSW5wdXRFdmVudFJlY2VpdmVyLmphdmE6Mjk1KQoJYXQgYW5kcm9pZC5vcy5NZXNzYWdlUXVldWUubmF0aXZlUG9sbE9uY2UoTmF0aXZlIE1ldGhvZCkKCWF0IGFuZHJvaWQub3MuTWVzc2FnZVF1ZXVlLm5leHQoTWVzc2FnZVF1ZXVlLmphdmE6MzQ2KQoJYXQgYW5kcm9pZC5vcy5Mb29wZXIubG9vcE9uY2UoTG9vcGVyLmphdmE6MTg5KQoJYXQgYW5kcm9pZC5vcy5Mb29wZXIubG9vcChMb29wZXIuamF2YTozMTcpCglhdCBhbmRyb2lkLmFwcC5BY3Rpdml0eVRocmVhZC5tYWluKEFjdGl2aXR5VGhyZWFkLmphdmE6ODcwNSkKCWF0IGphdmEubGFuZy5yZWZsZWN0Lk1ldGhvZC5pbnZva2UoTmF0aXZlIE1ldGhvZCkKCWF0IGNvbS5hbmRyb2lkLmludGVybmFsLm9zLlJ1bnRpbWVJbml0JE1ldGhvZEFuZEFyZ3NDYWxsZXIucnVuKFJ1bnRpbWVJbml0LmphdmE6NTgwKQoJYXQgY29tLmFuZHJvaWQuaW50ZXJuYWwub3MuWnlnb3RlSW5pdC5tYWluKFp5Z290ZUluaXQuamF2YTo4ODYpCg==",
//                        ViolationPenalty.DropBox,
//                    )
//                    base64(
//                        "YW5kcm9pZC5vcy5zdHJpY3Rtb2RlLkRpc2tSZWFkVmlvbGF0aW9uCglhdCBhbmRyb2lkLm9zLlN0cmljdE1vZGUkQW5kcm9pZEJsb2NrR3VhcmRQb2xpY3kub25SZWFkRnJvbURpc2soU3RyaWN0TW9kZS5qYXZhOjE2ODMpCglhdCBsaWJjb3JlLmlvLkJsb2NrR3VhcmRPcy5hY2Nlc3MoQmxvY2tHdWFyZE9zLmphdmE6NzQpCglhdCBsaWJjb3JlLmlvLkZvcndhcmRpbmdPcy5hY2Nlc3MoRm9yd2FyZGluZ09zLmphdmE6MTI4KQoJYXQgYW5kcm9pZC5hcHAuQWN0aXZpdHlUaHJlYWQkQW5kcm9pZE9zLmFjY2VzcyhBY3Rpdml0eVRocmVhZC5qYXZhOjg1ODIpCglhdCBqYXZhLmlvLlVuaXhGaWxlU3lzdGVtLmNoZWNrQWNjZXNzKFVuaXhGaWxlU3lzdGVtLmphdmE6MzMyKQoJYXQgamF2YS5pby5GaWxlLmV4aXN0cyhGaWxlLmphdmE6ODI5KQoJYXQgYW5kcm9pZC5hcHAuQ29udGV4dEltcGwuZ2V0RGF0YURpcihDb250ZXh0SW1wbC5qYXZhOjMyNzEpCglhdCBhbmRyb2lkLmFwcC5Db250ZXh0SW1wbC5nZXRQcmVmZXJlbmNlc0RpcihDb250ZXh0SW1wbC5qYXZhOjc2NCkKCWF0IGFuZHJvaWQuYXBwLkNvbnRleHRJbXBsLmdldFNoYXJlZFByZWZlcmVuY2VzUGF0aChDb250ZXh0SW1wbC5qYXZhOjk5MSkKCWF0IGFuZHJvaWQuYXBwLkNvbnRleHRJbXBsLmdldFNoYXJlZFByZWZlcmVuY2VzKENvbnRleHRJbXBsLmphdmE6NjA2KQoJYXQgYW5kcm9pZC5jb250ZW50LkNvbnRleHRXcmFwcGVyLmdldFNoYXJlZFByZWZlcmVuY2VzKENvbnRleHRXcmFwcGVyLmphdmE6MjIzKQoJYXQgYW5kcm9pZC5hcHAuQWN0aXZpdHkuZ2V0UHJlZmVyZW5jZXMoQWN0aXZpdHkuamF2YTo3NjkxKQoJYXQgY29tLnN0cmljdHByby5leGFtcGxlLk1haW5BY3Rpdml0eSRvbkNyZWF0ZSQxJDEkMSQxJDEkMS5pbnZva2UoTWFpbkFjdGl2aXR5Lmt0OjMxKQoJYXQgY29tLnN0cmljdHByby5leGFtcGxlLk1haW5BY3Rpdml0eSRvbkNyZWF0ZSQxJDEkMSQxJDEkMS5pbnZva2UoTWFpbkFjdGl2aXR5Lmt0OjMwKQoJYXQgYW5kcm9pZHguY29tcG9zZS5mb3VuZGF0aW9uLkNsaWNrYWJsZVBvaW50ZXJJbnB1dE5vZGUkcG9pbnRlcklucHV0JDMuaW52b2tlLWstNGxRME0oQ2xpY2thYmxlLmt0Ojk4NykKCWF0IGFuZHJvaWR4LmNvbXBvc2UuZm91bmRhdGlvbi5DbGlja2FibGVQb2ludGVySW5wdXROb2RlJHBvaW50ZXJJbnB1dCQzLmludm9rZShDbGlja2FibGUua3Q6OTgxKQoJYXQgYW5kcm9pZHguY29tcG9zZS5mb3VuZGF0aW9uLmdlc3R1cmVzLlRhcEdlc3R1cmVEZXRlY3Rvckt0JGRldGVjdFRhcEFuZFByZXNzJDIkMS5pbnZva2VTdXNwZW5kKFRhcEdlc3R1cmVEZXRlY3Rvci5rdDoyNTUpCglhdCBrb3RsaW4uY29yb3V0aW5lcy5qdm0uaW50ZXJuYWwuQmFzZUNvbnRpbnVhdGlvbkltcGwucmVzdW1lV2l0aChDb250aW51YXRpb25JbXBsLmt0OjMzKQoJYXQga290bGlueC5jb3JvdXRpbmVzLkRpc3BhdGNoZWRUYXNrS3QucmVzdW1lKERpc3BhdGNoZWRUYXNrLmt0OjE3OSkKCWF0IGtvdGxpbnguY29yb3V0aW5lcy5EaXNwYXRjaGVkVGFza0t0LmRpc3BhdGNoKERpc3BhdGNoZWRUYXNrLmt0OjE2OCkKCWF0IGtvdGxpbnguY29yb3V0aW5lcy5DYW5jZWxsYWJsZUNvbnRpbnVhdGlvbkltcGwuZGlzcGF0Y2hSZXN1bWUoQ2FuY2VsbGFibGVDb250aW51YXRpb25JbXBsLmt0OjQ3NCkKCWF0IGtvdGxpbnguY29yb3V0aW5lcy5DYW5jZWxsYWJsZUNvbnRpbnVhdGlvbkltcGwucmVzdW1lSW1wbChDYW5jZWxsYWJsZUNvbnRpbnVhdGlvbkltcGwua3Q6NTA4KQoJYXQga290bGlueC5jb3JvdXRpbmVzLkNhbmNlbGxhYmxlQ29udGludWF0aW9uSW1wbC5yZXN1bWVJbXBsJGRlZmF1bHQoQ2FuY2VsbGFibGVDb250aW51YXRpb25JbXBsLmt0OjQ5NykKCWF0IGtvdGxpbnguY29yb3V0aW5lcy5DYW5jZWxsYWJsZUNvbnRpbnVhdGlvbkltcGwucmVzdW1lV2l0aChDYW5jZWxsYWJsZUNvbnRpbnVhdGlvbkltcGwua3Q6MzY4KQoJYXQgYW5kcm9pZHguY29tcG9zZS51aS5pbnB1dC5wb2ludGVyLlN1c3BlbmRpbmdQb2ludGVySW5wdXRNb2RpZmllck5vZGVJbXBsJFBvaW50ZXJFdmVudEhhbmRsZXJDb3JvdXRpbmUub2ZmZXJQb2ludGVyRXZlbnQoU3VzcGVuZGluZ1BvaW50ZXJJbnB1dEZpbHRlci5rdDo2NjUpCglhdCBhbmRyb2lkeC5jb21wb3NlLnVpLmlucHV0LnBvaW50ZXIuU3VzcGVuZGluZ1BvaW50ZXJJbnB1dE1vZGlmaWVyTm9kZUltcGwuZGlzcGF0Y2hQb2ludGVyRXZlbnQoU3VzcGVuZGluZ1BvaW50ZXJJbnB1dEZpbHRlci5rdDo1NDQpCglhdCBhbmRyb2lkeC5jb21wb3NlLnVpLmlucHV0LnBvaW50ZXIuU3VzcGVuZGluZ1BvaW50ZXJJbnB1dE1vZGlmaWVyTm9kZUltcGwub25Qb2ludGVyRXZlbnQtSDBwUnVvWShTdXNwZW5kaW5nUG9pbnRlcklucHV0RmlsdGVyLmt0OjU2NikKCWF0IGFuZHJvaWR4LmNvbXBvc2UuZm91bmRhdGlvbi5BYnN0cmFjdENsaWNrYWJsZVBvaW50ZXJJbnB1dE5vZGUub25Qb2ludGVyRXZlbnQtSDBwUnVvWShDbGlja2FibGUua3Q6OTQ3KQoJYXQgYW5kcm9pZHguY29tcG9zZS5mb3VuZGF0aW9uLkFic3RyYWN0Q2xpY2thYmxlTm9kZS5vblBvaW50ZXJFdmVudC1IMHBSdW9ZKENsaWNrYWJsZS5rdDo3OTUpCglhdCBhbmRyb2lkeC5jb21wb3NlLnVpLmlucHV0LnBvaW50ZXIuTm9kZS5kaXNwYXRjaE1haW5FdmVudFBhc3MoSGl0UGF0aFRyYWNrZXIua3Q6MzE3KQoJYXQgYW5kcm9pZHguY29tcG9zZS51aS5pbnB1dC5wb2ludGVyLk5vZGUuZGlzcGF0Y2hNYWluRXZlbnRQYXNzKEhpdFBhdGhUcmFja2VyLmt0OjMwMykKCWF0IGFuZHJvaWR4LmNvbXBvc2UudWkuaW5wdXQucG9pbnRlci5Ob2RlLmRpc3BhdGNoTWFpbkV2ZW50UGFzcyhIaXRQYXRoVHJhY2tlci5rdDozMDMpCglhdCBhbmRyb2lkeC5jb21wb3NlLnVpLmlucHV0LnBvaW50ZXIuTm9kZVBhcmVudC5kaXNwYXRjaE1haW5FdmVudFBhc3MoSGl0UGF0aFRyYWNrZXIua3Q6MTg1KQoJYXQgYW5kcm9pZHguY29tcG9zZS51aS5pbnB1dC5wb2ludGVyLkhpdFBhdGhUcmFja2VyLmRpc3BhdGNoQ2hhbmdlcyhIaXRQYXRoVHJhY2tlci5rdDoxMDQpCglhdCBhbmRyb2lkeC5jb21wb3NlLnVpLmlucHV0LnBvaW50ZXIuUG9pbnRlcklucHV0RXZlbnRQcm9jZXNzb3IucHJvY2Vzcy1CSXpYZm9nKFBvaW50ZXJJbnB1dEV2ZW50UHJvY2Vzc29yLmt0OjExMykKCWF0IGFuZHJvaWR4LmNvbXBvc2UudWkucGxhdGZvcm0uQW5kcm9pZENvbXBvc2VWaWV3LnNlbmRNb3Rpb25FdmVudC04aUFzVlRjKEFuZHJvaWRDb21wb3NlVmlldy5hbmRyb2lkLmt0OjE1NzYpCglhdCBhbmRyb2lkeC5jb21wb3NlLnVpLnBsYXRmb3JtLkFuZHJvaWRDb21wb3NlVmlldy5oYW5kbGVNb3Rpb25FdmVudC04aUFzVlRjKEFuZHJvaWRDb21wb3NlVmlldy5hbmRyb2lkLmt0OjE1MjcpCglhdCBhbmRyb2lkeC5jb21wb3NlLnVpLnBsYXRmb3JtLkFuZHJvaWRDb21wb3NlVmlldy5kaXNwYXRjaFRvdWNoRXZlbnQoQW5kcm9pZENvbXBvc2VWaWV3LmFuZHJvaWQua3Q6MTQ2NikKCWF0IGFuZHJvaWQudmlldy5WaWV3R3JvdXAuZGlzcGF0Y2hUcmFuc2Zvcm1lZFRvdWNoRXZlbnQoVmlld0dyb3VwLmphdmE6MzEyMikKCWF0IGFuZHJvaWQudmlldy5WaWV3R3JvdXAuZGlzcGF0Y2hUb3VjaEV2ZW50KFZpZXdHcm91cC5qYXZhOjI4MDMpCglhdCBhbmRyb2lkLnZpZXcuVmlld0dyb3VwLmRpc3BhdGNoVHJhbnNmb3JtZWRUb3VjaEV2ZW50KFZpZXdHcm91cC5qYXZhOjMxMjIpCglhdCBhbmRyb2lkLnZpZXcuVmlld0dyb3VwLmRpc3BhdGNoVG91Y2hFdmVudChWaWV3R3JvdXAuamF2YToyODAzKQoJYXQgYW5kcm9pZC52aWV3LlZpZXdHcm91cC5kaXNwYXRjaFRyYW5zZm9ybWVkVG91Y2hFdmVudChWaWV3R3JvdXAuamF2YTozMTIyKQoJYXQgYW5kcm9pZC52aWV3LlZpZXdHcm91cC5kaXNwYXRjaFRvdWNoRXZlbnQoVmlld0dyb3VwLmphdmE6MjgwMykKCWF0IGFuZHJvaWQudmlldy5WaWV3R3JvdXAuZGlzcGF0Y2hUcmFuc2Zvcm1lZFRvdWNoRXZlbnQoVmlld0dyb3VwLmphdmE6MzEyMikKCWF0IGFuZHJvaWQudmlldy5WaWV3R3JvdXAuZGlzcGF0Y2hUb3VjaEV2ZW50KFZpZXdHcm91cC5qYXZhOjI4MDMpCglhdCBjb20uYW5kcm9pZC5pbnRlcm5hbC5wb2xpY3kuRGVjb3JWaWV3LnN1cGVyRGlzcGF0Y2hUb3VjaEV2ZW50KERlY29yVmlldy5qYXZhOjQ1OCkKCWF0IGNvbS5hbmRyb2lkLmludGVybmFsLnBvbGljeS5QaG9uZVdpbmRvdy5zdXBlckRpc3BhdGNoVG91Y2hFdmVudChQaG9uZVdpbmRvdy5qYXZhOjE5ODApCglhdCBhbmRyb2lkLmFwcC5BY3Rpdml0eS5kaXNwYXRjaFRvdWNoRXZlbnQoQWN0aXZpdHkuamF2YTo0NTMzKQoJYXQgY29tLmFuZHJvaWQuaW50ZXJuYWwucG9saWN5LkRlY29yVmlldy5kaXNwYXRjaFRvdWNoRXZlbnQoRGVjb3JWaWV3LmphdmE6NDE2KQoJYXQgYW5kcm9pZC52aWV3LlZpZXcuZGlzcGF0Y2hQb2ludGVyRXZlbnQoVmlldy5qYXZhOjE2NzI5KQoJYXQgYW5kcm9pZC52aWV3LlZpZXdSb290SW1wbCRWaWV3UG9zdEltZUlucHV0U3RhZ2UucHJvY2Vzc1BvaW50ZXJFdmVudChWaWV3Um9vdEltcGwuamF2YTo3OTQ3KQoJYXQgYW5kcm9pZC52aWV3LlZpZXdSb290SW1wbCRWaWV3UG9zdEltZUlucHV0U3RhZ2Uub25Qcm9jZXNzKFZpZXdSb290SW1wbC5qYXZhOjc3MTApCglhdCBhbmRyb2lkLnZpZXcuVmlld1Jvb3RJbXBsJElucHV0U3RhZ2UuZGVsaXZlcihWaWV3Um9vdEltcGwuamF2YTo3MTA2KQoJYXQgYW5kcm9pZC52aWV3LlZpZXdSb290SW1wbCRJbnB1dFN0YWdlLm9uRGVsaXZlclRvTmV4dChWaWV3Um9vdEltcGwuamF2YTo3MTYzKQoJYXQgYW5kcm9pZC52aWV3LlZpZXdSb290SW1wbCRJbnB1dFN0YWdlLmZvcndhcmQoVmlld1Jvb3RJbXBsLmphdmE6NzEyOSkKCWF0IGFuZHJvaWQudmlldy5WaWV3Um9vdEltcGwkQXN5bmNJbnB1dFN0YWdlLmZvcndhcmQoVmlld1Jvb3RJbXBsLmphdmE6NzI5NSkKCWF0IGFuZHJvaWQudmlldy5WaWV3Um9vdEltcGwkSW5wdXRTdGFnZS5hcHBseShWaWV3Um9vdEltcGwuamF2YTo3MTM3KQoJYXQgYW5kcm9pZC52aWV3LlZpZXdSb290SW1wbCRBc3luY0lucHV0U3RhZ2UuYXBwbHkoVmlld1Jvb3RJbXBsLmphdmE6NzM1MikKCWF0IGFuZHJvaWQudmlldy5WaWV3Um9vdEltcGwkSW5wdXRTdGFnZS5kZWxpdmVyKFZpZXdSb290SW1wbC5qYXZhOjcxMTApCglhdCBhbmRyb2lkLnZpZXcuVmlld1Jvb3RJbXBsJElucHV0U3RhZ2Uub25EZWxpdmVyVG9OZXh0KFZpZXdSb290SW1wbC5qYXZhOjcxNjMpCglhdCBhbmRyb2lkLnZpZXcuVmlld1Jvb3RJbXBsJElucHV0U3RhZ2UuZm9yd2FyZChWaWV3Um9vdEltcGwuamF2YTo3MTI5KQoJYXQgYW5kcm9pZC52aWV3LlZpZXdSb290SW1wbCRJbnB1dFN0YWdlLmFwcGx5KFZpZXdSb290SW1wbC5qYXZhOjcxMzcpCglhdCBhbmRyb2lkLnZpZXcuVmlld1Jvb3RJbXBsJElucHV0U3RhZ2UuZGVsaXZlcihWaWV3Um9vdEltcGwuamF2YTo3MTEwKQoJYXQgYW5kcm9pZC52aWV3LlZpZXdSb290SW1wbC5kZWxpdmVySW5wdXRFdmVudChWaWV3Um9vdEltcGwuamF2YToxMDIxNCkKCWF0IGFuZHJvaWQudmlldy5WaWV3Um9vdEltcGwuZG9Qcm9jZXNzSW5wdXRFdmVudHMoVmlld1Jvb3RJbXBsLmphdmE6MTAxNjUpCglhdCBhbmRyb2lkLnZpZXcuVmlld1Jvb3RJbXBsLmVucXVldWVJbnB1dEV2ZW50KFZpZXdSb290SW1wbC5qYXZhOjEwMTM0KQoJYXQgYW5kcm9pZC52aWV3LlZpZXdSb290SW1wbCRXaW5kb3dJbnB1dEV2ZW50UmVjZWl2ZXIub25JbnB1dEV2ZW50KFZpZXdSb290SW1wbC5qYXZhOjEwMzU2KQoJYXQgYW5kcm9pZC52aWV3LklucHV0RXZlbnRSZWNlaXZlci5kaXNwYXRjaElucHV0RXZlbnQoSW5wdXRFdmVudFJlY2VpdmVyLmphdmE6Mjk1KQoJYXQgYW5kcm9pZC5vcy5NZXNzYWdlUXVldWUubmF0aXZlUG9sbE9uY2UoTmF0aXZlIE1ldGhvZCkKCWF0IGFuZHJvaWQub3MuTWVzc2FnZVF1ZXVlLm5leHQoTWVzc2FnZVF1ZXVlLmphdmE6MzQ2KQoJYXQgYW5kcm9pZC5vcy5Mb29wZXIubG9vcE9uY2UoTG9vcGVyLmphdmE6MTg5KQoJYXQgYW5kcm9pZC5vcy5Mb29wZXIubG9vcChMb29wZXIuamF2YTozMTcpCglhdCBhbmRyb2lkLmFwcC5BY3Rpdml0eVRocmVhZC5tYWluKEFjdGl2aXR5VGhyZWFkLmphdmE6ODcwNSkKCWF0IGphdmEubGFuZy5yZWZsZWN0Lk1ldGhvZC5pbnZva2UoTmF0aXZlIE1ldGhvZCkKCWF0IGNvbS5hbmRyb2lkLmludGVybmFsLm9zLlJ1bnRpbWVJbml0JE1ldGhvZEFuZEFyZ3NDYWxsZXIucnVuKFJ1bnRpbWVJbml0LmphdmE6NTgwKQoJYXQgY29tLmFuZHJvaWQuaW50ZXJuYWwub3MuWnlnb3RlSW5pdC5tYWluKFp5Z290ZUluaXQuamF2YTo4ODYpCg==",
//                        ViolationPenalty.DropBox,
//                    )
                }
                .build()
        )
    }

    private fun setupSrictCompat() {
        val threadPolicy: StrictMode.ThreadPolicy = StrictModeCompat.ThreadPolicy.Builder()
            .detectResourceMismatches()
            .detectCustomSlowCalls()
            .detectUnbufferedIo() // Available only on Android 8.0+
            .penaltyLog()
            .penaltyDeath()
            .penaltyListener(
                ContextCompat.getMainExecutor(this)
            ) {
                Log.e("StrictModeCompat", "Detected ThreadPolicy violation: $it")
            }
            .build()

        val vmPolicy: StrictMode.VmPolicy = StrictModeCompat.VmPolicy.Builder()
            .detectFileUriExposure()
            .detectLeakedRegistrationObjects()
            .detectCleartextNetwork()
            .detectUntaggedSockets() // Available only on Android 8.0+
            .detectContentUriWithoutPermission() // Available only on Android 8.0+
            .penaltyLog()
            .penaltyDeath()
            .penaltyListener(
                ContextCompat.getMainExecutor(this)
            ) {
                Log.e("StrictModeCompat", "Detected VmPolicy violation: $it")
            }
            .build()

        StrictModeCompat.setPolicies(threadPolicy, vmPolicy)
    }

    private fun setupStrictMode() {
        VmPolicy.Builder()
//            .setClassInstanceLimit()
            .detectAll()
            .detectActivityLeaks()
            .detectUntaggedSockets()
            .detectCleartextNetwork()
            .detectNonSdkApiUsage()
            .detectContentUriWithoutPermission()
            .detectImplicitDirectBoot()
            .detectIncorrectContextUse()
            .detectLeakedClosableObjects()
            .detectLeakedRegistrationObjects()
            .detectUnsafeIntentLaunch()
            .detectFileUriExposure()
            .detectCredentialProtectedWhileLocked()
            .detectLeakedSqlLiteObjects()

//            .permitNonSdkApiUsage()
//            .permitUnsafeIntentLaunch()

            .penaltyDeath()
            .penaltyDropBox()
            .penaltyDeathOnCleartextNetwork()
            .penaltyDeathOnFileUriExposure()
            .penaltyLog()
//            .penaltyListener()
            .build()
            .let(StrictMode::setVmPolicy)


        StrictMode.setThreadPolicy(
            ThreadPolicy.Builder()
                .detectAll()
                .detectExplicitGc()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .detectCustomSlowCalls()
                .detectResourceMismatches()
                .detectUnbufferedIo()

//                .permitAll()
//                .permitExplicitGc()
//                .permitDiskReads()
//                .permitDiskWrites()
//                .permitNetwork()
//                .permitCustomSlowCalls()
//                .permitResourceMismatches()
//                .permitUnbufferedIo()

//                .penaltyLog()
//                .penaltyDeath()
                .penaltyDropBox()
                .penaltyFlashScreen()
                .penaltyDialog()
//                .penaltyDeathOnNetwork()
                .penaltyListener(ContextCompat.getMainExecutor(this)) { violation ->
                    Log.e("StrictPro", "Detected ThreadPolicy violation: $violation")
                }
                .build()
        )
    }
}