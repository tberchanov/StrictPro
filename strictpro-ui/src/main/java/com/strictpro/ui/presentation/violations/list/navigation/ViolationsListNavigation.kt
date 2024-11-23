package com.strictpro.ui.presentation.violations.list.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.strictpro.ui.R
import com.strictpro.ui.domain.model.ViolationType
import com.strictpro.ui.presentation.ui.navigation.BottomNavigationRoute
import com.strictpro.ui.presentation.violations.list.ui.ViolationsScreen
import kotlinx.serialization.Serializable

@Serializable
internal class ViolationsScreenRoute : BottomNavigationRoute(
    nameRes = R.string.violations,
    iconRes = R.drawable.violation,
)

internal fun NavGraphBuilder.violationsScreen(
    onViolationTypeClicked: (ViolationType) -> Unit,
) {
    composable<ViolationsScreenRoute> {
        ViolationsScreen(onViolationTypeClicked = onViolationTypeClicked)
    }
}