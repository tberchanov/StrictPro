package com.strictpro.policy

import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import androidx.annotation.RequiresApi
import com.strictpro.StrictPro
import com.strictpro.penalty.ViolationPenalty
import com.strictpro.penalty.ViolationPenalty.Death
import com.strictpro.penalty.ViolationPenalty.DeathOnNetwork
import com.strictpro.penalty.ViolationPenalty.Dialog
import com.strictpro.penalty.ViolationPenalty.DropBox
import com.strictpro.penalty.ViolationPenalty.FlashScreen
import com.strictpro.penalty.ViolationPenalty.Ignore
import com.strictpro.penalty.ViolationPenalty.Log
import com.strictpro.penalty.creator.ThreadPolicyPenaltiesCreator
import com.strictpro.utils.MainThreadExecutor
import com.strictpro.utils.Logger
import com.strictpro.utils.doIf

internal object ThreadPolicySetter {
    fun set(policy: StrictPro.ThreadPolicy) {
        val androidPolicy = ThreadPolicy.Builder()
            .applyDetects(policy)
            .applyPermits(policy)
            .applyOther(policy)
            .applyPenalties(policy)
            .build()
        StrictMode.setThreadPolicy(androidPolicy)
    }

    private fun ThreadPolicy.Builder.applyDetects(
        policy: StrictPro.ThreadPolicy
    ): ThreadPolicy.Builder {
        return doIf(policy.detectAll) { detectAll() }
            .doIf(policy.detectExplicitGc) {
                if (VERSION.SDK_INT >= VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    detectExplicitGc()
                } else {
                    Logger.logUnsupportedFeature(
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
                if (VERSION.SDK_INT >= VERSION_CODES.M) {
                    detectResourceMismatches()
                } else {
                    Logger.logUnsupportedFeature(
                        StrictPro.ThreadPolicy.CATEGORY,
                        "detectResourceMismatches"
                    )
                }
            }
            .doIf(policy.detectUnbufferedIo) {
                if (VERSION.SDK_INT >= VERSION_CODES.O) {
                    detectUnbufferedIo()
                } else {
                    Logger.logUnsupportedFeature(
                        StrictPro.ThreadPolicy.CATEGORY,
                        "detectUnbufferedIo"
                    )
                }
            }
    }

    private fun ThreadPolicy.Builder.applyPermits(
        policy: StrictPro.ThreadPolicy
    ): ThreadPolicy.Builder {
        return doIf(policy.permitAll) { permitAll() }
            .doIf(policy.permitExplicitGc) {
                if (VERSION.SDK_INT >= VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    permitExplicitGc()
                } else {
                    Logger.logUnsupportedFeature(
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
                if (VERSION.SDK_INT >= VERSION_CODES.M) {
                    permitResourceMismatches()
                } else {
                    Logger.logUnsupportedFeature(
                        StrictPro.ThreadPolicy.CATEGORY,
                        "permitResourceMismatches"
                    )
                }
            }
            .doIf(policy.permitUnbufferedIo) {
                if (VERSION.SDK_INT >= VERSION_CODES.O) {
                    permitUnbufferedIo()
                } else {
                    Logger.logUnsupportedFeature(
                        StrictPro.ThreadPolicy.CATEGORY,
                        "permitUnbufferedIo"
                    )
                }
            }
    }

    private fun ThreadPolicy.Builder.applyOther(
        policy: StrictPro.ThreadPolicy
    ): ThreadPolicy.Builder {
        if (policy.getPenaltyListeners().isNotEmpty()) {
            if (VERSION.SDK_INT >= VERSION_CODES.P) {
                /*
                * Listeners are called in `applyStrictProPenalties`.
                * */
            } else {
                Logger.logUnsupportedFeature(
                    StrictPro.ThreadPolicy.CATEGORY,
                    "penaltyListener"
                )
            }
        }
        return this
    }

    private fun ThreadPolicy.Builder.applyPenalties(
        policy: StrictPro.ThreadPolicy
    ): ThreadPolicy.Builder {
        return if (VERSION.SDK_INT >= VERSION_CODES.P) {
            applyStrictProPenalties(policy)
        } else {
            if (policy.violationWhiteList.containsConditions()) {
                Logger.logUnsupportedFeature(
                    StrictPro.ThreadPolicy.CATEGORY,
                    "setWhiteList"
                )
            }
            applyStrictModePenalties(policy.getPenalties())
        }
    }

    @RequiresApi(VERSION_CODES.P)
    private fun ThreadPolicy.Builder.applyStrictProPenalties(
        policy: StrictPro.ThreadPolicy
    ): ThreadPolicy.Builder {
        return penaltyListener(MainThreadExecutor()) { violation ->
            Logger.logDebug("ThreadPolicySetter.penaltyListener: $violation")

            val whiteListPenalties =
                policy.violationWhiteList.getWhiteListPenalties(violation)
            if (whiteListPenalties.contains(Ignore)) {
                Logger.logDebug("ThreadPolicySetter.penaltyListener: ignoring penalties")
            } else {
                val penalties = whiteListPenalties.ifEmpty {
                    Logger.logDebug("ThreadPolicySetter.penaltyListener: no white list penalties")
                    ThreadPolicyPenaltiesCreator.create(policy, violation)
                }.toMutableSet()
                // According to the StrictMode documentation,
                // if no penalties are set, the default is Log.
                if (penalties.isEmpty()) {
                    penalties.add(Log)
                }
                StrictPro.penaltyExecutor.execute(
                    violation, penalties, StrictPro.currentActivityRef.get()
                )
            }

            /*
            * As StrictMode allows to set only single penalty listener,
            * StrictPro should call all listeners from its internal listener.
            * */
            policy.getPenaltyListeners().forEach { (executor, listener) ->
                executor.execute {
                    listener.onThreadViolation(violation)
                }
            }
        }
    }

    private fun ThreadPolicy.Builder.applyStrictModePenalties(
        penalties: Set<ViolationPenalty>
    ): ThreadPolicy.Builder {
        return doIf(penalties.contains(Log)) { penaltyLog() }
            .doIf(penalties.contains(Death)) { penaltyDeath() }
            .doIf(penalties.contains(DropBox)) { penaltyDropBox() }
            .doIf(penalties.contains(Dialog)) { penaltyDialog() }
            .doIf(penalties.contains(FlashScreen)) { penaltyFlashScreen() }
            .doIf(penalties.contains(DeathOnNetwork)) { penaltyDeathOnNetwork() }
    }
}