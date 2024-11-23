package com.strictpro.ui.presentation.violations.history.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.strictpro.ui.domain.model.ViolationType
import com.strictpro.ui.presentation.violations.history.ui.HistoryFullScreen
import kotlinx.serialization.Serializable

@Serializable
data class HistoryFullScreenRoute(
    val violationTypeValue: String? = null,
)

fun NavController.navigateToHistoryFull(violationType: ViolationType?) =
    navigate(route = HistoryFullScreenRoute(violationType?.value))

fun NavGraphBuilder.historyFullScreen() {
    composable<HistoryFullScreenRoute> {
        val args = it.toRoute<HistoryFullScreenRoute>()
        HistoryFullScreen(
            violationType = args.violationTypeValue?.let(::ViolationType),
        )
    }
}