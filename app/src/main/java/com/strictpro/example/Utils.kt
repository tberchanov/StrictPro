package com.strictpro.example

import android.content.Context
import android.os.StrictMode
import android.util.Log
import com.strictpro.StrictPro
import java.io.BufferedInputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

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

fun enableStrictThreadPolicy(context: Context) {
    StrictPro.setThreadPolicy(
        StrictPro.ThreadPolicy.Builder()
            .detectAll()
            .penaltyLog()
            .penaltyDialog()
            .penaltyFlashScreen()
//            .setWhiteList {
//                detectAppViolationsOnly(context)
//            }
            .build()
    )
}

fun enableStrictVmPolicy(context: Context) {
    StrictPro.setVmPolicy(
        StrictPro.VmPolicy.Builder()
            .detectAll()
            .penaltyLog()
            .penaltyDialog()
            .penaltyFlashScreen()
            .setWhiteList {
                detectAppViolationsOnly(context)
            }
            .build()
    )
}

fun disableStrictThreadPolicy() {
    StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.LAX)
}

fun disableStrictVmPolicy() {
    StrictMode.setVmPolicy(StrictMode.VmPolicy.LAX)
}
