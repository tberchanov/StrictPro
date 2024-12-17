package com.strictpro.ui.presentation.ui.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

@Composable
internal fun RowScope.BottomNavigationItem(
    bottomNavigationRoute: BottomNavigationRoute,
    navController: NavHostController,
    currentDestination: NavDestination?,
) {
    val name = stringResource(bottomNavigationRoute.nameRes)
    BottomNavigationItem(
        icon = {
            Icon(
                ImageVector.vectorResource(id = bottomNavigationRoute.iconRes),
                contentDescription = name
            )
        },
        label = { Text(name) },
        selected = currentDestination?.hierarchy?.any {
            it.hasRoute(bottomNavigationRoute::class)
        } == true,
        onClick = {
            navController.navigate(bottomNavigationRoute) {
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
