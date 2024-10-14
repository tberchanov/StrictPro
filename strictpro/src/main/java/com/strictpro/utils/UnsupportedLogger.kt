package com.strictpro.utils

import android.util.Log
import java.util.Locale

private const val FEATURE_NOT_SUPPORTED_MSG = "%s:%s is not supported"

internal object UnsupportedLogger {

    fun logUnsupportedFeature(category: String, feature: String) {
        Log.d(
            LIB_TAG,
            String.format(Locale.US, FEATURE_NOT_SUPPORTED_MSG, category, feature),
        )
    }
}