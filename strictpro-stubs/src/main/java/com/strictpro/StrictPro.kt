package com.strictpro

import android.os.StrictMode.OnThreadViolationListener
import android.os.StrictMode.OnVmViolationListener
import java.util.concurrent.Executor

object StrictPro {

    var printDebugLogs = false

    class ThreadPolicy internal constructor() {

        class Builder {

            fun detectAll(): Builder {
                return this
            }

            fun detectExplicitGc(): Builder {
                return this
            }

            fun detectDiskReads(): Builder {
                return this
            }

            fun detectDiskWrites(): Builder {
                return this
            }

            fun detectNetwork(): Builder {
                return this
            }

            fun detectCustomSlowCalls(): Builder {
                return this
            }

            fun detectResourceMismatches(): Builder {
                return this
            }

            fun detectUnbufferedIo(): Builder {
                return this
            }

            fun permitAll(): Builder {
                return this
            }

            fun permitExplicitGc(): Builder {
                return this
            }

            fun permitDiskReads(): Builder {
                return this
            }

            fun permitDiskWrites(): Builder {
                return this
            }

            fun permitNetwork(): Builder {
                return this
            }

            fun permitCustomSlowCalls(): Builder {
                return this
            }

            fun permitResourceMismatches(): Builder {
                return this
            }

            fun permitUnbufferedIo(): Builder {
                return this
            }

            fun penaltyLog(): Builder {
                return this
            }

            fun penaltyDropBox(): Builder {
                return this
            }

            fun penaltyDialog(): Builder {
                return this
            }

            fun penaltyDeath(): Builder {
                return this
            }

            fun penaltyFlashScreen(): Builder {
                return this
            }

            fun penaltyDeathOnNetwork(): Builder {
                return this
            }

            fun penaltyListener(executor: Executor, listener: OnThreadViolationListener): Builder {
                return this
            }

            fun setWhiteList(whiteListBlock: ViolationWhiteList.() -> Unit): Builder {
                return this
            }

            fun build(): ThreadPolicy {
                return ThreadPolicy()
            }
        }
    }

    fun setThreadPolicy(policy: ThreadPolicy) {
    }

    class VmPolicy internal constructor() {

        class Builder {

            fun detectAll(): Builder {
                return this
            }

            fun detectActivityLeaks(): Builder {
                return this
            }

            fun detectUntaggedSockets(): Builder {
                return this
            }

            fun detectCleartextNetwork(): Builder {
                return this
            }

            fun detectNonSdkApiUsage(): Builder {
                return this
            }

            fun detectContentUriWithoutPermission(): Builder {
                return this
            }

            fun detectImplicitDirectBoot(): Builder {
                return this
            }

            fun detectIncorrectContextUse(): Builder {
                return this
            }

            fun detectLeakedClosableObjects(): Builder {
                return this
            }

            fun detectLeakedRegistrationObjects(): Builder {
                return this
            }

            fun detectUnsafeIntentLaunch(): Builder {
                return this
            }

            fun detectFileUriExposure(): Builder {
                return this
            }

            fun detectCredentialProtectedWhileLocked(): Builder {
                return this
            }

            fun detectLeakedSqlLiteObjects(): Builder {
                return this
            }

            fun permitNonSdkApiUsage(): Builder {
                return this
            }

            fun permitUnsafeIntentLaunch(): Builder {
                return this
            }

            fun penaltyDeath(): Builder {
                return this
            }

            fun penaltyDropBox(): Builder {
                return this
            }

            fun penaltyDeathOnCleartextNetwork(): Builder {
                return this
            }

            fun penaltyDeathOnFileUriExposure(): Builder {
                return this
            }

            fun penaltyLog(): Builder {
                return this
            }

            fun penaltyDialog(): Builder {
                return this
            }

            fun penaltyFlashScreen(): Builder {
                return this
            }

            fun penaltyListener(executor: Executor, listener: OnVmViolationListener): Builder {
                return this
            }

            fun setClassInstanceLimit(clazz: Class<*>, limit: Int): Builder {
                return this
            }

            fun setWhiteList(whiteListBlock: ViolationWhiteList.() -> Unit): Builder {
                return this
            }

            fun build(): VmPolicy {
                return VmPolicy()
            }
        }
    }

    fun setVmPolicy(policy: VmPolicy) {
    }
}