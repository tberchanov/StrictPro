package com.strictpro.utils

import android.os.Build
import android.os.strictmode.CleartextNetworkViolation
import android.os.strictmode.FileUriExposedViolation
import android.os.strictmode.NetworkViolation
import android.os.strictmode.Violation
import androidx.annotation.RequiresApi
import com.strictpro.penalty.ViolationPenalty

internal inline fun <T> T.doIf(condition: Boolean, action: T.() -> Unit): T {
    if (condition) {
        action()
    }
    return this
}

internal fun Violation.stackTraceToStringCompat(): String {
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
        this.stackTrace.joinToString("\n")
    } else {
        stackTraceToString()
    }
}

@RequiresApi(Build.VERSION_CODES.P)
internal fun Set<ViolationPenalty>.shouldDeathOnNetwork(violation: Violation): Boolean {
    return contains(ViolationPenalty.DeathOnNetwork) && violation is NetworkViolation
}

@RequiresApi(Build.VERSION_CODES.P)
internal fun Set<ViolationPenalty>.shouldDeathOnCleartextNetwork(violation: Violation): Boolean {
    return contains(ViolationPenalty.DeathOnCleartextNetwork) &&
            violation is CleartextNetworkViolation
}

@RequiresApi(Build.VERSION_CODES.P)
internal fun Set<ViolationPenalty>.shouldDeathOnFileUriExposure(violation: Violation): Boolean {
    return contains(ViolationPenalty.DeathOnFileUriExposure) &&
            violation is FileUriExposedViolation
}
