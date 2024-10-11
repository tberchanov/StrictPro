package com.strictpro.utils

internal inline fun <T> T.doIf(condition: Boolean, action: T.() -> Unit): T {
    if (condition) {
        action()
    }
    return this
}