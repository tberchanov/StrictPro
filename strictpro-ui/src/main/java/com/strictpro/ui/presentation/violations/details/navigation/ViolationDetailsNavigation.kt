package com.strictpro.ui.presentation.violations.details.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.strictpro.ui.presentation.violations.details.ui.ViolationDetailsScreen
import kotlinx.serialization.Serializable

@Serializable
internal data class ViolationDetailsScreenRoute(
    val violationId: String,
)

fun NavGraphBuilder.violationDetailsScreen() {
    composable<ViolationDetailsScreenRoute> {
        val args = it.toRoute<ViolationDetailsScreenRoute>()
        ViolationDetailsScreen(violationId = args.violationId)
    }
}

internal fun NavController.navigateToViolationDetails(violationId: String) =
    navigate(route = ViolationDetailsScreenRoute(violationId))