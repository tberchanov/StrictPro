package com.strictpro.policy

import android.os.Build
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import com.strictpro.StrictPro
import com.strictpro.penalty.ViolationPenalty
import com.strictpro.penalty.ViolationPenalty.Death
import com.strictpro.penalty.ViolationPenalty.DeathOnCleartextNetwork
import com.strictpro.penalty.ViolationPenalty.DeathOnFileUriExposure
import com.strictpro.penalty.ViolationPenalty.DropBox
import com.strictpro.penalty.ViolationPenalty.Log
import com.strictpro.penalty.creator.VmPolicyPenaltiesCreator
import com.strictpro.utils.MainThreadExecutor
import com.strictpro.utils.UnsupportedLogger
import com.strictpro.utils.doIf

internal object VmPolicySetter {

    fun set(policy: StrictPro.VmPolicy) {
        val androidPolicy = VmPolicy.Builder()
            .doIf(policy.detectAll) { detectAll() }
            .doIf(policy.detectActivityLeaks) { detectActivityLeaks() }
            .doIf(policy.detectUntaggedSockets) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    detectUntaggedSockets()
                } else {
                    UnsupportedLogger.logUnsupportedFeature(
                        StrictPro.VmPolicy.CATEGORY,
                        "detectUntaggedSockets"
                    )
                }
            }
            .doIf(policy.detectCleartextNetwork) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    detectCleartextNetwork()
                } else {
                    UnsupportedLogger.logUnsupportedFeature(
                        StrictPro.VmPolicy.CATEGORY,
                        "detectCleartextNetwork"
                    )
                }
            }
            .doIf(policy.detectNonSdkApiUsage) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    detectNonSdkApiUsage()
                } else {
                    UnsupportedLogger.logUnsupportedFeature(
                        StrictPro.VmPolicy.CATEGORY,
                        "detectNonSdkApiUsage"
                    )
                }
            }
            .doIf(policy.detectContentUriWithoutPermission) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    detectContentUriWithoutPermission()
                } else {
                    UnsupportedLogger.logUnsupportedFeature(
                        StrictPro.VmPolicy.CATEGORY,
                        "detectContentUriWithoutPermission"
                    )
                }
            }
            .doIf(policy.detectImplicitDirectBoot) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    detectImplicitDirectBoot()
                } else {
                    UnsupportedLogger.logUnsupportedFeature(
                        StrictPro.VmPolicy.CATEGORY,
                        "detectImplicitDirectBoot"
                    )
                }
            }
            .doIf(policy.detectIncorrectContextUse) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    detectIncorrectContextUse()
                } else {
                    UnsupportedLogger.logUnsupportedFeature(
                        StrictPro.VmPolicy.CATEGORY,
                        "detectIncorrectContextUse"
                    )
                }
            }
            .doIf(policy.detectLeakedClosableObjects) { detectLeakedClosableObjects() }
            .doIf(policy.detectLeakedRegistrationObjects) { detectLeakedRegistrationObjects() }
            .doIf(policy.detectUnsafeIntentLaunch) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    detectUnsafeIntentLaunch()
                } else {
                    UnsupportedLogger.logUnsupportedFeature(
                        StrictPro.VmPolicy.CATEGORY,
                        "detectUnsafeIntentLaunch"
                    )
                }
            }
            .doIf(policy.detectFileUriExposure) { detectFileUriExposure() }
            .doIf(policy.detectCredentialProtectedWhileLocked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    detectCredentialProtectedWhileLocked()
                } else {
                    UnsupportedLogger.logUnsupportedFeature(
                        StrictPro.VmPolicy.CATEGORY,
                        "detectCredentialProtectedWhileLocked"
                    )
                }
            }
            .doIf(policy.detectLeakedSqlLiteObjects) { detectLeakedSqlLiteObjects() }
            .apply {
                UnsupportedLogger.logUnsupportedFeature(
                    StrictPro.VmPolicy.CATEGORY,
                    "penaltyListener"
                )
            }
            .apply {
                policy.getClassInstanceLimits().forEach { (clazz, limit) ->
                    setClassInstanceLimit(clazz, limit)
                }
            }
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    penaltyListener(MainThreadExecutor()) { violation ->
                        val whiteListPenalties =
                            policy.violationWhiteList.getWhiteListPenalties(violation)
                        if (whiteListPenalties.isEmpty()) {
                            val policyPenalties =
                                VmPolicyPenaltiesCreator.create(policy, violation)
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
                        StrictPro.VmPolicy.CATEGORY,
                        "setWhiteList"
                    )
                    val penalties = policy.getPenalties()
                    doIf(penalties.contains(Log)) { penaltyLog() }
                    doIf(penalties.contains(Death)) { penaltyDeath() }
                    doIf(penalties.contains(DropBox)) { penaltyDropBox() }
                    doIf(penalties.contains(DeathOnFileUriExposure)) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            penaltyDeathOnFileUriExposure()
                        } else {
                            UnsupportedLogger.logUnsupportedFeature(
                                StrictPro.VmPolicy.CATEGORY,
                                "penaltyDeathOnFileUriExposure"
                            )
                        }
                    }
                    doIf(penalties.contains(DeathOnCleartextNetwork)) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            penaltyDeathOnCleartextNetwork()
                        } else {
                            UnsupportedLogger.logUnsupportedFeature(
                                StrictPro.VmPolicy.CATEGORY,
                                "penaltyDeathOnCleartextNetwork"
                            )
                        }
                    }
                }
            }
            .build()
        StrictMode.setVmPolicy(androidPolicy)
    }
}