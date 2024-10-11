package com.strictpro

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.StrictMode.OnThreadViolationListener
import android.os.StrictMode.OnVmViolationListener
import com.strictpro.penalty.ViolationPenalty
import com.strictpro.penalty.executor.CompositePenaltyExecutor
import com.strictpro.penalty.executor.DeathPenaltyExecutor
import com.strictpro.penalty.executor.DialogPenaltyExecutor
import com.strictpro.penalty.executor.DropBoxPenaltyExecutor
import com.strictpro.penalty.executor.FlashScreenPenaltyExecutor
import com.strictpro.penalty.executor.LogPenaltyExecutor
import com.strictpro.policy.ThreadPolicySetter
import com.strictpro.policy.VmPolicySetter
import com.strictpro.utils.DefaultActivityLifecycleCallbacks
import java.lang.ref.WeakReference
import java.util.concurrent.Executor

object StrictPro {

    internal var currentActivityRef: WeakReference<Activity> = WeakReference(null)

    internal fun listenActivities(context: Context) {
        (context.applicationContext as Application).registerActivityLifecycleCallbacks(
            object : DefaultActivityLifecycleCallbacks() {
                override fun onActivityCreated(
                    activity: Activity,
                    savedInstanceState: android.os.Bundle?
                ) {
                    currentActivityRef = WeakReference(activity)
                }

                override fun onActivityStarted(activity: Activity) {
                    currentActivityRef = WeakReference(activity)
                }

                override fun onActivityDestroyed(activity: Activity) {
                    currentActivityRef = WeakReference(null)
                }
            }
        )
    }

    internal val penaltyExecutor = CompositePenaltyExecutor(
        listOf(
            LogPenaltyExecutor(),
            DropBoxPenaltyExecutor(),
            DialogPenaltyExecutor(),
            FlashScreenPenaltyExecutor(),
            DeathPenaltyExecutor(),
            // TODO Add NotificationPenaltyExecutor, ToastPenaltyExecutor, VibrationPenaltyExecutor, SoundPenaltyExecutor
        )
    )

    class ThreadPolicy {

        internal var detectAll = false
        internal var detectExplicitGc = false
        internal var detectDiskReads = false
        internal var detectDiskWrites = false
        internal var detectNetwork = false
        internal var detectCustomSlowCalls = false
        internal var detectResourceMismatches = false
        internal var detectUnbufferedIo = false

        internal var permitAll = false
        internal var permitExplicitGc = false
        internal var permitDiskReads = false
        internal var permitDiskWrites = false
        internal var permitNetwork = false
        internal var permitCustomSlowCalls = false
        internal var permitResourceMismatches = false
        internal var permitUnbufferedIo = false

        internal val violationWhiteList = ViolationWhiteList()
        private val penalties = mutableSetOf<ViolationPenalty>()
        private val penaltyListeners = mutableListOf<Pair<Executor, OnThreadViolationListener>>()

        internal fun getPenalties(): Set<ViolationPenalty> = penalties
        internal fun getPenaltyListeners(): List<Pair<Executor, OnThreadViolationListener>> =
            penaltyListeners

        class Builder {

            private val threadPolicy = ThreadPolicy()

            fun detectAll(): Builder {
                threadPolicy.detectAll = true
                return this
            }

            fun detectExplicitGc(): Builder {
                threadPolicy.detectExplicitGc = true
                return this
            }

            fun detectDiskReads(): Builder {
                threadPolicy.detectDiskReads = true
                return this
            }

            fun detectDiskWrites(): Builder {
                threadPolicy.detectDiskWrites = true
                return this
            }

            fun detectNetwork(): Builder {
                threadPolicy.detectNetwork = true
                return this
            }

            fun detectCustomSlowCalls(): Builder {
                threadPolicy.detectCustomSlowCalls = true
                return this
            }

            fun detectResourceMismatches(): Builder {
                threadPolicy.detectResourceMismatches = true
                return this
            }

            fun detectUnbufferedIo(): Builder {
                threadPolicy.detectUnbufferedIo = true
                return this
            }

            fun permitAll(): Builder {
                threadPolicy.permitAll = true
                return this
            }

            fun permitExplicitGc(): Builder {
                threadPolicy.permitExplicitGc = true
                return this
            }

            fun permitDiskReads(): Builder {
                threadPolicy.permitDiskReads = true
                return this
            }

            fun permitDiskWrites(): Builder {
                threadPolicy.permitDiskWrites = true
                return this
            }

            fun permitNetwork(): Builder {
                threadPolicy.permitNetwork = true
                return this
            }

            fun permitCustomSlowCalls(): Builder {
                threadPolicy.permitCustomSlowCalls = true
                return this
            }

            fun permitResourceMismatches(): Builder {
                threadPolicy.permitResourceMismatches = true
                return this
            }

            fun permitUnbufferedIo(): Builder {
                threadPolicy.permitUnbufferedIo = true
                return this
            }

            fun penaltyLog(): Builder {
                threadPolicy.penalties.add(ViolationPenalty.Log)
                return this
            }

            fun penaltyDropBox(): Builder {
                threadPolicy.penalties.add(ViolationPenalty.DropBox)
                return this
            }

            fun penaltyDialog(): Builder {
                threadPolicy.penalties.add(ViolationPenalty.Dialog)
                return this
            }

            fun penaltyDeath(): Builder {
                threadPolicy.penalties.add(ViolationPenalty.Death)
                return this
            }

            fun penaltyFlashScreen(): Builder {
                threadPolicy.penalties.add(ViolationPenalty.FlashScreen)
                return this
            }

            fun penaltyDeathOnNetwork(): Builder {
                threadPolicy.penalties.add(ViolationPenalty.DeathOnNetwork)
                return this
            }

            fun penaltyListener(executor: Executor, listener: OnThreadViolationListener): Builder {
                threadPolicy.penaltyListeners.add(executor to listener)
                return this
            }

            fun setWhiteList(whiteListBlock: ViolationWhiteList.() -> Unit): Builder {
                threadPolicy.violationWhiteList.whiteListBlock()
                return this
            }

            fun build(): ThreadPolicy {
                return threadPolicy
            }
        }

        companion object {
            internal const val CATEGORY = "ThreadPolicy"
        }
    }

    fun setThreadPolicy(policy: ThreadPolicy) {
        ThreadPolicySetter.set(policy)
    }

    class VmPolicy {

        internal var detectAll = false
        internal var detectActivityLeaks = false
        internal var detectUntaggedSockets = false
        internal var detectCleartextNetwork = false
        internal var detectNonSdkApiUsage = false
        internal var detectContentUriWithoutPermission = false
        internal var detectImplicitDirectBoot = false
        internal var detectIncorrectContextUse = false
        internal var detectLeakedClosableObjects = false
        internal var detectLeakedRegistrationObjects = false
        internal var detectUnsafeIntentLaunch = false
        internal var detectFileUriExposure = false
        internal var detectCredentialProtectedWhileLocked = false
        internal var detectLeakedSqlLiteObjects = false

        internal var permitNonSdkApiUsage = false
        internal var permitUnsafeIntentLaunch = false

        internal val violationWhiteList = ViolationWhiteList()
        private val penalties = mutableSetOf<ViolationPenalty>()
        private val penaltyListeners = mutableListOf<Pair<Executor, OnVmViolationListener>>()
        private val classInstanceLimits = mutableMapOf<Class<*>, Int>()

        internal fun getPenalties(): Set<ViolationPenalty> = penalties
        internal fun getPenaltyListeners(): List<Pair<Executor, OnVmViolationListener>> =
            penaltyListeners

        internal fun getClassInstanceLimits(): Map<Class<*>, Int> = classInstanceLimits

        class Builder {

            private val vmPolicy = VmPolicy()

            fun detectAll(): Builder {
                vmPolicy.detectAll = true
                return this
            }

            fun detectActivityLeaks(): Builder {
                vmPolicy.detectActivityLeaks = true
                return this
            }

            fun detectUntaggedSockets(): Builder {
                vmPolicy.detectUntaggedSockets = true
                return this
            }

            fun detectCleartextNetwork(): Builder {
                vmPolicy.detectCleartextNetwork = true
                return this
            }

            fun detectNonSdkApiUsage(): Builder {
                vmPolicy.detectNonSdkApiUsage = true
                return this
            }

            fun detectContentUriWithoutPermission(): Builder {
                vmPolicy.detectContentUriWithoutPermission = true
                return this
            }

            fun detectImplicitDirectBoot(): Builder {
                vmPolicy.detectImplicitDirectBoot = true
                return this
            }

            fun detectIncorrectContextUse(): Builder {
                vmPolicy.detectIncorrectContextUse = true
                return this
            }

            fun detectLeakedClosableObjects(): Builder {
                vmPolicy.detectLeakedClosableObjects = true
                return this
            }

            fun detectLeakedRegistrationObjects(): Builder {
                vmPolicy.detectLeakedRegistrationObjects = true
                return this
            }

            fun detectUnsafeIntentLaunch(): Builder {
                vmPolicy.detectUnsafeIntentLaunch = true
                return this
            }

            fun detectFileUriExposure(): Builder {
                vmPolicy.detectFileUriExposure = true
                return this
            }

            fun detectCredentialProtectedWhileLocked(): Builder {
                vmPolicy.detectCredentialProtectedWhileLocked = true
                return this
            }

            fun detectLeakedSqlLiteObjects(): Builder {
                vmPolicy.detectLeakedSqlLiteObjects = true
                return this
            }

            fun permitNonSdkApiUsage(): Builder {
                vmPolicy.permitNonSdkApiUsage = true
                return this
            }

            fun permitUnsafeIntentLaunch(): Builder {
                vmPolicy.permitUnsafeIntentLaunch = true
                return this
            }

            fun penaltyDeath(): Builder {
                vmPolicy.penalties.add(ViolationPenalty.Death)
                return this
            }

            fun penaltyDropBox(): Builder {
                vmPolicy.penalties.add(ViolationPenalty.DropBox)
                return this
            }

            fun penaltyDeathOnCleartextNetwork(): Builder {
                vmPolicy.penalties.add(ViolationPenalty.DeathOnCleartextNetwork)
                return this
            }

            fun penaltyDeathOnFileUriExposure(): Builder {
                vmPolicy.penalties.add(ViolationPenalty.DeathOnFileUriExposure)
                return this
            }

            fun penaltyLog(): Builder {
                vmPolicy.penalties.add(ViolationPenalty.Log)
                return this
            }

            fun penaltyDialog(): Builder {
                vmPolicy.penalties.add(ViolationPenalty.Dialog)
                return this
            }

            fun penaltyFlashScreen(): Builder {
                vmPolicy.penalties.add(ViolationPenalty.FlashScreen)
                return this
            }

            fun penaltyListener(executor: Executor, listener: OnVmViolationListener): Builder {
                vmPolicy.penaltyListeners.add(executor to listener)
                return this
            }

            fun setClassInstanceLimit(clazz: Class<*>, limit: Int): Builder {
                vmPolicy.classInstanceLimits[clazz] = limit
                return this
            }

            fun setWhiteList(whiteListBlock: ViolationWhiteList.() -> Unit): Builder {
                vmPolicy.violationWhiteList.whiteListBlock()
                return this
            }

            fun build(): VmPolicy {
                return vmPolicy
            }
        }

        companion object {
            internal const val CATEGORY = "VmPolicy"
        }
    }

    fun setVmPolicy(policy: VmPolicy) {
        VmPolicySetter.set(policy)
    }
}