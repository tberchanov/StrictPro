package com.strictpro.ui.presentation.violations.list.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.strictpro.ui.domain.model.ViolationQuantity
import com.strictpro.ui.domain.model.ViolationType
import com.strictpro.ui.presentation.ui.common.NoViolations
import com.strictpro.ui.presentation.ui.theme.DarkGray
import com.strictpro.ui.presentation.violations.list.viewmodel.ViolationsListState
import com.strictpro.ui.presentation.violations.list.viewmodel.ViolationsViewModel
import kotlinx.collections.immutable.toImmutableList
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun ViolationsScreen(
    viewModel: ViolationsViewModel = koinViewModel(),
    onViolationTypeClicked: (ViolationType) -> Unit = {},
) {
    LaunchedEffect(Unit) {
        viewModel.loadData()
    }
    val state by viewModel.state.collectAsStateWithLifecycle()
    ViolationsScreenContent(
        state,
        onViolationTypeClicked,
    )
}

@Composable
internal fun ViolationsScreenContent(
    state: ViolationsListState,
    onViolationTypeClicked: (ViolationType) -> Unit = {},
) {
    if (state.violations.isEmpty()) {
        NoViolations()
    } else {
        Scaffold(
            backgroundColor = DarkGray,
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = state.title)
                    },
                )
            }
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                ViolationsList(
                    state.violations.toImmutableList(),
                    onClick = { onViolationTypeClicked(it.type) },
                )
            }
        }
    }
}

@Preview
@Composable
private fun ViolationsScreenContentPreview() {
    val violations = remember {
        listOf(
            ViolationQuantity(1, ViolationType("Name")),
            ViolationQuantity(10, ViolationType("Name")),
            ViolationQuantity(123, ViolationType("Name")),
            ViolationQuantity(1234, ViolationType("Name")),
            ViolationQuantity(112, ViolationType("Name")),
            ViolationQuantity(90, ViolationType("Name")),
            ViolationQuantity(7, ViolationType("Name")),
        )
    }
    ViolationsScreenContent(
        ViolationsListState(
            title = "Preview Title",
            violations = violations
        )
    )
}
