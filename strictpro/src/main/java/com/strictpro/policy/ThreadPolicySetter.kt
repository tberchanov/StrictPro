package com.strictpro.policy

import android.os.Build
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import com.strictpro.StrictPro
import com.strictpro.penalty.ViolationPenalty
import com.strictpro.penalty.ViolationPenalty.Death
import com.strictpro.penalty.ViolationPenalty.DeathOnNetwork
import com.strictpro.penalty.ViolationPenalty.Dialog
import com.strictpro.penalty.ViolationPenalty.DropBox
import com.strictpro.penalty.ViolationPenalty.FlashScreen
import com.strictpro.penalty.ViolationPenalty.Log
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
            .doIf(policy.detectResourceMismatches) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    detectResourceMismatches()
                } else {
                    UnsupportedLogger.logUnsupportedFeature(
                        StrictPro.ThreadPolicy.CATEGORY,
                        "detectResourceMismatches"
                    )
                }
            }
            .doIf(policy.detectUnbufferedIo) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    detectUnbufferedIo()
                } else {
                    UnsupportedLogger.logUnsupportedFeature(
                        StrictPro.ThreadPolicy.CATEGORY,
                        "detectUnbufferedIo"
                    )
                }
            }
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
            .doIf(policy.permitResourceMismatches) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    permitResourceMismatches()
                } else {
                    UnsupportedLogger.logUnsupportedFeature(
                        StrictPro.ThreadPolicy.CATEGORY,
                        "permitResourceMismatches"
                    )
                }
            }
            .doIf(policy.permitUnbufferedIo) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    permitUnbufferedIo()
                } else {
                    UnsupportedLogger.logUnsupportedFeature(
                        StrictPro.ThreadPolicy.CATEGORY,
                        "permitUnbufferedIo"
                    )
                }
            }
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    policy.getPenaltyListeners().forEach { (executor, listener) ->
                        penaltyListener(executor, listener)
                    }
                } else {
                    UnsupportedLogger.logUnsupportedFeature(
                        StrictPro.ThreadPolicy.CATEGORY,
                        "penaltyListener"
                    )
                }
            }
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    penaltyListener(MainThreadExecutor()) { violation ->
                        val whiteListPenalties =
                            policy.violationWhiteList.getWhiteListPenalties(violation)
                        if (whiteListPenalties.isEmpty()) {
                            val policyPenalties =
                                ThreadPolicyPenaltiesCreator.create(policy, violation)
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
                } else {
                    UnsupportedLogger.logUnsupportedFeature(
                        StrictPro.ThreadPolicy.CATEGORY,
                        "setWhiteList"
                    )
                    val penalties = policy.getPenalties()
                    doIf(penalties.contains(Log)) { penaltyLog() }
                    doIf(penalties.contains(Death)) { penaltyDeath() }
                    doIf(penalties.contains(DropBox)) { penaltyDropBox() }
                    doIf(penalties.contains(Dialog)) { penaltyDialog() }
                    doIf(penalties.contains(FlashScreen)) { penaltyFlashScreen() }
                    doIf(penalties.contains(DeathOnNetwork)) { penaltyDeathOnNetwork() }
                }
            }
            .build()
        StrictMode.setThreadPolicy(androidPolicy)
    }
}