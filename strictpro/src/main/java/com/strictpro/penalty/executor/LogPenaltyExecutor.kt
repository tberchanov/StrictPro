package com.strictpro.penalty.executor

import android.app.Activity
import android.os.strictmode.Violation
import android.util.Log
import com.strictpro.penalty.ViolationPenalty
import com.strictpro.utils.LIB_TAG

internal class LogPenaltyExecutor : PenaltyExecutor {
    override fun executablePenalty() = ViolationPenalty.Log

    override fun executePenalty(violation: Violation, currentActivity: Activity?) {
        Log.d(LIB_TAG, "Policy violation: ", violation)
    }
}