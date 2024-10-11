package com.strictpro.policy

import android.os.Build
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import com.strictpro.StrictPro
import com.strictpro.penalty.ViolationPenalty
import com.strictpro.penalty.creator.VmPolicyPenaltiesCreator
import com.strictpro.utils.MainThreadExecutor
import com.strictpro.utils.UnsupportedLogger
import com.strictpro.utils.doIf

internal object VmPolicySetter {

    fun set(policy: StrictPro.VmPolicy) {
        val androidPolicy = VmPolicy.Builder()
            .doIf(policy.detectAll) { detectAll() }
            .doIf(policy.detectActivityLeaks) { detectActivityLeaks() }
            .doIf(policy.detectUntaggedSockets) { detectUntaggedSockets() }
            .doIf(policy.detectCleartextNetwork) { detectCleartextNetwork() }
            .doIf(policy.detectNonSdkApiUsage) { detectNonSdkApiUsage() }
            .doIf(policy.detectContentUriWithoutPermission) { detectContentUriWithoutPermission() }
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
                policy.getPenaltyListeners().forEach { (executor, listener) ->
                    penaltyListener(executor, listener)
                }
            }
            .apply {
                policy.getClassInstanceLimits().forEach { (clazz, limit) ->
                    setClassInstanceLimit(clazz, limit)
                }
            }
            .penaltyListener(MainThreadExecutor()) { violation ->
                val whiteListPenalties = policy.violationWhiteList.getWhiteListPenalties(violation)
                if (whiteListPenalties.isEmpty()) {
                    val policyPenalties = VmPolicyPenaltiesCreator.create(policy, violation)
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
        StrictMode.setVmPolicy(androidPolicy)
    }
}