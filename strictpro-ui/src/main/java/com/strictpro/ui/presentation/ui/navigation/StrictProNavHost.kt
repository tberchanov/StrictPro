package com.strictpro.ui.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.strictpro.ui.presentation.violations.details.navigation.navigateToViolationDetails
import com.strictpro.ui.presentation.violations.details.navigation.violationDetailsScreen
import com.strictpro.ui.presentation.violations.history.navigation.historyFullScreen
import com.strictpro.ui.presentation.violations.history.navigation.historyScreen
import com.strictpro.ui.presentation.violations.history.navigation.navigateToHistoryFull
import com.strictpro.ui.presentation.violations.list.navigation.ViolationsScreenRoute
import com.strictpro.ui.presentation.violations.list.navigation.violationsScreen

@Composable
fun StrictProNavHost(
    navController: NavHostController,
) {
    NavHost(
        navController,
        startDestination = ViolationsScreenRoute(),
    ) {
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