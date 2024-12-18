package com.strictpro.ui.presentation.unavailable.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.strictpro.ui.presentation.unavailable.ui.UnavailableScreen
import kotlinx.serialization.Serializable

@Serializable
internal object UnavailableScreenRoute

internal fun NavGraphBuilder.unavailableScreen() {
    composable<UnavailableScreenRoute> {
        UnavailableScreen()
    }
}