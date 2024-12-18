package com.strictpro.ui.presentation.ui.navigation

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.strictpro.ui.presentation.unavailable.navigation.UnavailableScreenRoute
import com.strictpro.ui.presentation.unavailable.navigation.unavailableScreen
import com.strictpro.ui.presentation.violations.details.navigation.navigateToViolationDetails
import com.strictpro.ui.presentation.violations.details.navigation.violationDetailsScreen
import com.strictpro.ui.presentation.violations.history.navigation.historyFullScreen
import com.strictpro.ui.presentation.violations.history.navigation.historyScreen
import com.strictpro.ui.presentation.violations.history.navigation.navigateToHistoryFull
import com.strictpro.ui.presentation.violations.list.navigation.ViolationsScreenRoute
import com.strictpro.ui.presentation.violations.list.navigation.violationsScreen

@Composable
internal fun StrictProNavHost(
    navController: NavHostController,
) {
    NavHost(
        navController,
        startDestination = getStartDestination(),
    ) {
        unavailableScreen()
        violationsScreen(
            onViolationTypeClicked = navController::navigateToHistoryFull,
        )
        historyScreen(
            onViolationClick = navController::navigateToViolationDetails,
        )
        historyFullScreen(
            onViolationClick = navController::navigateToViolationDetails,
        )
        violationDetailsScreen()
    }
}

/**
 * Determines the start destination for the navigation graph.
 *
 * On Android API level 28 and above, the start destination is set to `ViolationsScreenRoute`,
 * allowing the app to listen for and display violations.
 *
 * On versions below API level 28, the start destination is set to `UnavailableScreenRoute`
 * because the ability to listen for violations is not available, and thus it is not possible
 * to display violations on the UI.
 */
private fun getStartDestination(): Any =
    if (Build.VERSION.SDK_INT >= 28) ViolationsScreenRoute() else UnavailableScreenRoute
