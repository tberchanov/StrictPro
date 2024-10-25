package com.strictpro.utils

import android.util.Log
import com.strictpro.StrictPro
import java.util.Locale

private const val FEATURE_NOT_SUPPORTED_MSG = "%s:%s is not supported"

internal object Logger {

    fun logUnsupportedFeature(category: String, feature: String) {
        Log.d(
            LIB_TAG,
            String.format(Locale.US, FEATURE_NOT_SUPPORTED_MSG, category, feature),
        )
    }

    fun logDebug(message: String) {
        if (StrictPro.printDebugLogs) {
            Log.d(LIB_TAG, message)
        }
    }
}