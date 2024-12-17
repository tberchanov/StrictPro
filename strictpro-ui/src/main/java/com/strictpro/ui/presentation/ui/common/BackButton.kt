package com.strictpro.ui.presentation.ui.common

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.strictpro.ui.R
import com.strictpro.ui.presentation.ui.navigation.LocalNavController

@Composable
internal fun BackButton() {
    val navController = LocalNavController.current
    IconButton(onClick = { navController?.navigateUp() }) {
        Icon(
            ImageVector.vectorResource(id = R.drawable.arrow_back),
            contentDescription = "Back",
        )
    }
}

@Preview
@Composable
private fun BackButtonPreview() {
    BackButton()
}
