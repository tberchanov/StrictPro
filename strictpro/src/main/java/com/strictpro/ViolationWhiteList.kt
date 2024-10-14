package com.strictpro

import android.content.Context
import android.os.Build
import android.os.strictmode.Violation
import com.strictpro.penalty.ViolationPenalty
import com.strictpro.utils.Base64Util
import com.strictpro.utils.shouldDeathOnCleartextNetwork
import com.strictpro.utils.shouldDeathOnFileUriExposure
import com.strictpro.utils.shouldDeathOnNetwork
import com.strictpro.utils.stackTraceToStringCompat

internal typealias ViolationPredicate = (Violation) -> ViolationPenalty?

// TODO document
class ViolationWhiteList {

    private val whiteListPredicates = mutableListOf<ViolationPredicate>()

    fun base64(base64: String, penalty: ViolationPenalty?) {
        whiteListPredicates.add { violation ->
            val stack = violation.stackTraceToStringCompat()
            val base64Stack = Base64Util.encodeToString(stack)
            if (base64Stack == base64) {
                penalty
            } else {
                null
            }
        }
    }

    fun contains(substring: String, penalty: ViolationPenalty?) {
        whiteListPredicates.add { violation ->
            if (violation.stackTraceToStringCompat().contains(substring)) {
                penalty
            } else {
                null
            }
        }
    }

    fun ignoreIf(predicate: ViolationPredicate) {
        whiteListPredicates.add(predicate)
    }

    fun detectAppViolationsOnly(context: Context) {
        whiteListPredicates.add { violation ->
            if (violation.stackTraceToStringCompat().contains(context.packageName)) {
                null
            } else {
                ViolationPenalty.Ignore
            }
        }
    }

    internal fun getWhiteListPenalties(violation: Violation): Set<ViolationPenalty> {
        val penalties = mutableSetOf<ViolationPenalty>()
        for (predicate in whiteListPredicates) {
            val penalty = predicate(violation)
            if (penalty != null) {
                penalties.add(penalty)
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P &&
            (penalties.shouldDeathOnNetwork(violation) ||
                penalties.shouldDeathOnFileUriExposure(violation) ||
                penalties.shouldDeathOnCleartextNetwork(violation))
        ) {
            penalties.add(ViolationPenalty.Death)
        }

        return penalties
    }
}