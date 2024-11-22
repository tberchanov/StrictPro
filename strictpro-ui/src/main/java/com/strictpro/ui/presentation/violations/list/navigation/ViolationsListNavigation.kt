package com.strictpro.ui.presentation.violations.list.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.strictpro.ui.R
import com.strictpro.ui.domain.model.ViolationQuantity
import com.strictpro.ui.domain.model.ViolationType
import com.strictpro.ui.presentation.ui.navigation.TopLevelRoute
import com.strictpro.ui.presentation.ui.theme.Icons
import com.strictpro.ui.presentation.violations.list.ui.ViolationsScreen
import kotlinx.serialization.Serializable

@Serializable
internal object ViolationsScreenRoute

@Composable
internal fun violationsTopLevelRoute() =
    TopLevelRoute(stringResource(R.string.violations), ViolationsScreenRoute, Icons.Violation)

internal fun NavGraphBuilder.violationsScreen(
    onViolationTypeClicked: (ViolationType) -> Unit,
) {
    composable<ViolationsScreenRoute> {
        ViolationsScreen(onViolationTypeClicked = onViolationTypeClicked)
    }
}