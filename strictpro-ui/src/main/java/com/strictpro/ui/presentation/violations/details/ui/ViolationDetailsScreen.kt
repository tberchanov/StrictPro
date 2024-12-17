package com.strictpro.ui.presentation.violations.details.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.strictpro.ui.R
import com.strictpro.ui.presentation.ui.common.BackButton
import com.strictpro.ui.presentation.ui.theme.DarkGray
import com.strictpro.ui.presentation.violations.details.viewmodel.ViolationDetailsState
import com.strictpro.ui.presentation.violations.details.viewmodel.ViolationDetailsViewModel
import com.strictpro.ui.presentation.violations.history.ui.StackTraceItems
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
    ViolationDetailsScreenContent(
        state,
        onPrintTraceClicked = { viewModel.printTrace() },
        onPrintBase64Clicked = { viewModel.printBase64Trace() },
    )
}

@Composable
internal fun ViolationDetailsScreenContent(
    violationDetailsState: ViolationDetailsState,
    onPrintTraceClicked: () -> Unit = {},
    onPrintBase64Clicked: () -> Unit = {},
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
            Button(onClick = onPrintTraceClicked) {
                Text(stringResource(R.string.print_trace_to_logcat))
            }
            Button(onClick = onPrintBase64Clicked) {
                Text(stringResource(R.string.print_base64_to_logcat))
            }
            StackTraceItems(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 4.dp),
                stackTraceItems = violationDetailsState.trace,
                cutLast = true,
            )
        }
    }
}

@Preview
@Composable
internal fun ViolationDetailsScreenContentPreview() {
    ViolationDetailsScreenContent(
        ViolationDetailsState(
            title = "Violation title",
            trace = listOf(
                "Stack trace item 1",
                "Stack trace item 2",
                "Stack trace item 3",
            ),
        )
    )
}