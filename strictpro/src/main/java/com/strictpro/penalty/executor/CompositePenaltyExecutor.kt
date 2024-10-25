package com.strictpro.penalty.executor

import android.app.Activity
import android.os.strictmode.Violation
import com.strictpro.penalty.ViolationPenalty
import com.strictpro.utils.Logger

internal class CompositePenaltyExecutor(
    private val penaltyExecutors: List<PenaltyExecutor>
) {

    fun execute(
        violation: Violation,
        penalties: Set<ViolationPenalty>,
        currentActivity: Activity?,
    ) {
        Logger.logDebug("CompositePenaltyExecutor.execute $violation, penalties: $penalties")
        penaltyExecutors.forEach { executor ->
            if (penalties.contains(executor.executablePenalty())) {
                executor.executePenalty(violation, currentActivity)
            }
        }
    }
}