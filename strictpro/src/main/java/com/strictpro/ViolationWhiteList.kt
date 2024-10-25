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

internal typealias PenaltyCondition = (Violation) -> ViolationPenalty?

/**
 * The `ViolationWhiteList` class is used to define a set of conditions that determine
 * which violations should be ignored or assigned specific penalties. This is useful for customizing
 * the behavior of strict mode policies in Android applications.
 *
 * The class provides several methods to add different types of conditions to the white list:
 *
 * - `base64(base64: String, penalty: ViolationPenalty?)`: Adds a condition that matches violations
 *   based on a base64-encoded stack trace. If the stack trace of a violation matches the provided
 *   base64 string, the specified penalty is applied.
 *
 * - `contains(substring: String, penalty: ViolationPenalty?)`: Adds a condition that matches violations
 *   based on a substring in the stack trace. If the stack trace of a violation contains the specified
 *   substring, the specified penalty is applied.
 *
 * - `condition(condition: PenaltyCondition)`: Adds a custom condition to the white list. The condition
 *   is a function that takes a `Violation` and returns a `ViolationPenalty?`. If the condition returns
 *   a non-null penalty, it is applied to the violation.
 *
 * The class also provides an internal method to retrieve the penalties for a given violation:
 *
 * - `internal fun getWhiteListPenalties(violation: Violation): Set<ViolationPenalty>`: Evaluates all
 *   conditions in the white list against the provided violation and returns a set of penalties that
 *   should be applied. If certain conditions are met (e.g., network-related violations), additional
 *   penalties may be added.
 */
class ViolationWhiteList {

    private val whiteListConditions = mutableListOf<PenaltyCondition>()

    fun base64(base64: String, penalty: ViolationPenalty?) {
        whiteListConditions.add { violation ->
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
        whiteListConditions.add { violation ->
            if (violation.stackTraceToStringCompat().contains(substring)) {
                penalty
            } else {
                null
            }
        }
    }

    /**
     * If you want do nothing on some violation condition, you can return null.
     *
     * Example:
     * <pre>
     * condition { violation ->
     *     if (violation is NetworkViolation) {
     *         ViolationPenalty.Death
     *     } else {
     *         null
     *     }
     * }
     * </pre>
     */
    fun condition(condition: PenaltyCondition) {
        whiteListConditions.add(condition)
    }

    internal fun getWhiteListPenalties(violation: Violation): Set<ViolationPenalty> {
        val penalties = mutableSetOf<ViolationPenalty>()
        for (condition in whiteListConditions) {
            val penalty = condition(violation)
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

    internal fun containsConditions() = whiteListConditions.isNotEmpty()
}