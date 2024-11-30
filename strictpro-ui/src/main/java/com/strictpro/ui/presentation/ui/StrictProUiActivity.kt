package com.strictpro.ui.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarDefaults
import androidx.compose.material.SnackbarHost
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.strictpro.ui.presentation.ui.navigation.BottomNavigation
import com.strictpro.ui.presentation.ui.navigation.LocalNavController
import com.strictpro.ui.presentation.ui.navigation.StrictProNavHost
import com.strictpro.ui.presentation.ui.theme.ComopseSampleTheme
import com.strictpro.ui.presentation.ui.theme.DarkGray
import com.strictpro.ui.presentation.util.snackbar.SnackbarController
import com.strictpro.ui.presentation.util.snackbar.observeSnackbars

internal class StrictProUiActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComopseSampleTheme {
                val snackbarHostState = observeSnackbars()
                val navController = rememberNavController()
                CompositionLocalProvider(LocalNavController provides navController) {
                    Scaffold(
                        modifier = Modifier
                            .background(DarkGray)
                            .safeDrawingPadding(),
                        snackbarHost = {
                            SnackbarHost(
                                hostState = snackbarHostState,
                                snackbar = { snackbarData ->
                                    val lastEvent = SnackbarController.lastEvent
                                    Snackbar(
                                        snackbarData,
                                        backgroundColor = lastEvent?.backgroundColor
                                            ?: SnackbarDefaults.backgroundColor,
                                    )
                                }
                            )
                        },
                        bottomBar = {
                            BottomNavigation(navController)
                        }
                    ) { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            StrictProNavHost(navController)
                        }
                    }
                }
            }
        }
    }
}
