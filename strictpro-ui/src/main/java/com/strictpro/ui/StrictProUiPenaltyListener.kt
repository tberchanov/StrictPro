package com.strictpro.ui

import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.StrictMode.OnThreadViolationListener
import android.os.StrictMode.OnVmViolationListener
import android.os.strictmode.Violation
import androidx.annotation.RequiresApi
import com.strictpro.StrictPro
import com.strictpro.ui.StrictProUiPenaltyListener.StrictProUiPenaltyListenerVersionP
import java.util.concurrent.Executor

open class StrictProUiPenaltyListener private constructor() {
    companion object {
        fun create(): StrictProUiPenaltyListener {
            return if (Build.VERSION.SDK_INT >= VERSION_CODES.P) {
                StrictProUiPenaltyListenerVersionP()
            } else {
                StrictProUiPenaltyListenerStub
            }
        }
    }

    @RequiresApi(VERSION_CODES.P)
    internal class StrictProUiPenaltyListenerVersionP : StrictProUiPenaltyListener(),
        OnThreadViolationListener, OnVmViolationListener {
        override fun onThreadViolation(violation: Violation) {
            onViolation(violation)
        }

        override fun onVmViolation(violation: Violation) {
            onViolation(violation)
        }

        private fun onViolation(violation: Violation) {
            ViolationSaver.save(violation)
        }
    }

    internal object StrictProUiPenaltyListenerStub : StrictProUiPenaltyListener()
}

fun StrictPro.VmPolicy.Builder.penaltyListener(
    executor: Executor,
    listener: StrictProUiPenaltyListener,
): StrictPro.VmPolicy.Builder {
    return if (Build.VERSION.SDK_INT >= VERSION_CODES.P &&
        listener is StrictProUiPenaltyListenerVersionP
    ) {
        penaltyListener(executor, listener)
    } else {
        this
    }
}

fun StrictPro.ThreadPolicy.Builder.penaltyListener(
    executor: Executor,
    listener: StrictProUiPenaltyListener,
): StrictPro.ThreadPolicy.Builder {
    return if (Build.VERSION.SDK_INT >= VERSION_CODES.P &&
        listener is StrictProUiPenaltyListenerVersionP
    ) {
        penaltyListener(executor, listener)
    } else {
        this
    }
}
