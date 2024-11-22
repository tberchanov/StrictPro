package com.strictpro.ui.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.strictpro.ui.presentation.violations.history.navigation.historyTopLevelRoute
import com.strictpro.ui.presentation.violations.list.navigation.violationsTopLevelRoute

@Composable
internal fun BottomNavigation(navController: NavHostController) {
    androidx.compose.material.BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        bottomNavigationRoutes().forEach { topLevelRoute ->
            BottomNavigationItem(
                topLevelRoute = topLevelRoute,
                navController = navController,
                currentDestination = currentDestination
            )
        }
    }
}

@Composable
internal fun bottomNavigationRoutes() = listOf(
    violationsTopLevelRoute(),
    historyTopLevelRoute()
)
