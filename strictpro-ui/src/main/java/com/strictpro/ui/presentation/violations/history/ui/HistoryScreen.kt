package com.strictpro.ui.presentation.violations.history.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.strictpro.ui.domain.model.ViolationType
import com.strictpro.ui.presentation.ui.theme.DarkGray
import com.strictpro.ui.presentation.violations.history.model.ViolationHistoryItemUI
import com.strictpro.ui.presentation.violations.history.viewmodel.ViolationsHistoryState
import com.strictpro.ui.presentation.violations.history.viewmodel.ViolationsHistoryViewModel
import com.strictpro.ui.presentation.violations.list.viewmodel.ViolationsViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun HistoryScreen(
    viewModel: ViolationsHistoryViewModel = koinViewModel(),
    violationType: ViolationType? = null,
) {
    LaunchedEffect(Unit) {
        viewModel.loadData(violationType)
    }
    val state by viewModel.state.collectAsState()
    HistoryScreenContent(state)
}

@Composable
internal fun HistoryScreenContent(
    violationsHistoryState: ViolationsHistoryState,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGray),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ViolationsHistoryList(violationsHistoryState.historyItems.toImmutableList())
    }
}

@Preview
@Composable
private fun HistoryScreenContentPreview() {
    HistoryScreenContent(
        ViolationsHistoryState(
            historyItems = listOf(
                ViolationHistoryItemUI(
                    dateMillis = 0,
                    formattedDate = "\uD83D\uDDD3\uFE0F 09.25  \uD83D\uDD52 03:11:13",
                    violationName = "Violation info",
                    filteredStackTraceItems = listOf(
                        "Stack trace item 1",
                        "Stack trace item 2",
                        "Stack trace item 3",
                    ),
                ),
                ViolationHistoryItemUI(
                    dateMillis = 1,
                    formattedDate = "\uD83D\uDDD3\uFE0F 09.25  \uD83D\uDD52 03:11:13",
                    violationName = "Violation info",
                    filteredStackTraceItems = listOf(
                        "Stack trace item 1",
                        "Stack trace item 2",
                        "Stack trace item 3",
                    ),
                ),
                ViolationHistoryItemUI(
                    dateMillis = 2,
                    formattedDate = "\uD83D\uDDD3\uFE0F 10.13  \uD83D\uDD52 14:45:56",
                    violationName = "Violation info",
                    filteredStackTraceItems = listOf(
                        "Stack trace item 1",
                        "Stack trace item 2",
                        "Stack trace item 3",
                    ),
                ),
                ViolationHistoryItemUI(
                    dateMillis = 3,
                    formattedDate = "\uD83D\uDDD3\uFE0F 10.21, 23:59:39",
                    violationName = "Violation info",
                    filteredStackTraceItems = listOf(
                        "Stack trace item 1",
                        "Stack trace item 2",
                        "Stack trace item 3",
                    ),
                ),
            )
        )
    )
}

