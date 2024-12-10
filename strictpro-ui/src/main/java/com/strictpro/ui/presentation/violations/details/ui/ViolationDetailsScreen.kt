package com.strictpro.ui.presentation.violations.details.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.strictpro.ui.presentation.ui.common.BackButton
import com.strictpro.ui.presentation.ui.theme.DarkGray
import com.strictpro.ui.presentation.violations.details.viewmodel.ViolationDetailsState
import com.strictpro.ui.presentation.violations.details.viewmodel.ViolationDetailsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun ViolationDetailsScreen(
    viewModel: ViolationDetailsViewModel = koinViewModel(),
    violationId: String,
) {
    LaunchedEffect(Unit) {
        viewModel.loadData(violationId)
    }
    val state by viewModel.state.collectAsStateWithLifecycle()
    ViolationDetailsScreenContent(state)
}

@Composable
internal fun ViolationDetailsScreenContent(
    violationDetailsState: ViolationDetailsState,
) {
    Scaffold(
        backgroundColor = DarkGray,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = violationDetailsState.title,
                    )
                },
                navigationIcon = {
                    BackButton()
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
        ) {
            Button(onClick = {}) {
                Text("Print trace to logcat")
            }
            Button(onClick = {}) {
                Text("Print base64 to logcat")
            }
            // stack trace list
        }
    }
}

@Preview
@Composable
internal fun ViolationDetailsScreenContentPreview() {
    ViolationDetailsScreenContent(
        ViolationDetailsState()
    )
}