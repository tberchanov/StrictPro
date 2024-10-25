package com.strictpro.example

import android.content.Intent
import android.os.StrictMode
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.strictpro.StrictPro
import com.strictpro.example.ui.theme.StrictProTheme
import kotlin.concurrent.thread

const val TAG = "StrictProApp"

private val ThreadPolicyColor = Color.Blue.copy(blue = 0.5f)
private val VmPolicyColor = Color.Green.copy(green = 0.5f)

@Composable
fun ExampleContent(
    threadPolicy: StrictPro.ThreadPolicy = buildDefaultThreadPolicy(LocalContext.current),
    vmPolicy: StrictPro.VmPolicy = buildDefaultVmPolicy(LocalContext.current),
) {
    LaunchedEffect(Unit) {
        StrictPro.printDebugLogs = true
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
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 12.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.DarkGray)
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(20.dp),
                            text = "It is example app to demonstrate StrictPro library.\n\n" +
                                "You can  trigger different violations by clicking on buttons below.",
                            color = Color.White,
                            textAlign = TextAlign.Center,
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("ThreadPolicy")
                        Box(modifier = Modifier.width(8.dp))
                        var threadPolicyEnabled by remember { mutableStateOf(true) }
                        Switch(
                            threadPolicyEnabled,
                            colors = SwitchDefaults.colors(
                                checkedTrackColor = ThreadPolicyColor,
                                uncheckedTrackColor = ThreadPolicyColor.copy(alpha = 0.5f),
                            ),
                            onCheckedChange = {
                                threadPolicyEnabled = it
                                if (it) {
                                    StrictPro.setThreadPolicy(threadPolicy)
                                } else {
                                    disableStrictThreadPolicy()
                                }
                            })
                    }
                    val context = LocalContext.current
                    ThreadPolicyButton(R.string.Trigger_DiskReadViolation) {
                        performPrefsEdit(context)
                    }
                    ThreadPolicyButton(R.string.Trigger_CustomViolation) {
                        StrictMode.noteSlowCall("Slow call")
                    }
                    ThreadPolicyButton(R.string.Trigger_ExplicitGcViolation) {
                        performExplicitGc()
                    }
                    ThreadPolicyButton(R.string.Trigger_NetworkViolation) {
                        performNetworkOperation(tagged = true)
                    }
                    ThreadPolicyButton(R.string.Trigger_ResourceMismatches) {
                        triggerResourceMismatch(context)
                    }
                    Box(Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("VmPolicy")
                        Box(modifier = Modifier.width(8.dp))
                        var vmPolicyEnabled by remember { mutableStateOf(true) }
                        Switch(
                            vmPolicyEnabled,
                            colors = SwitchDefaults.colors(
                                checkedTrackColor = VmPolicyColor,
                                uncheckedTrackColor = VmPolicyColor.copy(alpha = 0.5f),
                            ),
                            onCheckedChange = {
                                vmPolicyEnabled = it
                                if (it) {
                                    StrictPro.setVmPolicy(vmPolicy)
                                } else {
                                    disableStrictVmPolicy()
                                }
                            })
                    }
                    VmPolicyButton(R.string.Trigger_LeakedClosableViolation) {
                        performLeakedClosable(context)
                    }
                    VmPolicyButton(R.string.Trigger_UntaggedSockets) {
                        thread {
                            performNetworkOperation()
                        }
                    }
                    VmPolicyButton(R.string.Trigger_NonSdkApiUsage) {
                        performNonSdkApiUsage()
                    }
                    VmPolicyButton(R.string.Trigger_CleartextNetwork) {
                        thread {
                            performNetworkOperation(tagged = true, cleartext = true)
                        }
                    }
                    VmPolicyButton(R.string.Trigger_LeakedRegistrationObjects) {
                        context.startActivity(
                            Intent(
                                context,
                                LeakedReceiverActivity::class.java
                            )
                        )
                    }
                    VmPolicyButton(R.string.Trigger_FileUriExposure) {
                        thread {
                            triggerFileUriExposure(context)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ThreadPolicyButton(textRes: Int, action: () -> Unit) {
    Button(
        onClick = action,
        colors = ButtonDefaults.buttonColors(containerColor = ThreadPolicyColor)
    ) {
        Text(LocalContext.current.getString(textRes))
    }
}

@Composable
fun VmPolicyButton(textRes: Int, action: () -> Unit) {
    Button(
        onClick = action,
        colors = ButtonDefaults.buttonColors(containerColor = VmPolicyColor)
    ) {
        Text(LocalContext.current.getString(textRes))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewComposeContent() {
    ExampleContent()
}
