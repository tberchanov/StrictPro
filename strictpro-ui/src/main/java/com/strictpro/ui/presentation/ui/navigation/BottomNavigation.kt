package com.strictpro.ui.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.strictpro.ui.presentation.violations.history.navigation.HistoryScreenRoute
import com.strictpro.ui.presentation.violations.list.navigation.ViolationsScreenRoute

@Composable
internal fun BottomNavigation(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    if (navBackStackEntry.isBottomNavigationEntry()) {
        androidx.compose.material.BottomNavigation {
            bottomNavigationRoutes().forEach { bottomNavigationRoute ->
                BottomNavigationItem(
                    bottomNavigationRoute = bottomNavigationRoute,
                    navController = navController,
                    currentDestination = currentDestination
                )
            }
        }
    }
}

@Composable
internal fun bottomNavigationRoutes(): List<BottomNavigationRoute> = listOf(
    ViolationsScreenRoute(),
    HistoryScreenRoute(),
)
