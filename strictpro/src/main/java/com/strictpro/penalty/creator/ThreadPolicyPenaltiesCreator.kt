package com.strictpro.penalty.creator

import android.os.strictmode.NetworkViolation
import android.os.strictmode.Violation
import com.strictpro.StrictPro
import com.strictpro.penalty.ViolationPenalty

object ThreadPolicyPenaltiesCreator {
    fun create(threadPolicy: StrictPro.ThreadPolicy, violation: Violation): Set<ViolationPenalty> {
        val penalties = threadPolicy.getPenalties().toMutableSet()
        fun shouldDeathOnNetwork(): Boolean {
            return penalties.contains(ViolationPenalty.DeathOnNetwork) &&
                    violation is NetworkViolation
        }

        if (shouldDeathOnNetwork()) {
            penalties.add(ViolationPenalty.Death)
        }
        return penalties
    }
}