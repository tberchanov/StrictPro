package com.strictpro.ui.presentation.violations.history.model

data class ViolationHistoryItemUI(
    val dateMillis: Long,
    val formattedDate: String,
    val violationName: String,
    val filteredStackTraceItems: List<String>,
    val violationId: String,
)
