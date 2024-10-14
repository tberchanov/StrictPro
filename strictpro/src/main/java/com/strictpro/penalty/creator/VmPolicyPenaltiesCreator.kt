package com.strictpro.penalty.creator

import android.os.Build
import android.os.strictmode.Violation
import androidx.annotation.RequiresApi
import com.strictpro.StrictPro
import com.strictpro.penalty.ViolationPenalty
import com.strictpro.utils.shouldDeathOnCleartextNetwork
import com.strictpro.utils.shouldDeathOnFileUriExposure

internal object VmPolicyPenaltiesCreator {
    @RequiresApi(Build.VERSION_CODES.P)
    fun create(vmPolicy: StrictPro.VmPolicy, violation: Violation): Set<ViolationPenalty> {
        val penalties = vmPolicy.getPenalties().toMutableSet()
        if (penalties.shouldDeathOnFileUriExposure(violation) ||
            penalties.shouldDeathOnCleartextNetwork(violation)
        ) {
            penalties.add(ViolationPenalty.Death)
        }

        return penalties
    }
}