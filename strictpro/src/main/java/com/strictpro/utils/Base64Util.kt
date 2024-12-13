package com.strictpro.utils

import android.util.Base64

object Base64Util {
    fun encodeToString(input: String): String {
        return Base64.encodeToString(input.toByteArray(), Base64.NO_WRAP)
    }
}