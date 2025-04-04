package com.strictpro.ui.presentation.violations.history.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.strictpro.ui.presentation.ui.ListItemHorizontalPadding
import com.strictpro.ui.presentation.ui.ListItemVerticalPadding
import com.strictpro.ui.presentation.violations.history.model.ViolationHistoryItemUI
import com.strictpro.ui.presentation.violations.history.util.formatViolationDate
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun ViolationsHistoryList(
    violations: ImmutableList<ViolationHistoryItemUI>,
    onClick: (ViolationHistoryItemUI) -> Unit = {},
) {
    LazyColumn(modifier = Modifier.padding(bottom = ListItemVerticalPadding)) {
        items(
            violations.size,
            key = { violations[it].dateMillis },
        ) { index ->
            ViolationHistoryItem(
                modifier = Modifier.padding(
                    start = ListItemHorizontalPadding,
                    end = ListItemHorizontalPadding,
                    top = ListItemVerticalPadding,
                ),
                violationHistoryItemUI = violations[index],
                onClick = onClick
            )
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF121212, // DarkGray
)
@Composable
private fun ViolationsHistoryListPreview() {
    ViolationsHistoryList(
        listOf(
            ViolationHistoryItemUI(
                violationId = "0",
                dateMillis = 0,
                formattedDate = formatViolationDate(0),
                violationName = "Violation info",
                filteredStackTraceItems = listOf(
                    "Stack trace item 1",
                    "Stack trace item 2",
                    "Stack trace item 3",
                ),
            ),
            ViolationHistoryItemUI(
                violationId = "0",
                dateMillis = 1,
                formattedDate = formatViolationDate(System.currentTimeMillis()),
                violationName = "Violation info",
                filteredStackTraceItems = listOf(
                    "Stack trace item 1",
                    "Stack trace item 2",
                    "Stack trace item 3",
                ),
            ),
            ViolationHistoryItemUI(
                violationId = "0",
                dateMillis = 2,
                formattedDate = formatViolationDate(3000),
                violationName = "Violation info",
                filteredStackTraceItems = listOf(
                    "Stack trace item 1",
                    "Stack trace item 2",
                    "Stack trace item 3",
                ),
            ),
            ViolationHistoryItemUI(
                violationId = "0",
                dateMillis = 3,
                formattedDate = formatViolationDate(1000),
                violationName = "Violation info",
                filteredStackTraceItems = listOf(
                    "Stack trace item 1",
                    "Stack trace item 2",
                    "Stack trace item 3",
                ),
            ),
        ).toImmutableList()
    )
}