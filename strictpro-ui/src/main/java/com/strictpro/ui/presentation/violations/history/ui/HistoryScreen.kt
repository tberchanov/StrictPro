package com.strictpro.ui.presentation.violations.history.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.serialization.Serializable

@Serializable
object HistoryScreenRoute

@Composable
fun HistoryScreen() {
    HistoryScreenContent()
}

@Composable
fun HistoryScreenContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) { Text("History") }
}

@Preview
@Composable
fun HistoryScreenContentPreview() {
    HistoryScreenContent()
}

