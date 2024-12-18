package com.strictpro.ui.presentation.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.strictpro.ui.R
import com.strictpro.ui.presentation.ui.theme.DarkGray

@Composable
internal fun NoViolations() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGray),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            stringResource(R.string.no_violations),
            color = Color.White,
            fontSize = 20.sp
        )
        Text(
            stringResource(R.string.that_is_good),
            modifier = Modifier.padding(top = 8.dp),
            color = Color.White,
            fontSize = 18.sp
        )
    }
}

@Preview
@Composable
private fun NoViolationsPreview() {
    NoViolations()
}