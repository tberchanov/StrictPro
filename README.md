<h1 align="center">Kotlin Multiplatform Developer Roadmap</h1></br>

Have you ever used [StrictMode](https://developer.android.com/reference/android/os/StrictMode.html) ?

Then you have probably faced with those issues:

* StrictMode detects violations that you cannot or do not want to fix (false positives). As result developers disalble a lot of detections or ignore them. Making such a powerful tool useless.

* Some StrictMode configurations require specific versions of build API. That enforces developers to write boilerplate conditions.

* Lack of violations UI representation.

StrictPro library aims to solve those issues providing a wrapper on StrictMode with additional opportunities.

###### build.gradle.kts ######
```kotlin
android {
    defaultConfig {
        minSdk = 21 // Required minimum API is 21
    }
}

dependencies {    
    implementation("com.github.tberchanov:StrictPro:0.0.2")
}
```

###### settings.gradle.kts ######
```kotlin
dependencyResolutionManagement {
    repositories {
        maven { url = uri("https://jitpack.io") }
    }
}
```

Samples
------

###### MainApplication.kt ######
```kotlin
class MainApplication: Application() {

    override fun onCreate() {
        StrictPro.setVmPolicy(
            StrictPro.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .penaltyDialog()
                .penaltyFlashScreen()
                .setWhiteList {
                    // Defines penalty for the violation by substring in stack. Ignore violation if penalty is null.
                    contains("some substring in stack", null)
                    // Defines penalty for the violation by base64 encoded stack. Ignore violation if penalty is null.
                    base64("base64 encoded stack trace", ViolationPenalty.Ignore)
                    base64("another base64 encoded stack trace", ViolationPenalty.Dialog)
                    // Ignore all violations that are not contain app package name in stack.
                    detectAppViolationsOnly(context)
                    // Custom logic to define penalty for the violation. Ignore violation if penalty is null.
                    ignoreIf { violation ->
                        // some custom logic
                        ViolationPenalty.FlashScreen
                    }
                    detectAppViolationsOnly(this@MainApplication)
                }
                .build(),
        )
        StrictPro.setThreadPolicy(
            StrictPro.ThreadPolicy.Builder()
                .detectAll()
                .detectExplicitGc() // Call requires API level 34
                .penaltyLog()
                .penaltyDialog()
                .penaltyFlashScreen()
                .setWhiteList {
                    // Defines penalty for the violation by substring in stack. Ignore violation if penalty is null.
                    contains("some substring in stack", null)
                    // Defines penalty for the violation by base64 encoded stack. Ignore violation if penalty is null.
                    base64("base64 encoded stack trace", ViolationPenalty.Ignore)
                    base64("another base64 encoded stack trace", ViolationPenalty.Dialog)
                    // Ignore all violations that are not contain app package name in stack.
                    detectAppViolationsOnly(context)
                    // Custom logic to define penalty for the violation. Ignore violation if penalty is null.
                    ignoreIf { violation ->
                        // some custom logic
                        ViolationPenalty.FlashScreen
                    }
                    detectAppViolationsOnly(this@MainApplication)
                }
                .build(),
        )
    }
}
```

Future plans / TODO:
---

* Enable logging to logcat if no penalty is specified for ThreadPolicy or VmPolicy.

* Add dialog rate limiting (trottling).

* Add penalties priority. Death penalty should be executed lastly

* Do deathOnNetwork according to documentation: require detectNetwork, run before any penalty.

* Add timing and other metadata to the DropBox penalty log.

* Implement more penalties executors: NotificationPenaltyExecutor, ToastPenaltyExecutor, VibrationPenaltyExecutor, SoundPenaltyExecutor.

* Add screen with UI about StrictMode penalties.

Useful articles about StrictMode:
---

https://medium.com/wizeline-mobile/raising-the-bar-with-android-strictmode-7042d8a9e67b

https://medium.com/mobile-app-development-publication/android-strict-mode-selective-code-suppression-37ee0d999f6b#.kszw12gs1

https://medium.com/mobile-app-development-publication/walk-through-hell-with-android-strictmode-7e8605168032

https://elye-project.medium.com/hell-level-4-unleashed-by-android-strict-mode-dare-you-challenge-it-1dc9048bb4fb

## License

This project is licensed under the [MIT License](LICENSE).