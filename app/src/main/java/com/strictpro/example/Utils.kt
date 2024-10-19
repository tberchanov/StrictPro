package com.strictpro.example

import android.content.Context
import android.os.StrictMode
import android.util.Log
import com.strictpro.StrictPro
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

fun buildDefaultThreadPolicy(context: Context) = StrictPro.ThreadPolicy.Builder()
    .detectAll()
    .penaltyLog()
    .penaltyDialog()
    .penaltyFlashScreen()
    .setWhiteList {
        detectAppViolationsOnly(context)
    }
    .build()

fun buildDefaultVmPolicy(context: Context) = StrictPro.VmPolicy.Builder()
    .detectAll()
    .penaltyLog()
    .penaltyDialog()
    .penaltyFlashScreen()
    .setWhiteList {
        detectAppViolationsOnly(context)
    }
    .build()

fun performNetworkOperation() {
    try {
        val url = URL("https://www.example.com")
        val urlConnection = url.openConnection() as HttpURLConnection
        try {
            BufferedInputStream(urlConnection.inputStream).close()
        } finally {
            urlConnection.disconnect()
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

fun performPrefsEdit(context: Context) {
    Log.d("StrictPro", "performPrefsEdit")
    context.getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        .edit()
        .putBoolean("StrictDeath", false)
        .commit()
}

fun disableStrictThreadPolicy() {
    StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.LAX)
}

fun disableStrictVmPolicy() {
    StrictMode.setVmPolicy(StrictMode.VmPolicy.LAX)
}

fun performNonSdkApiUsage() {
    val clazz = Class.forName("android.app.ActivityThread")
    val method = clazz.getMethod("currentActivityThread")
    val activityThread = method.invoke(null)
}

fun performLeakedClosable(context: Context) {
    FileOutputStream(
        File(
            context.filesDir,
            "somefile.txt"
        )
    )
}

fun performExplicitGc() {
    System.gc()
    System.runFinalization()
}
