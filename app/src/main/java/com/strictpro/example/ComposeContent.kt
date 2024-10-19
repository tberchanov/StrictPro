package com.strictpro.example

import android.content.Context
import android.os.StrictMode
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.strictpro.StrictPro
import com.strictpro.example.ui.theme.StrictProTheme

@Composable
fun ComposeContent(
    context: Context,
    threadPolicy: StrictPro.ThreadPolicy = buildDefaultThreadPolicy(context),
    vmPolicy: StrictPro.VmPolicy = buildDefaultVmPolicy(context),
) {
    LaunchedEffect(Unit) {
        StrictPro.setThreadPolicy(threadPolicy)
        StrictPro.setVmPolicy(vmPolicy)
    }
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
                                StrictPro.setThreadPolicy(threadPolicy)
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
                                StrictPro.setVmPolicy(vmPolicy)
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
                        Button(onClick = { performPrefsEdit(context) }) {
                            Text(context.getString(R.string.Trigger_DiskReadViolation))
                        }
                        Button(onClick = { StrictMode.noteSlowCall("Slow call") }) {
                            Text(context.getString(R.string.Trigger_CustomViolation))
                        }
                        Button(onClick = { performExplicitGc() }) {
                            Text(context.getString(R.string.Trigger_ExplicitGcViolation))
                        }
                        Button(onClick = { performLeakedClosable(context) }) {
                            Text(context.getString(R.string.Trigger_LeakedClosableViolation))
                        }
                        Button(onClick = { performNetworkOperation() }) {
                            Text(context.getString(R.string.Trigger_NetworkViolation))
                        }
                        Button(onClick = { performNonSdkApiUsage() }) {
                            Text(context.getString(R.string.Trigger_NonSdkApiUsage))
                        }
                    }
                }
            }
        }
    }
}
