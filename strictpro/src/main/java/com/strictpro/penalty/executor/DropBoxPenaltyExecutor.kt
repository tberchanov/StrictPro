package com.strictpro.penalty.executor

import android.app.Activity
import android.content.Context
import android.os.DropBoxManager
import android.os.strictmode.Violation
import android.util.Log
import com.strictpro.penalty.ViolationPenalty
import com.strictpro.utils.LIB_TAG
import com.strictpro.utils.stackTraceToStringCompat

private const val TAG = "DropBoxPenaltyExecutor"

internal class DropBoxPenaltyExecutor : PenaltyExecutor {
    override fun executablePenalty() = ViolationPenalty.DropBox

    override fun executePenalty(violation: Violation, currentActivity: Activity?) {
        if (currentActivity != null) {
            val dropbox = currentActivity.getSystemService(
                Context.DROPBOX_SERVICE
            ) as DropBoxManager
            dropbox.addText(LIB_TAG, violation.stackTraceToStringCompat())
        } else {
            Log.e(TAG, "Cannot add to dropbox, as current activity is null")
        }
    }
}