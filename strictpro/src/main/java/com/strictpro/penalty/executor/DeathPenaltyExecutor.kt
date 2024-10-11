package com.strictpro.penalty.executor

import android.app.Activity
import android.os.strictmode.Violation
import com.strictpro.penalty.ViolationPenalty

internal class DeathPenaltyExecutor : PenaltyExecutor {
    override fun executablePenalty() = ViolationPenalty.Death

    override fun executePenalty(violation: Violation, currentActivity: Activity?) {
        throw violation
    }
}