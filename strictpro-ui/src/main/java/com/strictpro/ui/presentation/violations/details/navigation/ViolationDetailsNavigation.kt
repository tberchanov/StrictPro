package com.strictpro.ui.presentation.violations.details.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.strictpro.ui.presentation.violations.details.ui.ViolationDetailsScreen
import kotlinx.serialization.Serializable

@Serializable
internal object ViolationDetailsScreenRoute

fun NavGraphBuilder.violationDetailsScreen() {
    composable<ViolationDetailsScreenRoute> {
        ViolationDetailsScreen()
    }
}