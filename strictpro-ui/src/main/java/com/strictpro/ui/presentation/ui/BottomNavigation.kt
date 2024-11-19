package com.strictpro.ui.presentation.ui

import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.strictpro.ui.presentation.ui.theme.Icons
import com.strictpro.ui.presentation.violations.history.ui.HistoryScreenRoute
import com.strictpro.ui.presentation.violations.list.ui.ViolationsScreenRoute

@Composable
internal fun BottomNavigation(navController: NavHostController) {
    androidx.compose.material.BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        bottomNavigationRoutes().forEach { topLevelRoute ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        topLevelRoute.icon,
                        contentDescription = topLevelRoute.name
                    )
                },
                label = { Text(topLevelRoute.name) },
                selected = currentDestination?.hierarchy?.any {
                    it.hasRoute(topLevelRoute.route::class)
                } == true,
                onClick = {
                    navController.navigate(topLevelRoute.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}

internal data class TopLevelRoute<T : Any>(val name: String, val route: T, val icon: ImageVector)

@Composable
internal fun bottomNavigationRoutes() = listOf(
    TopLevelRoute("Violations", ViolationsScreenRoute, Icons.Violation),
    TopLevelRoute("History", HistoryScreenRoute, Icons.History)
)