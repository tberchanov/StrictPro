package com.strictpro.ui.domain.model

import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.strictmode.Violation

data class StrictProViolation(
    val id: String,
    val dateMillis: Long,
    val violation: Violation,
)

fun StrictProViolation.getViolationName(): String {
    return if (VERSION.SDK_INT >= VERSION_CODES.P) {
        violation::class.java.simpleName
    } else {
        ""
    }
}