package com.strictpro.utils

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor

internal class MainThreadExecutor : Executor {
    private val handler: Handler = Handler(Looper.getMainLooper())

    override fun execute(command: Runnable) {
        handler.post(command)
    }
}
