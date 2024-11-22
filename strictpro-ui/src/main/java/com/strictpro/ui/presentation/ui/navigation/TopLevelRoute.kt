package com.strictpro.ui.presentation.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector

internal data class TopLevelRoute<T : Any>(
    val name: String,
    val route: T,
    val icon: ImageVector,
)
