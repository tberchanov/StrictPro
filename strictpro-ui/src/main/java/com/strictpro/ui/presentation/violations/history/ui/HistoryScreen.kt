package com.strictpro.ui.presentation.violations.history.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.strictpro.ui.domain.model.ViolationType
import com.strictpro.ui.presentation.ui.common.BackButton
import com.strictpro.ui.presentation.ui.theme.DarkGray
import com.strictpro.ui.presentation.violations.history.model.ViolationHistoryItemUI
import com.strictpro.ui.presentation.violations.history.viewmodel.ViolationsHistoryState
import com.strictpro.ui.presentation.violations.history.viewmodel.ViolationsHistoryViewModel
import kotlinx.collections.immutable.toImmutableList
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun HistoryScreen(
    viewModel: ViolationsHistoryViewModel = koinViewModel(),
    violationType: ViolationType? = null,
) {
    LaunchedEffect(Unit) {
        viewModel.loadData(violationType)
    }
    val state by viewModel.state.collectAsStateWithLifecycle()
    HistoryScreenContent(state)
}

@Composable
internal fun HistoryScreenContent(
    violationsHistoryState: ViolationsHistoryState,
) {
    Scaffold(
        backgroundColor = DarkGray,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = violationsHistoryState.title,
                    )
                },
                navigationIcon = if (violationsHistoryState.showBackButton) {
                    { BackButton() }
                } else {
                    null
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ViolationsHistoryList(violationsHistoryState.historyItems.toImmutableList())
        }
    }
}

@Preview
@Composable
private fun HistoryScreenContentPreview() {
    val historyItems = remember {
        listOf(
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
    }
    HistoryScreenContent(
        ViolationsHistoryState(
            title = "Preview Title",
            historyItems = historyItems
        )
    )
}

