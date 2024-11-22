package com.strictpro.ui.presentation.ui.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

@Composable
internal fun RowScope.BottomNavigationItem(
    topLevelRoute: TopLevelRoute<*>,
    navController: NavHostController,
    currentDestination: NavDestination?,
) {
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
                // Restore state when reselecting a previously selected item
                restoreState = true
            }
        }
    )
}
