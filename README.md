Useful articles:

https://medium.com/wizeline-mobile/raising-the-bar-with-android-strictmode-7042d8a9e67b

https://medium.com/mobile-app-development-publication/android-strict-mode-selective-code-suppression-37ee0d999f6b#.kszw12gs1

https://medium.com/mobile-app-development-publication/walk-through-hell-with-android-strictmode-7e8605168032

https://elye-project.medium.com/hell-level-4-unleashed-by-android-strict-mode-dare-you-challenge-it-1dc9048bb4fb

TODO:

* Enable logging to logcat if no penalty is specified for ThreadPolicy or VmPolicy.

* Add dialog rate limiting (trottling).

* Add penalties priority. Death penalty should be executed lastly

* Do deathOnNetwork according to documentation: require detectNetwork, run before any penalty.

* Add timing and other metadata to the DropBox penalty log.