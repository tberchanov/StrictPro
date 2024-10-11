package com.strictpro.penalty

// TODO add priorities, so death penalty should execute after previous
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
