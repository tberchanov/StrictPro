package com.strictpro

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.StrictMode.OnThreadViolationListener
import android.os.StrictMode.OnVmViolationListener
import com.strictpro.penalty.ViolationPenalty
import com.strictpro.penalty.executor.CompositePenaltyExecutor
import com.strictpro.penalty.executor.DeathPenaltyExecutor
import com.strictpro.penalty.executor.DialogPenaltyExecutor
import com.strictpro.penalty.executor.DropBoxPenaltyExecutor
import com.strictpro.penalty.executor.FlashScreenPenaltyExecutor
import com.strictpro.penalty.executor.LogPenaltyExecutor
import com.strictpro.penalty.executor.PenaltyExecutor
import com.strictpro.policy.ThreadPolicySetter
import com.strictpro.policy.VmPolicySetter
import com.strictpro.utils.DefaultActivityLifecycleCallbacks
import com.strictpro.utils.Logger
import com.strictpro.utils.VisibleForTestingOnly_DoNotUseInProductionCode
import java.lang.ref.WeakReference
import java.util.concurrent.Executor

/**
 * StrictPro is a wrapper utility around {@link android.os.StrictMode}.
 *
 * StrictMode is a developer tool which detects things you might be doing by accident and brings
 * them to your attention so you can fix them.
 *
 * <p>StrictMode is most commonly used to catch accidental disk or network access on the
 * application's main thread, where UI operations are received and animations take place. Keeping
 * disk and network operations off the main thread makes for much smoother, more responsive
 * applications. By keeping your application's main thread responsive, you also prevent <a
 * href="{@docRoot}guide/practices/design/responsiveness.html">ANR dialogs</a> from being shown to
 * users.
 *
 * <p class="note">Note that even though an Android device's disk is often on flash memory, many
 * devices run a filesystem on top of that memory with very limited concurrency. It's often the case
 * that almost all disk accesses are fast, but may in individual cases be dramatically slower when
 * certain I/O is happening in the background from other processes. If possible, it's best to assume
 * that such things are not fast.
 *
 * <p>Example code to enable from early in your {@link android.app.Application}, {@link
 * android.app.Activity}, or other application component's {@link android.app.Application#onCreate}
 * method:
 *
 * <pre>
 * override fun onCreate() {
 *     StrictPro.setThreadPolicy(StrictPro.ThreadPolicy.Builder()
 *         .detectDiskReads()
 *         .detectDiskWrites()
 *         .detectNetwork()   // or .detectAll() for all detectable problems
 *         .penaltyLog()
 *         .build());
 *     StrictPro.setVmPolicy(StrictPro.VmPolicy.Builder()
 *         .detectLeakedSqlLiteObjects()
 *         .detectLeakedClosableObjects()
 *         .penaltyLog()
 *         .penaltyDeath()
 *         .build())
 *     super.onCreate()
 * }
 * </pre>
 *
 * <p>You can decide what should happen when a violation is detected. For example, using {@link
 * StrictPro.ThreadPolicy.Builder#penaltyLog} you can watch the output of <code>adb logcat</code> while you
 * use your application to see the violations as they happen.
 *
 * <p>If you find violations that you feel are problematic, there are a variety of tools to help
 * solve them: threads, {@link android.os.Handler}, {@link android.os.AsyncTask}, {@link
 * android.app.IntentService}, etc. But don't feel compelled to fix everything that StrictMode
 * finds. In particular, many cases of disk access are often necessary during the normal activity
 * lifecycle. Use StrictMode to find things you did by accident. Network requests on the UI thread
 * are almost always a problem, though.
 *
 * <p class="note">StrictMode is not a security mechanism and is not guaranteed to find all disk or
 * network accesses. While it does propagate its state across process boundaries when doing {@link
 * android.os.Binder} calls, it's still ultimately a best effort mechanism. Notably, disk or network
 * access from JNI calls won't necessarily trigger it.
 */
object StrictPro {

    internal var currentActivityRef: WeakReference<Activity> = WeakReference(null)

    internal fun listenActivities(context: Context) {
        (context.applicationContext as Application).registerActivityLifecycleCallbacks(
            object : DefaultActivityLifecycleCallbacks() {
                override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
                    currentActivityRef = WeakReference(activity)
                }
                override fun onActivityResumed(activity: Activity) {
                    currentActivityRef = WeakReference(activity)
                }
            }
        )
    }

    internal var penaltyExecutor = CompositePenaltyExecutor(
        /**
         * Creation order is important.
         * ViolationPenalties are executing according to executors order.
         * That's why fatal executors (DeathPenaltyExecutor) should execute lastly,
         * to let other executors complete.
         */
        listOf(
            LogPenaltyExecutor(),
            DropBoxPenaltyExecutor(),
            DialogPenaltyExecutor(),
            FlashScreenPenaltyExecutor(),
            DeathPenaltyExecutor(),
        )
    )

    /**
     * Flag to enable or disable printing of debug logs.
     *
     * When `printDebugLogs` is set to `true`, additional debug information will be logged
     * to help with troubleshooting and development. This can include detailed information
     * about policy violations, penalties applied, and other internal operations of the
     * `StrictPro` utility.
     *
     * When `printDebugLogs` is set to `false`, debug logging is disabled, and only essential
     * logs will be printed. This is the default setting to avoid cluttering the log output
     * with too much information.
     */
    var printDebugLogs = false

    /**
     * {@link StrictPro} policy applied to a certain thread.
     *
     * <p>The policy is enabled by {@link #setThreadPolicy}.
     *
     * <p>Note that multiple penalties may be provided and they're run in order from least to most
     * severe (logging before process death, for example).
     *
     * To choose different penalties for different detected actions use {@link #setWhiteList}.
     */
    class ThreadPolicy internal constructor() {

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

        override fun toString(): String {
            return "ThreadPolicy(detectAll=$detectAll, detectExplicitGc=$detectExplicitGc, " +
                "detectDiskReads=$detectDiskReads, detectDiskWrites=$detectDiskWrites, " +
                "detectNetwork=$detectNetwork, detectCustomSlowCalls=$detectCustomSlowCalls, " +
                "detectResourceMismatches=$detectResourceMismatches, " +
                "detectUnbufferedIo=$detectUnbufferedIo, permitAll=$permitAll, " +
                "permitExplicitGc=$permitExplicitGc, permitDiskReads=$permitDiskReads, " +
                "permitDiskWrites=$permitDiskWrites, permitNetwork=$permitNetwork, " +
                "permitCustomSlowCalls=$permitCustomSlowCalls, " +
                "permitResourceMismatches=$permitResourceMismatches, " +
                "permitUnbufferedIo=$permitUnbufferedIo, violationWhiteList=$violationWhiteList, " +
                "penalties=$penalties, penaltyListeners=$penaltyListeners)"
        }

        /**
         * Creates {@link ThreadPolicy} instances. Methods whose names start with {@code detect}
         * specify what problems we should look for. Methods whose names start with {@code penalty}
         * specify what we should do when we detect a problem.
         *
         * <p>You can call as many {@code detect} and {@code penalty} methods as you like. Currently
         * order is insignificant: all penalties apply to all detected problems.
         *
         * <p>For example, detect everything and log anything that's found:
         *
         * <pre>
         * val policy = StrictPro.ThreadPolicy.Builder()
         *     .detectAll()
         *     .penaltyLog()
         *     .build()
         * StrictPro.setThreadPolicy(policy)
         * </pre>
         */
        class Builder {

            private val threadPolicy = ThreadPolicy()

            /**
             * Detect everything that's potentially suspect.
             *
             * <p>As of the Gingerbread release this includes network and disk operations but will
             * likely expand in future releases.
             */
            fun detectAll(): Builder {
                threadPolicy.detectAll = true
                return this
            }

            /**
             * Detect calls to {@link Runtime#gc()}.
             *
             * Call requires API level 34 or higher, otherwise it will be ignored.
             */
            fun detectExplicitGc(): Builder {
                threadPolicy.detectExplicitGc = true
                return this
            }

            /** Enable detection of disk reads. */
            fun detectDiskReads(): Builder {
                threadPolicy.detectDiskReads = true
                return this
            }

            /** Enable detection of disk writes. */
            fun detectDiskWrites(): Builder {
                threadPolicy.detectDiskWrites = true
                return this
            }

            /** Enable detection of network operations. */
            fun detectNetwork(): Builder {
                threadPolicy.detectNetwork = true
                return this
            }

            /** Enable detection of slow calls. */
            fun detectCustomSlowCalls(): Builder {
                threadPolicy.detectCustomSlowCalls = true
                return this
            }

            /**
             * Enables detection of mismatches between defined resource types and getter calls.
             *
             * <p>This helps detect accidental type mismatches and potentially expensive type
             * conversions when obtaining typed resources.
             *
             * <p>For example, a strict mode violation would be thrown when calling {@link
             * android.content.res.TypedArray#getInt(int, int)} on an index that contains a
             * String-type resource. If the string value can be parsed as an integer, this method
             * call will return a value without crashing; however, the developer should format the
             * resource as an integer to avoid unnecessary type conversion.
             *
             * Call requires API level 23 or higher, otherwise it will be ignored.
             */
            fun detectResourceMismatches(): Builder {
                threadPolicy.detectResourceMismatches = true
                return this
            }

            /**
             * Detect unbuffered input/output operations.
             * Call requires API level 26 or higher, otherwise it will be ignored.
             * */
            fun detectUnbufferedIo(): Builder {
                threadPolicy.detectUnbufferedIo = true
                return this
            }

            /** Disable the detection of everything. */
            fun permitAll(): Builder {
                threadPolicy.permitAll = true
                return this
            }

            /**
             * Disable detection of calls to {@link Runtime#gc()}.
             *
             * Call requires API level 34 or higher, otherwise it will be ignored.
             */
            fun permitExplicitGc(): Builder {
                threadPolicy.permitExplicitGc = true
                return this
            }

            /** Disable detection of disk reads. */
            fun permitDiskReads(): Builder {
                threadPolicy.permitDiskReads = true
                return this
            }

            /** Disable detection of disk writes. */
            fun permitDiskWrites(): Builder {
                threadPolicy.permitDiskWrites = true
                return this
            }

            /** Disable detection of network operations. */
            fun permitNetwork(): Builder {
                threadPolicy.permitNetwork = true
                return this
            }

            /** Disable detection of slow calls. */
            fun permitCustomSlowCalls(): Builder {
                threadPolicy.permitCustomSlowCalls = true
                return this
            }

            /**
             * Disable detection of mismatches between defined resource types and getter calls.
             * Call requires API level 23 or higher, otherwise it will be ignored.
             * */
            fun permitResourceMismatches(): Builder {
                threadPolicy.permitResourceMismatches = true
                return this
            }

            /**
             * Disable detection of unbuffered input/output operations.
             * Call requires API level 26 or higher, otherwise it will be ignored.
             * */
            fun permitUnbufferedIo(): Builder {
                threadPolicy.permitUnbufferedIo = true
                return this
            }

            /** Log detected violations to the system log. */
            fun penaltyLog(): Builder {
                threadPolicy.penalties.add(ViolationPenalty.Log)
                return this
            }

            /**
             * Enable detected violations log a stacktrace and timing data to the {@link
             * android.os.DropBoxManager DropBox} on policy violation. Intended mostly for platform
             * integrators doing beta user field data collection.
             */
            fun penaltyDropBox(): Builder {
                threadPolicy.penalties.add(ViolationPenalty.DropBox)
                return this
            }

            /**
             * Show an annoying dialog to the developer on detected violations, rate-limited to be
             * only a little annoying.
             */
            fun penaltyDialog(): Builder {
                threadPolicy.penalties.add(ViolationPenalty.Dialog)
                return this
            }

            /**
             * Crash the whole process on violation. This penalty runs at the end of all enabled
             * penalties so you'll still get see logging or other violations before the process
             * dies.
             *
             * <p>Unlike {@link #penaltyDeathOnNetwork}, this applies to disk reads, disk writes,
             * and network usage if their corresponding detect flags are set.
             */
            fun penaltyDeath(): Builder {
                threadPolicy.penalties.add(ViolationPenalty.Death)
                return this
            }

            /** Flash the screen during a violation. */
            fun penaltyFlashScreen(): Builder {
                threadPolicy.penalties.add(ViolationPenalty.FlashScreen)
                return this
            }

            /**
             * Crash the whole process on any network usage. Unlike {@link #penaltyDeath}, this
             * penalty runs <em>before</em> anything else. You must still have called {@link
             * #detectNetwork} to enable this.
             *
             * <p>In the Honeycomb or later SDKs, this is on by default.
             */
            fun penaltyDeathOnNetwork(): Builder {
                threadPolicy.penalties.add(ViolationPenalty.DeathOnNetwork)
                return this
            }

            /**
             * Call #{@link OnThreadViolationListener#onThreadViolation(Violation)} on specified
             * executor every violation.
             *
             * Call requires API level 28 or higher, otherwise it will be ignored.
             */
            fun penaltyListener(executor: Executor, listener: OnThreadViolationListener): Builder {
                threadPolicy.penaltyListeners.add(executor to listener)
                return this
            }

            /**
             * Setup penalties based on the Violation conditions.
             * Use DSL syntax to define the white list conditions.
             *
             * <pre>
             * val policy = StrictPro.ThreadPolicy.Builder()
             *     .detectAll()
             *     .penaltyDeath()
             *     .setWhiteList {
             *         // Defines penalty for the violation by substring in stack. Do nothing on violation if penalty is null.
             *         contains("some substring in stack", null)
             *         // Defines penalty for the violation by base64 encoded stack.
             *         base64("base64 encoded stack trace", ViolationPenalty.Ignore)
             *         base64("another base64 encoded stack trace", ViolationPenalty.Dialog)
             *         // Custom logic to define penalty for the violation. Do nothing on violation if penalty is null.
             *         condition { violation ->
             *             // some custom logic
             *             ViolationPenalty.FlashScreen
             *         }
             *     }
             *     .build()
             * </pre>
             *
             * If at least one penalty is defined for the violation in the whitelist,
             * default penalties will NOT be applied.
             *
             * If a list of penalties is defined for the violation in the whitelist,
             * and this list contains ViolationPenalty.Ignore, all penalties will be ignored.
             *
             * Call requires API level 28 or higher, otherwise it will be ignored.
             */
            fun setWhiteList(whiteListBlock: ViolationWhiteList.() -> Unit): Builder {
                threadPolicy.violationWhiteList.whiteListBlock()
                return this
            }

            /**
             * Construct the ThreadPolicy instance.
             *
             * <p>Note: if no penalties are enabled before calling <code>build</code>, {@link
             * #penaltyLog} is implicitly set.
             */
            fun build(): ThreadPolicy {
                Logger.logDebug("ThreadPolicy.Builder.build: $threadPolicy")
                return threadPolicy
            }
        }

        companion object {
            internal const val CATEGORY = "ThreadPolicy"
        }
    }

    /**
     * Sets the policy for what actions on the current thread should be detected, as well as the
     * penalty if such actions occur.
     *
     * <p>Internally this sets a thread-local variable which is propagated across cross-process IPC
     * calls, meaning you can catch violations when a system service or another process accesses the
     * disk or network on your behalf.
     *
     * @param policy the policy to put into place
     */
    fun setThreadPolicy(policy: ThreadPolicy) {
        ThreadPolicySetter.set(policy)
    }

    /**
     * {@link StrictPro} policy applied to all threads in the virtual machine's process.
     *
     * <p>The policy is enabled by {@link #setVmPolicy}.
     */
    class VmPolicy internal constructor() {

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

        override fun toString(): String {
            return "VmPolicy(detectAll=$detectAll, detectActivityLeaks=$detectActivityLeaks, " +
                "detectUntaggedSockets=$detectUntaggedSockets, " +
                "detectCleartextNetwork=$detectCleartextNetwork, " +
                "detectNonSdkApiUsage=$detectNonSdkApiUsage, " +
                "detectContentUriWithoutPermission=$detectContentUriWithoutPermission, " +
                "detectImplicitDirectBoot=$detectImplicitDirectBoot, " +
                "detectIncorrectContextUse=$detectIncorrectContextUse, " +
                "detectLeakedClosableObjects=$detectLeakedClosableObjects, " +
                "detectLeakedRegistrationObjects=$detectLeakedRegistrationObjects, " +
                "detectUnsafeIntentLaunch=$detectUnsafeIntentLaunch, " +
                "detectFileUriExposure=$detectFileUriExposure, " +
                "detectCredentialProtectedWhileLocked=$detectCredentialProtectedWhileLocked, " +
                "detectLeakedSqlLiteObjects=$detectLeakedSqlLiteObjects, " +
                "permitNonSdkApiUsage=$permitNonSdkApiUsage, " +
                "permitUnsafeIntentLaunch=$permitUnsafeIntentLaunch, " +
                "violationWhiteList=$violationWhiteList, penalties=$penalties, " +
                "penaltyListeners=$penaltyListeners, " +
                "classInstanceLimits=$classInstanceLimits)"
        }

        /**
         * Creates {@link VmPolicy} instances. Methods whose names start with {@code detect} specify
         * what problems we should look for. Methods whose names start with {@code penalty} specify
         * what we should do when we detect a problem.
         *
         * <p>You can call as many {@code detect} and {@code penalty} methods as you like. Currently
         * order is insignificant: all penalties apply to all detected problems.
         *
         * <p>For example, detect everything and log anything that's found:
         *
         * <pre>
         * val policy = StrictPro.VmPolicy.Builder()
         *     .detectAll()
         *     .penaltyLog()
         *     .build()
         * StrictPro.setVmPolicy(policy)
         * </pre>
         */
        class Builder {

            private val vmPolicy = VmPolicy()

            /**
             * Detect everything that's potentially suspect.
             *
             * <p>In the Honeycomb release this includes leaks of SQLite cursors, Activities, and
             * other closable objects but will likely expand in future releases.
             */
            fun detectAll(): Builder {
                vmPolicy.detectAll = true
                return this
            }

            /** Detect leaks of {@link android.app.Activity} subclasses. */
            fun detectActivityLeaks(): Builder {
                vmPolicy.detectActivityLeaks = true
                return this
            }

            /**
             * Detect any sockets in the calling app which have not been tagged using {@link
             * TrafficStats}. Tagging sockets can help you investigate network usage inside your
             * app, such as a narrowing down heavy usage to a specific library or component.
             *
             * <p>This currently does not detect sockets created in native code.
             *
             * @see TrafficStats#setThreadStatsTag(int)
             * @see TrafficStats#tagSocket(java.net.Socket)
             * @see TrafficStats#tagDatagramSocket(java.net.DatagramSocket)
             *
             * Call requires API level 26 or higher, otherwise it will be ignored.
             */
            fun detectUntaggedSockets(): Builder {
                vmPolicy.detectUntaggedSockets = true
                return this
            }

            /**
             * Detect any network traffic from the calling app which is not wrapped in SSL/TLS. This
             * can help you detect places that your app is inadvertently sending cleartext data
             * across the network.
             *
             * <p>Using {@link #penaltyDeath()} or {@link #penaltyDeathOnCleartextNetwork()} will
             * block further traffic on that socket to prevent accidental data leakage, in addition
             * to crashing your process.
             *
             * <p>Using {@link #penaltyDropBox()} will log the raw contents of the packet that
             * triggered the violation.
             *
             * <p>This inspects both IPv4/IPv6 and TCP/UDP network traffic, but it may be subject to
             * false positives, such as when STARTTLS protocols or HTTP proxies are used.
             *
             * Call requires API level 23 or higher, otherwise it will be ignored.
             */
            fun detectCleartextNetwork(): Builder {
                vmPolicy.detectCleartextNetwork = true
                return this
            }

            /**
             * Detect reflective usage of APIs that are not part of the public Android SDK.
             *
             * <p>Note that any non-SDK APIs that this processes accesses before this detection is
             * enabled may not be detected. To ensure that all such API accesses are detected,
             * you should apply this policy as early as possible after process creation.
             *
             * Call requires API level 28 or higher, otherwise it will be ignored.
             */
            fun detectNonSdkApiUsage(): Builder {
                vmPolicy.detectNonSdkApiUsage = true
                return this
            }

            /**
             * Detect when the calling application sends a {@code content://} {@link
             * android.net.Uri} to another app without setting {@link
             * Intent#FLAG_GRANT_READ_URI_PERMISSION} or {@link
             * Intent#FLAG_GRANT_WRITE_URI_PERMISSION}.
             *
             * <p>Forgetting to include one or more of these flags when sending an intent is
             * typically an app bug.
             *
             * @see Intent#FLAG_GRANT_READ_URI_PERMISSION
             * @see Intent#FLAG_GRANT_WRITE_URI_PERMISSION
             *
             * Call requires API level 26 or higher, otherwise it will be ignored.
             */
            fun detectContentUriWithoutPermission(): Builder {
                vmPolicy.detectContentUriWithoutPermission = true
                return this
            }

            /**
             * Detect any implicit reliance on Direct Boot automatic filtering
             * of {@link PackageManager} values. Violations are only triggered
             * when implicit calls are made while the user is locked.
             * <p>
             * Apps becoming Direct Boot aware need to carefully inspect each
             * query site and explicitly decide which combination of flags they
             * want to use:
             * <ul>
             * <li>{@link PackageManager#MATCH_DIRECT_BOOT_AWARE}
             * <li>{@link PackageManager#MATCH_DIRECT_BOOT_UNAWARE}
             * <li>{@link PackageManager#MATCH_DIRECT_BOOT_AUTO}
             * </ul>
             *
             * Call requires API level 29 or higher, otherwise it will be ignored.
             */
            fun detectImplicitDirectBoot(): Builder {
                vmPolicy.detectImplicitDirectBoot = true
                return this
            }

            /**
             * Detect attempts to invoke a method on a {@link Context} that is not suited for such
             * operation.
             * <p>An example of this is trying to obtain an instance of UI service (e.g.
             * {@link android.view.WindowManager}) from a non-visual {@link Context}. This is not
             * allowed, since a non-visual {@link Context} is not adjusted to any visual area, and
             * therefore can report incorrect metrics or resources.
             * @see Context#getDisplay()
             * @see Context#getSystemService(String)
             *
             * Call requires API level 31 or higher, otherwise it will be ignored.
             */
            fun detectIncorrectContextUse(): Builder {
                vmPolicy.detectIncorrectContextUse = true
                return this
            }

            /**
             * Detect when an {@link java.io.Closeable} or other object with an explicit termination
             * method is finalized without having been closed.
             *
             * <p>You always want to explicitly close such objects to avoid unnecessary resources
             * leaks.
             */
            fun detectLeakedClosableObjects(): Builder {
                vmPolicy.detectLeakedClosableObjects = true
                return this
            }

            /**
             * Detect when a {@link BroadcastReceiver} or {@link ServiceConnection} is leaked during
             * {@link Context} teardown.
             */
            fun detectLeakedRegistrationObjects(): Builder {
                vmPolicy.detectLeakedRegistrationObjects = true
                return this
            }

            /**
             * Detect when your app sends an unsafe {@link Intent}.
             * <p>
             * Violations may indicate security vulnerabilities in the design of
             * your app, where a malicious app could trick you into granting
             * {@link Uri} permissions or launching unexported components. Here
             * are some typical design patterns that can be used to safely
             * resolve these violations:
             * <ul>
             * <li> If you are sending an implicit intent to an unexported component, you should
             * make it an explicit intent by using {@link Intent#setPackage},
             * {@link Intent#setClassName} or {@link Intent#setComponent}.
             * </li>
             * <li> If you are unparceling and sending an intent from the intent delivered, The
             * ideal approach is to migrate to using a {@link android.app.PendingIntent}, which
             * ensures that your launch is performed using the identity of the original creator,
             * completely avoiding the security issues described above.
             * <li>If using a {@link android.app.PendingIntent} isn't feasible, an
             * alternative approach is to create a brand new {@link Intent} and
             * carefully copy only specific values from the original
             * {@link Intent} after careful validation.
             * </ul>
             * <p>
             * Note that this <em>may</em> detect false-positives if your app
             * sends itself an {@link Intent} which is first routed through the
             * OS, such as using {@link Intent#createChooser}. In these cases,
             * careful inspection is required to determine if the return point
             * into your app is appropriately protected with a signature
             * permission or marked as unexported. If the return point is not
             * protected, your app is likely vulnerable to malicious apps.
             *
             * @see Context#startActivity(Intent)
             * @see Context#startService(Intent)
             * @see Context#bindService(Intent, ServiceConnection, int)
             * @see Context#sendBroadcast(Intent)
             * @see android.app.Activity#setResult(int, Intent)
             *
             * Call requires API level 31 or higher, otherwise it will be ignored.
             */
            fun detectUnsafeIntentLaunch(): Builder {
                vmPolicy.detectUnsafeIntentLaunch = true
                return this
            }

            /**
             * Detect when the calling application exposes a {@code file://} {@link android.net.Uri}
             * to another app.
             *
             * <p>This exposure is discouraged since the receiving app may not have access to the
             * shared path. For example, the receiving app may not have requested the {@link
             * android.Manifest.permission#READ_EXTERNAL_STORAGE} runtime permission, or the
             * platform may be sharing the {@link android.net.Uri} across user profile boundaries.
             *
             * <p>Instead, apps should use {@code content://} Uris so the platform can extend
             * temporary permission for the receiving app to access the resource.
             *
             * @see androidx.core.content.FileProvider
             * @see Intent#FLAG_GRANT_READ_URI_PERMISSION
             */
            fun detectFileUriExposure(): Builder {
                vmPolicy.detectFileUriExposure = true
                return this
            }

            /**
             * Detect access to filesystem paths stored in credential protected
             * storage areas while the user is locked.
             * <p>
             * When a user is locked, credential protected storage is
             * unavailable, and files stored in these locations appear to not
             * exist, which can result in subtle app bugs if they assume default
             * behaviors or empty states. Instead, apps should store data needed
             * while a user is locked under device protected storage areas.
             *
             * @see Context#createDeviceProtectedStorageContext()
             *
             * Call requires API level 29 or higher, otherwise it will be ignored.
             */
            fun detectCredentialProtectedWhileLocked(): Builder {
                vmPolicy.detectCredentialProtectedWhileLocked = true
                return this
            }

            /**
             * Detect when an {@link android.database.sqlite.SQLiteCursor} or other SQLite object is
             * finalized without having been closed.
             *
             * <p>You always want to explicitly close your SQLite cursors to avoid unnecessary
             * database contention and temporary memory leaks.
             */
            fun detectLeakedSqlLiteObjects(): Builder {
                vmPolicy.detectLeakedSqlLiteObjects = true
                return this
            }

            /**
             * Permit reflective usage of APIs that are not part of the public Android SDK. Note
             * that this <b>only</b> affects {@code StrictMode}, the underlying runtime may
             * continue to restrict or warn on access to methods that are not part of the
             * public SDK.
             *
             * Call requires API level 28 or higher, otherwise it will be ignored.
             */
            fun permitNonSdkApiUsage(): Builder {
                vmPolicy.permitNonSdkApiUsage = true
                return this
            }

            /**
             * Permit your app to launch any {@link Intent} which originated
             * from outside your app.
             * <p>
             * Disabling this check is <em>strongly discouraged</em>, as
             * violations may indicate security vulnerabilities in the design of
             * your app, where a malicious app could trick you into granting
             * {@link Uri} permissions or launching unexported components.
             *
             * @see #detectUnsafeIntentLaunch()
             *
             * Call requires API level 31 or higher, otherwise it will be ignored.
             */
            fun permitUnsafeIntentLaunch(): Builder {
                vmPolicy.permitUnsafeIntentLaunch = true
                return this
            }

            /**
             * Crashes the whole process on violation. This penalty runs at the end of all enabled
             * penalties so you'll still get your logging or other violations before the process
             * dies.
             */
            fun penaltyDeath(): Builder {
                vmPolicy.penalties.add(ViolationPenalty.Death)
                return this
            }

            /**
             * Enable detected violations log a stacktrace and timing data to the {@link
             * android.os.DropBoxManager DropBox} on policy violation. Intended mostly for platform
             * integrators doing beta user field data collection.
             */
            fun penaltyDropBox(): Builder {
                vmPolicy.penalties.add(ViolationPenalty.DropBox)
                return this
            }

            /**
             * Crashes the whole process when cleartext network traffic is detected.
             *
             * @see #detectCleartextNetwork()
             *
             * Call requires API level 23 or higher, otherwise it will be ignored.
             */
            fun penaltyDeathOnCleartextNetwork(): Builder {
                vmPolicy.penalties.add(ViolationPenalty.DeathOnCleartextNetwork)
                return this
            }

            /**
             * Crashes the whole process when a {@code file://} {@link android.net.Uri} is exposed
             * beyond this app.
             *
             * @see #detectFileUriExposure()
             *
             * Call requires API level 24 or higher, otherwise it will be ignored.
             */
            fun penaltyDeathOnFileUriExposure(): Builder {
                vmPolicy.penalties.add(ViolationPenalty.DeathOnFileUriExposure)
                return this
            }

            /** Log detected violations to the system log. */
            fun penaltyLog(): Builder {
                vmPolicy.penalties.add(ViolationPenalty.Log)
                return this
            }

            /**
             * Show an annoying dialog to the developer on detected violations, rate-limited to be
             * only a little annoying.
             */
            fun penaltyDialog(): Builder {
                vmPolicy.penalties.add(ViolationPenalty.Dialog)
                return this
            }

            /** Flash the screen during a violation. */
            fun penaltyFlashScreen(): Builder {
                vmPolicy.penalties.add(ViolationPenalty.FlashScreen)
                return this
            }

            /**
             * Call #{@link OnVmViolationListener#onVmViolation(Violation)} on every violation.
             *
             * Call requires API level 28 or higher, otherwise it will be ignored.
             */
            fun penaltyListener(executor: Executor, listener: OnVmViolationListener): Builder {
                vmPolicy.penaltyListeners.add(executor to listener)
                return this
            }

            /**
             * Set an upper bound on how many instances of a class can be in memory at once. Helps
             * to prevent object leaks.
             */
            fun setClassInstanceLimit(clazz: Class<*>, limit: Int): Builder {
                vmPolicy.classInstanceLimits[clazz] = limit
                return this
            }

            /**
             * Setup penalties based on the Violation conditions.
             * Use DSL syntax to define the white list conditions.
             *
             * <pre>
             * val policy = StrictPro.VmPolicy.Builder()
             *     .detectAll()
             *     .penaltyDeath()
             *     .setWhiteList {
             *         // Defines penalty for the violation by substring in stack. Do nothing on violation if penalty is null.
             *         contains("some substring in stack", null)
             *         // Defines penalty for the violation by base64 encoded stack.
             *         base64("base64 encoded stack trace", ViolationPenalty.Ignore)
             *         base64("another base64 encoded stack trace", ViolationPenalty.Dialog)
             *         // Custom logic to define penalty for the violation. Do nothing on violation if penalty is null.
             *         condition { violation ->
             *             // some custom logic
             *             ViolationPenalty.FlashScreen
             *         }
             *     }
             *     .build()
             * </pre>
             *
             * If at least one penalty is defined for the violation in the whitelist,
             * default penalties will NOT be applied.
             *
             * If a list of penalties is defined for the violation in the whitelist,
             * and this list contains ViolationPenalty.Ignore, all penalties will be ignored.
             *
             * Call requires API level 28 or higher, otherwise it will be ignored.
             */
            fun setWhiteList(whiteListBlock: ViolationWhiteList.() -> Unit): Builder {
                vmPolicy.violationWhiteList.whiteListBlock()
                return this
            }

            /**
             * Construct the VmPolicy instance.
             *
             * <p>Note: if no penalties are enabled before calling <code>build</code>, {@link
             * #penaltyLog} is implicitly set.
             */
            fun build(): VmPolicy {
                Logger.logDebug("VmPolicy.Builder.build: $vmPolicy")
                return vmPolicy
            }
        }

        companion object {
            internal const val CATEGORY = "VmPolicy"
        }
    }

    /**
     * Sets the policy for what actions in the VM process (on any thread) should be detected, as
     * well as the penalty if such actions occur.
     *
     * @param policy the policy to put into place
     */
    fun setVmPolicy(policy: VmPolicy) {
        VmPolicySetter.set(policy)
    }

    @VisibleForTestingOnly_DoNotUseInProductionCode
    interface VisibleForTestingOnly {
        @VisibleForTestingOnly_DoNotUseInProductionCode
        fun StrictPro.setPenaltyExecutors(vararg executor: PenaltyExecutor) {
            penaltyExecutor = CompositePenaltyExecutor(executor.toList())
        }
    }
}