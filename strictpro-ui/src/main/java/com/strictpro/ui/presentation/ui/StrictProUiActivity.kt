package com.strictpro.ui.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarDefaults
import androidx.compose.material.SnackbarHost
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.strictpro.ui.presentation.ui.theme.ComopseSampleTheme
import com.strictpro.ui.presentation.util.snackbar.SnackbarController
import com.strictpro.ui.presentation.util.snackbar.observeSnackbars
import com.strictpro.ui.presentation.violations.details.ui.ViolationDetailsScreen
import com.strictpro.ui.presentation.violations.details.ui.ViolationDetailsScreenRoute
import com.strictpro.ui.presentation.violations.history.ui.HistoryScreen
import com.strictpro.ui.presentation.violations.history.ui.HistoryScreenRoute
import com.strictpro.ui.presentation.violations.list.ui.ViolationsScreen
import com.strictpro.ui.presentation.violations.list.ui.ViolationsScreenRoute

internal class StrictProUiActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComopseSampleTheme {
                val snackbarHostState = observeSnackbars()
                val navController = rememberNavController()
                Scaffold(
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
                    NavHost(
                        navController,
                        startDestination = ViolationsScreenRoute,
                        modifier = Modifier.padding(innerPadding),
                    ) {
                        composable<ViolationsScreenRoute> {
                            ViolationsScreen()
                        }
                        composable<HistoryScreenRoute> {
                            HistoryScreen()
                        }
                        composable<ViolationDetailsScreenRoute> {
                            ViolationDetailsScreen()
                        }
                    }
                }

            }
        }
    }
}
