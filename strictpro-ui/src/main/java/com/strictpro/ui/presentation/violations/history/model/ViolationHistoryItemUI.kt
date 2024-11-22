package com.strictpro.ui.presentation.violations.history.model

import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import com.strictpro.ui.domain.model.StrictProViolation
import com.strictpro.ui.presentation.violations.history.util.formatViolationDate

data class ViolationHistoryItemUI(
    val dateMillis: Long,
    val formattedDate: String,
    val violationName: String,
    val filteredStackTraceItems: List<String>,
)

fun StrictProViolation.toViolationHistoryItemUI(packageName: String): ViolationHistoryItemUI {
    val formattedDate = formatViolationDate(dateMillis)
    val filteredStackTraceItems = if (VERSION.SDK_INT >= VERSION_CODES.P) {
        violation.stackTrace
            .filter {
                it.className.startsWith(packageName)
            }
            .take(3)
            .map { it.toString() }
    } else {
        emptyList()
    }

    val violationName = if (VERSION.SDK_INT >= VERSION_CODES.P) {
        violation::class.java.simpleName
    } else {
        ""
    }

    return ViolationHistoryItemUI(
        dateMillis = dateMillis,
        formattedDate = formattedDate,
        violationName = violationName,
        filteredStackTraceItems = filteredStackTraceItems,
    )
}