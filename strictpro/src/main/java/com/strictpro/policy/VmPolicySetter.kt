package com.strictpro.policy

import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import androidx.annotation.RequiresApi
import com.strictpro.StrictPro
import com.strictpro.penalty.ViolationPenalty
import com.strictpro.penalty.ViolationPenalty.Death
import com.strictpro.penalty.ViolationPenalty.DeathOnCleartextNetwork
import com.strictpro.penalty.ViolationPenalty.DeathOnFileUriExposure
import com.strictpro.penalty.ViolationPenalty.DropBox
import com.strictpro.penalty.ViolationPenalty.Ignore
import com.strictpro.penalty.ViolationPenalty.Log
import com.strictpro.penalty.creator.VmPolicyPenaltiesCreator
import com.strictpro.utils.MainThreadExecutor
import com.strictpro.utils.Logger
import com.strictpro.utils.doIf

internal object VmPolicySetter {

    fun set(policy: StrictPro.VmPolicy) {
        val androidPolicy = VmPolicy.Builder()
            .applyDetects(policy)
            .applyPermits(policy)
            .applyOther(policy)
            .applyPenalties(policy)
            .build()
        StrictMode.setVmPolicy(androidPolicy)
    }

    private fun VmPolicy.Builder.applyDetects(policy: StrictPro.VmPolicy): VmPolicy.Builder {
        return doIf(policy.detectAll) { detectAll() }
            .doIf(policy.detectActivityLeaks) { detectActivityLeaks() }
            .doIf(policy.detectUntaggedSockets) {
                if (VERSION.SDK_INT >= VERSION_CODES.O) {
                    detectUntaggedSockets()
                } else {
                    Logger.logUnsupportedFeature(
                        StrictPro.VmPolicy.CATEGORY,
                        "detectUntaggedSockets"
                    )
                }
            }
            /**
             * For some reason StrictMode.VmPolicy.Builder.detectCleartextNetwork is not activated within
             * StrictMode.VmPolicy.Builder.detectAll.
             * That's why we need to activate it separately if policy.detectAll is true.
             *
             * Detected on API 35.
             */
            .doIf(policy.detectAll || policy.detectCleartextNetwork) {
                if (VERSION.SDK_INT >= VERSION_CODES.M) {
                    detectCleartextNetwork()
                } else {
                    Logger.logUnsupportedFeature(
                        StrictPro.VmPolicy.CATEGORY,
                        "detectCleartextNetwork"
                    )
                }
            }
            /**
             * For some reason StrictMode.VmPolicy.Builder.detectNonSdkApiUsage is not activated within
             * StrictMode.VmPolicy.Builder.detectAll.
             * That's why we need to activate it separately if policy.detectAll is true.
             *
             * Detected on API 35.
             */
            .doIf(policy.detectAll || policy.detectNonSdkApiUsage) {
                if (VERSION.SDK_INT >= VERSION_CODES.P) {
                    detectNonSdkApiUsage()
                } else {
                    Logger.logUnsupportedFeature(
                        StrictPro.VmPolicy.CATEGORY,
                        "detectNonSdkApiUsage"
                    )
                }
            }
            .doIf(policy.detectContentUriWithoutPermission) {
                if (VERSION.SDK_INT >= VERSION_CODES.O) {
                    detectContentUriWithoutPermission()
                } else {
                    Logger.logUnsupportedFeature(
                        StrictPro.VmPolicy.CATEGORY,
                        "detectContentUriWithoutPermission"
                    )
                }
            }
            .doIf(policy.detectImplicitDirectBoot) {
                if (VERSION.SDK_INT >= VERSION_CODES.Q) {
                    detectImplicitDirectBoot()
                } else {
                    Logger.logUnsupportedFeature(
                        StrictPro.VmPolicy.CATEGORY,
                        "detectImplicitDirectBoot"
                    )
                }
            }
            .doIf(policy.detectIncorrectContextUse) {
                if (VERSION.SDK_INT >= VERSION_CODES.S) {
                    detectIncorrectContextUse()
                } else {
                    Logger.logUnsupportedFeature(
                        StrictPro.VmPolicy.CATEGORY,
                        "detectIncorrectContextUse"
                    )
                }
            }
            .doIf(policy.detectLeakedClosableObjects) { detectLeakedClosableObjects() }
            .doIf(policy.detectLeakedRegistrationObjects) { detectLeakedRegistrationObjects() }
            .doIf(policy.detectUnsafeIntentLaunch) {
                if (VERSION.SDK_INT >= VERSION_CODES.S) {
                    detectUnsafeIntentLaunch()
                } else {
                    Logger.logUnsupportedFeature(
                        StrictPro.VmPolicy.CATEGORY,
                        "detectUnsafeIntentLaunch"
                    )
                }
            }
            .doIf(policy.detectFileUriExposure) { detectFileUriExposure() }
            .doIf(policy.detectCredentialProtectedWhileLocked) {
                if (VERSION.SDK_INT >= VERSION_CODES.Q) {
                    detectCredentialProtectedWhileLocked()
                } else {
                    Logger.logUnsupportedFeature(
                        StrictPro.VmPolicy.CATEGORY,
                        "detectCredentialProtectedWhileLocked"
                    )
                }
            }
            .doIf(policy.detectLeakedSqlLiteObjects) { detectLeakedSqlLiteObjects() }
    }

    private fun VmPolicy.Builder.applyPermits(policy: StrictPro.VmPolicy): VmPolicy.Builder {
        return doIf(policy.permitNonSdkApiUsage) {
            if (VERSION.SDK_INT >= VERSION_CODES.P) {
                permitNonSdkApiUsage()
            } else {
                Logger.logUnsupportedFeature(
                    StrictPro.VmPolicy.CATEGORY,
                    "permitNonSdkApiUsage"
                )
            }
        }
            .doIf(policy.permitUnsafeIntentLaunch) {
                if (VERSION.SDK_INT >= VERSION_CODES.S) {
                    permitUnsafeIntentLaunch()
                } else {
                    Logger.logUnsupportedFeature(
                        StrictPro.VmPolicy.CATEGORY,
                        "permitUnsafeIntentLaunch"
                    )
                }
            }
    }

    private fun VmPolicy.Builder.applyOther(policy: StrictPro.VmPolicy): VmPolicy.Builder {
        if (policy.getPenaltyListeners().isNotEmpty()) {
            if (VERSION.SDK_INT >= VERSION_CODES.P) {
                /*
                * Listeners are called in `applyStrictProPenalties`.
                * */
            } else {
                Logger.logUnsupportedFeature(
                    StrictPro.VmPolicy.CATEGORY,
                    "penaltyListener"
                )
            }
        }

        policy.getClassInstanceLimits().forEach { (clazz, limit) ->
            setClassInstanceLimit(clazz, limit)
        }
        return this
    }

    private fun VmPolicy.Builder.applyPenalties(policy: StrictPro.VmPolicy): VmPolicy.Builder {
        return if (VERSION.SDK_INT >= VERSION_CODES.P) {
            applyStrictProPenalties(policy)
        } else {
            if (policy.violationWhiteList.containsConditions()) {
                Logger.logUnsupportedFeature(
                    StrictPro.VmPolicy.CATEGORY,
                    "setWhiteList"
                )
            }
            applyStrictModePenalties(policy.getPenalties())
        }
    }

    @RequiresApi(VERSION_CODES.P)
    private fun VmPolicy.Builder.applyStrictProPenalties(
        policy: StrictPro.VmPolicy
    ): VmPolicy.Builder {
        return penaltyListener(MainThreadExecutor()) { violation ->
            Logger.logDebug("VmPolicySetter.penaltyListener: $violation")

            val whiteListPenalties =
                policy.violationWhiteList.getWhiteListPenalties(violation)
            if (whiteListPenalties.contains(Ignore)) {
                Logger.logDebug("VmPolicySetter.penaltyListener: ignoring penalties")
            } else {
                val penalties = whiteListPenalties.ifEmpty {
                    Logger.logDebug("VmPolicySetter.penaltyListener: no white list penalties")
                    VmPolicyPenaltiesCreator.create(policy, violation)
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
                    listener.onVmViolation(violation)
                }
            }
        }
    }

    private fun VmPolicy.Builder.applyStrictModePenalties(
        penalties: Set<ViolationPenalty>
    ): VmPolicy.Builder {
        return doIf(penalties.contains(Log)) { penaltyLog() }
            .doIf(penalties.contains(Death)) { penaltyDeath() }
            .doIf(penalties.contains(DropBox)) { penaltyDropBox() }
            .doIf(penalties.contains(DeathOnFileUriExposure)) {
                if (VERSION.SDK_INT >= VERSION_CODES.N) {
                    penaltyDeathOnFileUriExposure()
                } else {
                    Logger.logUnsupportedFeature(
                        StrictPro.VmPolicy.CATEGORY,
                        "penaltyDeathOnFileUriExposure"
                    )
                }
            }
            .doIf(penalties.contains(DeathOnCleartextNetwork)) {
                if (VERSION.SDK_INT >= VERSION_CODES.M) {
                    penaltyDeathOnCleartextNetwork()
                } else {
                    Logger.logUnsupportedFeature(
                        StrictPro.VmPolicy.CATEGORY,
                        "penaltyDeathOnCleartextNetwork"
                    )
                }
            }
    }
}