package com.strictpro.ui.presentation.violations.history.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.strictpro.ui.R
import com.strictpro.ui.domain.model.ViolationType
import com.strictpro.ui.presentation.ui.navigation.BottomNavigationRoute
import com.strictpro.ui.presentation.violations.history.ui.HistoryScreen
import kotlinx.serialization.Serializable

@Serializable
internal data class HistoryScreenRoute(
    val violationTypeValue: String? = null,
) : BottomNavigationRoute(
    nameRes = R.string.history,
    iconRes = R.drawable.history,
)

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
