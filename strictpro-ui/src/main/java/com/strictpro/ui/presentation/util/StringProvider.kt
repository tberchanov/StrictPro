package com.strictpro.ui.presentation.util

import android.content.Context
import com.strictpro.ui.R

internal class StringProvider(
    private val context: Context,
) {

    fun violationsScreenTitle(violationsQuantity: Int): String {
        return context.getString(R.string.distinct_violations_format, violationsQuantity)
    }

    fun totalViolations(violationsQuantity: Int): String {
        return context.getString(R.string.total_violations_format, violationsQuantity)
    }

    fun violations(violationsQuantity: Int, type: String): String {
        return context.getString(R.string.violations_format, violationsQuantity, type)
    }
}