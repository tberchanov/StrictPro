package com.strictpro.ui.presentation.violations.history.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.strictpro.ui.domain.model.ViolationType

@Composable
fun HistoryFullScreen(violationType: ViolationType?) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text("Full screen history")
            })
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            HistoryScreen(violationType = violationType)
        }
    }
}

