package com.strictpro.penalty

/**
 * The `ViolationPenalty` sealed class is used to define the penalties that should be applied to
 * violations detected by strict mode policies. The class provides several predefined penalties:
 *
 * - `Log`: Logs a message to the system log when a violation is detected.
 * - `Death`: Crashes the application when a violation is detected.
 * - `DropBox`: Writes a report to the system dropbox when a violation is detected.
 * - `FlashScreen`: Flashes the screen when a violation is detected.
 * - `Dialog`: Shows a dialog to the user when a violation is detected.
 * - `DeathOnNetwork`: Crashes the application when a network-related violation is detected.
 * - `DeathOnCleartextNetwork`: Crashes the application when a cleartext network violation is detected.
 * - `DeathOnFileUriExposure`: Crashes the application when a file URI exposure violation is detected.
 * - `Ignore`: Ignores the violation and does not apply any penalty.
 */
sealed class ViolationPenalty {
    data object Log : ViolationPenalty()
    data object Death : ViolationPenalty()
    data object DropBox : ViolationPenalty()
    data object FlashScreen : ViolationPenalty()
    data object Dialog : ViolationPenalty()
    data object DeathOnNetwork : ViolationPenalty()
    data object DeathOnCleartextNetwork : ViolationPenalty()
    data object DeathOnFileUriExposure : ViolationPenalty()
    data object Ignore : ViolationPenalty()
}
