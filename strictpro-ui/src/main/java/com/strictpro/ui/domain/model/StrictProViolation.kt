package com.strictpro.ui.domain.model

import android.os.strictmode.Violation

data class StrictProViolation(
    val dateMillis: Long,
    val violation: Violation,
)