package com.strictpro.penalty.executor

import android.app.Activity
import android.os.strictmode.Violation
import com.strictpro.penalty.ViolationPenalty

internal interface PenaltyExecutor {

    fun executablePenalty(): ViolationPenalty

    fun executePenalty(violation: Violation, currentActivity: Activity?)
}