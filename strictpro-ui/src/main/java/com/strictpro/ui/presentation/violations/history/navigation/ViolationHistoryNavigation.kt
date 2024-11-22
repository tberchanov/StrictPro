package com.strictpro.ui.presentation.violations.history.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.strictpro.ui.R
import com.strictpro.ui.domain.model.ViolationType
import com.strictpro.ui.presentation.ui.navigation.TopLevelRoute
import com.strictpro.ui.presentation.ui.theme.Icons
import com.strictpro.ui.presentation.violations.history.ui.HistoryScreen
import kotlinx.serialization.Serializable

@Serializable
data class HistoryScreenRoute(
    val violationTypeValue: String? = null,
)

@Composable
internal fun historyTopLevelRoute() =
    TopLevelRoute(stringResource(R.string.history), HistoryScreenRoute(), Icons.History)

fun NavGraphBuilder.historyScreen() {
    composable<HistoryScreenRoute> {
        val args = it.toRoute<HistoryScreenRoute>()
        HistoryScreen(
            violationType = args.violationTypeValue?.let(::ViolationType),
        )
    }
}

fun NavController.navigateToHistory(violationType: ViolationType?) =
    navigate(route = HistoryScreenRoute(violationType?.value))
