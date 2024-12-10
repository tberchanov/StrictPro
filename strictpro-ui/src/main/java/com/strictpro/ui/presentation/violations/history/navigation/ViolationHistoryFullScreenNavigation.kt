package com.strictpro.ui.presentation.violations.history.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.strictpro.ui.domain.model.ViolationType
import com.strictpro.ui.presentation.violations.history.ui.HistoryScreen
import kotlinx.serialization.Serializable

@Serializable
internal data class HistoryFullScreenRoute(
    val violationTypeValue: String? = null,
)

internal fun NavController.navigateToHistoryFull(violationType: ViolationType?) =
    navigate(route = HistoryFullScreenRoute(violationType?.value))

internal fun NavGraphBuilder.historyFullScreen(
    onViolationClick: (violationId: String) -> Unit = {},
) {
    composable<HistoryFullScreenRoute> {
        val args = it.toRoute<HistoryFullScreenRoute>()
        HistoryScreen(
            violationType = args.violationTypeValue?.let(::ViolationType),
            onHistoryItemClick = { historyItem ->
                onViolationClick(historyItem.violationId)
            },
        )
    }
}