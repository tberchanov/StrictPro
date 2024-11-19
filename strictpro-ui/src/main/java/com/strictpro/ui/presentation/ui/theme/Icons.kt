package com.strictpro.ui.presentation.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.strictpro.ui.R

object Icons {
    val Violation: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.violation)
    val History: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.history)
}