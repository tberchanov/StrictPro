package com.strictpro.ui.presentation.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation.NavBackStackEntry
import kotlinx.serialization.Serializable

@Serializable
internal abstract class BottomNavigationRoute(
    /*
    * `showBottomBar` is implicitly used to determine whether to show the bottom bar or not.
    * */
    @Suppress("unused")
    // TODO need to test is it working fine with minification
    val showBottomBar: Boolean = true,
    @StringRes
    val nameRes: Int,
    @DrawableRes
    val iconRes: Int,
)

internal fun NavBackStackEntry?.isBottomNavigationEntry(): Boolean =
    this?.arguments?.getBoolean("showBottomBar") == true