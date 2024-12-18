package com.strictpro.ui

import com.strictpro.StrictPro
import java.util.concurrent.Executor

class StrictProUiPenaltyListener {
    companion object {
        fun create(): StrictProUiPenaltyListener {
            return StrictProUiPenaltyListener()
        }
    }
}

fun StrictPro.VmPolicy.Builder.penaltyListener(
    executor: Executor,
    listener: StrictProUiPenaltyListener,
): StrictPro.VmPolicy.Builder {
    return this
}

fun StrictPro.ThreadPolicy.Builder.penaltyListener(
    executor: Executor,
    listener: StrictProUiPenaltyListener,
): StrictPro.ThreadPolicy.Builder {
    return this
}