package com.strictpro.penalty.executor

import android.app.Activity
import android.os.strictmode.Violation
import com.strictpro.penalty.ViolationPenalty

internal class CompositePenaltyExecutor(
    private val penaltyExecutors: List<PenaltyExecutor>
) {

    fun execute(
        violation: Violation,
        penalties: Set<ViolationPenalty>,
        currentActivity: Activity?,
    ) {
        penaltyExecutors.forEach { executor ->
            if (penalties.contains(executor.executablePenalty())) {
                executor.executePenalty(violation, currentActivity)
            }
        }
    }
}