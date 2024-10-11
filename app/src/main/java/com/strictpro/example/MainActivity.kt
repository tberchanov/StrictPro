package com.strictpro.example

import android.content.Context
import android.os.Bundle
import android.os.StrictMode
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.strictpro.StrictPro
import com.strictpro.example.ui.theme.StrictProTheme
import java.io.File
import java.io.FileOutputStream
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableStrictVmPolicy()
        enableStrictThreadPolicy()

        enableEdgeToEdge()
        setContent {
            StrictProTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("ThreadPolicy")
                                Box(modifier = Modifier.width(8.dp))
                                var threadPolicyEnabled by remember { mutableStateOf(true) }
                                Switch(threadPolicyEnabled, onCheckedChange = {
                                    threadPolicyEnabled = it
                                    if (it) {
                                        enableStrictThreadPolicy()
                                    } else {
                                        disableStrictThreadPolicy()
                                    }
                                })
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("VmPolicy")
                                Box(modifier = Modifier.width(8.dp))
                                var vmPolicyEnabled by remember { mutableStateOf(true) }
                                Switch(vmPolicyEnabled, onCheckedChange = {
                                    vmPolicyEnabled = it
                                    if (it) {
                                        enableStrictVmPolicy()
                                    } else {
                                        disableStrictVmPolicy()
                                    }
                                })
                            }
                            Column(
                                modifier = Modifier
                                    .padding(innerPadding)
                                    .fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Button(onClick = {
                                    this@MainActivity.getPreferences(Context.MODE_PRIVATE)
                                        .edit()
                                        .putBoolean("StrictDeath", false)
                                        .commit()
                                }) {
                                    Text("Trigger DiskReadViolation")
                                }
                                Button(onClick = {
                                    StrictMode.noteSlowCall("Slow call")
                                }) {
                                    Text("Trigger CustomViolation")
                                }
                                Button(onClick = {
                                    System.gc()
                                    System.runFinalization()
                                    System.gc()
                                }) {
                                    Text("Trigger ExplicitGcViolation")
                                }
                                Button(onClick = {
                                    FileOutputStream(
                                        File(
                                            this@MainActivity.filesDir,
                                            "somefile.txt"
                                        )
                                    )
                                }) {
                                    Text("Trigger LeakedClosableViolation")
                                }
                                Button(onClick = {
                                    performNetworkOperation()
                                }) {
                                    Text("Trigger NetworkViolation")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun performNetworkOperation() {
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

    private fun enableStrictThreadPolicy() {
        StrictPro.setThreadPolicy(
            StrictPro.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .penaltyDialog()
                .penaltyFlashScreen()
                .setWhiteList {
                    detectAppViolationsOnly(this@MainActivity)
                }
                .build()
        )
    }

    private fun enableStrictVmPolicy() {
        StrictPro.setVmPolicy(
            StrictPro.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .penaltyDialog()
                .penaltyFlashScreen()
                .setWhiteList {
                    detectAppViolationsOnly(this@MainActivity)
                }
                .build()
        )
    }

    private fun disableStrictThreadPolicy() {
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.LAX)
    }

    private fun disableStrictVmPolicy() {
        StrictMode.setVmPolicy(StrictMode.VmPolicy.LAX)
    }
}