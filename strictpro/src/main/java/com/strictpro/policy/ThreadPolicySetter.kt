package com.strictpro.policy

import android.os.Build
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.setThreadPolicy
import com.strictpro.StrictPro
import com.strictpro.penalty.ViolationPenalty
import com.strictpro.penalty.creator.ThreadPolicyPenaltiesCreator
import com.strictpro.utils.MainThreadExecutor
import com.strictpro.utils.UnsupportedLogger
import com.strictpro.utils.doIf

internal object ThreadPolicySetter {
    fun set(policy: StrictPro.ThreadPolicy) {
        val androidPolicy = ThreadPolicy.Builder()
            .doIf(policy.detectAll) { detectAll() }
            .doIf(policy.detectExplicitGc) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    detectExplicitGc()
                } else {
                    UnsupportedLogger.logUnsupportedFeature(
                        StrictPro.ThreadPolicy.CATEGORY,
                        "detectExplicitGc"
                    )
                }
            }
            .doIf(policy.detectDiskReads) { detectDiskReads() }
            .doIf(policy.detectDiskWrites) { detectDiskWrites() }
            .doIf(policy.detectNetwork) { detectNetwork() }
            .doIf(policy.detectCustomSlowCalls) { detectCustomSlowCalls() }
            .doIf(policy.detectResourceMismatches) { detectResourceMismatches() }
            .doIf(policy.detectUnbufferedIo) { detectUnbufferedIo() }
            .doIf(policy.permitAll) { permitAll() }
            .doIf(policy.permitExplicitGc) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    permitExplicitGc()
                } else {
                    UnsupportedLogger.logUnsupportedFeature(
                        StrictPro.ThreadPolicy.CATEGORY,
                        "permitExplicitGc"
                    )
                }
            }
            .doIf(policy.permitDiskReads) { permitDiskReads() }
            .doIf(policy.permitDiskWrites) { permitDiskWrites() }
            .doIf(policy.permitNetwork) { permitNetwork() }
            .doIf(policy.permitCustomSlowCalls) { permitCustomSlowCalls() }
            .doIf(policy.permitResourceMismatches) { permitResourceMismatches() }
            .doIf(policy.permitUnbufferedIo) { permitUnbufferedIo() }
            .apply {
                policy.getPenaltyListeners().forEach { (executor, listener) ->
                    penaltyListener(executor, listener)
                }
            }
            .penaltyListener(MainThreadExecutor()) { violation ->
                val whiteListPenalties = policy.violationWhiteList.getWhiteListPenalties(violation)
                if (whiteListPenalties.isEmpty()) {
                    val policyPenalties = ThreadPolicyPenaltiesCreator.create(policy, violation)
                    StrictPro.penaltyExecutor.execute(
                        violation, policyPenalties, StrictPro.currentActivityRef.get()
                    )
                } else if (whiteListPenalties.contains(ViolationPenalty.Ignore)) {
                    // noop
                } else {
                    StrictPro.penaltyExecutor.execute(
                        violation, whiteListPenalties, StrictPro.currentActivityRef.get()
                    )
                }
            }
            .build()
        setThreadPolicy(androidPolicy)
    }
}