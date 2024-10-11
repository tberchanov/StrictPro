package com.strictpro.penalty.creator

import android.os.strictmode.CleartextNetworkViolation
import android.os.strictmode.FileUriExposedViolation
import android.os.strictmode.Violation
import com.strictpro.StrictPro
import com.strictpro.penalty.ViolationPenalty

object VmPolicyPenaltiesCreator {
    fun create(vmPolicy: StrictPro.VmPolicy, violation: Violation): Set<ViolationPenalty> {
        val penalties = vmPolicy.getPenalties().toMutableSet()
        fun shouldDeathOnCleartextNetwork(): Boolean {
            return penalties.contains(ViolationPenalty.DeathOnCleartextNetwork) &&
                    violation is CleartextNetworkViolation
        }

        fun shouldDeathOnFileUriExposure(): Boolean {
            return penalties.contains(ViolationPenalty.DeathOnFileUriExposure) &&
                    violation is FileUriExposedViolation
        }

        if (shouldDeathOnFileUriExposure() || shouldDeathOnCleartextNetwork()) {
            penalties.add(ViolationPenalty.Death)
        }

        return penalties
    }
}