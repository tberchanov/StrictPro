package com.strictpro.penalty.executor

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.os.strictmode.Violation
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.strictpro.penalty.ViolationPenalty

private const val TAG = "FlashScreenPenaltyExecutor"
private const val FLASH_DELAY = 100L
private const val FLASH_COLOR = android.R.color.holo_red_dark

internal class FlashScreenPenaltyExecutor : PenaltyExecutor {
    override fun executablePenalty() = ViolationPenalty.FlashScreen

    override fun executePenalty(violation: Violation, currentActivity: Activity?) {
        if (currentActivity != null) {
            flashScreen(currentActivity)
        } else {
            Log.e(TAG, "Cannot flash screen, as current activity is null")
        }
    }

    private fun flashScreen(activity: Activity) {
        val flashView = View(activity)
        flashView.setBackgroundColor(
            ContextCompat.getColor(
                activity,
                FLASH_COLOR
            )
        )
        activity.addContentView(
            flashView,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )

        Handler(Looper.getMainLooper()).postDelayed({
            val rootView =
                activity.window.decorView.findViewById<FrameLayout>(android.R.id.content)
            rootView.removeView(flashView)
        }, FLASH_DELAY)
    }
}