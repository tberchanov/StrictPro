package com.strictpro

import android.os.strictmode.NetworkViolation
import android.os.strictmode.Violation
import com.strictpro.penalty.ViolationPenalty
import com.strictpro.utils.Base64Util

internal typealias ViolationPredicate = (Violation) -> ViolationPenalty

class ViolationWhiteList {

    private val whiteListPredicates = mutableListOf<ViolationPredicate>()

    fun base64(base64: String, penalty: ViolationPenalty) {
        whiteListPredicates.add { violation ->
            val stack = violation.stackTraceToString()
            val base64Stack = Base64Util.encodeToString(stack)
            if (base64Stack == base64) {
                penalty
            } else {
                ViolationPenalty.Ignore
            }
        }
    }

    fun contains(substring: String, penalty: ViolationPenalty) {
        whiteListPredicates.add { violation ->
            if (violation.stackTraceToString().contains(substring)) {
                penalty
            } else {
                ViolationPenalty.Ignore
            }
        }
    }

    fun ignoreIf(predicate: ViolationPredicate) {
        whiteListPredicates.add(predicate)
    }

    internal fun getWhiteListPenalties(violation: Violation): Set<ViolationPenalty> {
        val penalties = mutableSetOf<ViolationPenalty>()
        for (predicate in whiteListPredicates) {
            penalties.add(predicate(violation))
        }
        if (penalties.contains(ViolationPenalty.DeathOnNetwork) && violation is NetworkViolation) {
            penalties.add(ViolationPenalty.Death)
        }
        return penalties
    }
}