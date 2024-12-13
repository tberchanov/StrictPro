package com.strictpro.example

import android.content.Context
import android.content.Intent
import android.content.res.TypedArray
import android.net.TrafficStats
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.os.StrictMode
import android.util.Log
import androidx.core.os.ExecutorCompat
import com.strictpro.StrictPro
import com.strictpro.ui.StrictProUiPenaltyListener
import com.strictpro.ui.penaltyListener
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

private val mainExecutor = ExecutorCompat.create(Handler(Looper.getMainLooper()))

fun buildDefaultThreadPolicy(context: Context) = StrictPro.ThreadPolicy.Builder()
    .detectAll()
    .penaltyLog()
//    .penaltyDialog()
//    .penaltyFlashScreen()
    .setWhiteList {

    }
    .penaltyListener(mainExecutor, StrictProUiPenaltyListener.create())
//    .penaltyListener(mainExecutor) { violation ->
//        Log.d("StrictPro", "thread penaltyListener2", violation)
//    }
    .build()

fun buildDefaultVmPolicy(context: Context) = StrictPro.VmPolicy.Builder()
    .detectAll()
    .penaltyLog()
//    .penaltyDialog()
//    .penaltyFlashScreen()
    .penaltyListener(mainExecutor, StrictProUiPenaltyListener.create())
    .setWhiteList {

    }
//    .penaltyListener(mainExecutor) { violation ->
//        Log.d("StrictPro", "vm penaltyListener1", violation)
//    }
//    .penaltyListener(mainExecutor) { violation ->
//        Log.d("StrictPro", "vm penaltyListener2", violation)
//    }
    .build()

fun performNetworkOperation(tagged: Boolean = false, cleartext: Boolean = false) {
    try {
        if (tagged) {
            TrafficStats.setThreadStatsTag(Thread.currentThread().id.toInt())
        }
        val url = URL(
            if (cleartext) "http://api.sampleapis.com/coffee/hot" else "https://api.sampleapis.com/coffee/hot"
        )
        val urlConnection = url.openConnection() as HttpURLConnection
        try {
            urlConnection.requestMethod = "GET"
            BufferedInputStream(urlConnection.inputStream).use { inputStream ->
                val response = inputStream.bufferedReader().use { it.readText() }
                Log.d("StrictPro", "Response: $response")
            }
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
    thread {
        FileOutputStream(
            File(
                context.filesDir,
                "somefile.txt"
            )
        )
        Thread.sleep(500)
        // This should trigger a leaked closable violation
        performExplicitGc()
    }
}

fun performExplicitGc() {
    System.gc()
    System.runFinalization()
}

fun triggerResourceMismatch(context: Context) {
    try {
        val typedArray: TypedArray = context.resources.obtainTypedArray(R.array.some_string_array)
        typedArray.getInt(0, -1) // This should trigger a resource mismatch violation
        typedArray.recycle()
    } catch (e: Exception) {
        Log.e(TAG, "triggerResourceMismatch: ", e)
    }
}

fun triggerFileUriExposure(context: Context) {
    // Create a file URI to share
    val file = File(context.filesDir, "example.txt")

    // Write something to the file (optional for demonstration)
    file.writeText("This is a test file for StrictMode detection.")

    // Create a file URI that may trigger StrictMode's detectFileUriExposure
    val fileUri = Uri.fromFile(file)

    // Attempt to share the file URI through an intent
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_STREAM, fileUri)
    }

    // Start activity with the intent, which should trigger detectFileUriExposure
    context.startActivity(Intent.createChooser(intent, "Share file"))
}