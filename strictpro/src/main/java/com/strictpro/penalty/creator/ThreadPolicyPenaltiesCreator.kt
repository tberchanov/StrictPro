package com.strictpro.penalty.creator

import android.os.Build
import android.os.strictmode.Violation
import androidx.annotation.RequiresApi
import com.strictpro.StrictPro
import com.strictpro.penalty.ViolationPenalty
import com.strictpro.utils.shouldDeathOnNetwork

internal object ThreadPolicyPenaltiesCreator {
    @RequiresApi(Build.VERSION_CODES.P)
    fun create(threadPolicy: StrictPro.ThreadPolicy, violation: Violation): Set<ViolationPenalty> {
        val penalties = threadPolicy.getPenalties().toMutableSet()
        if (penalties.shouldDeathOnNetwork(violation)) {
            penalties.add(ViolationPenalty.Death)
        }
        return penalties
    }
}