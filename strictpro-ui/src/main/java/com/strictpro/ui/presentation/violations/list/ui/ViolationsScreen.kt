package com.strictpro.ui.presentation.violations.list.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.strictpro.ui.R
import com.strictpro.ui.domain.model.ViolationQuantity
import com.strictpro.ui.presentation.ui.theme.DarkGray
import com.strictpro.ui.presentation.ui.theme.PrimaryRed
import com.strictpro.ui.presentation.violations.list.viewmodel.ViolationsListState
import com.strictpro.ui.presentation.violations.list.viewmodel.ViolationsViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
object ViolationsScreenRoute

@Composable
fun ViolationsScreen(viewModel: ViolationsViewModel = koinViewModel()) {
    LaunchedEffect(Unit) {
        viewModel.loadData()
    }
    val state by viewModel.state.collectAsState()
    ViolationsScreenContent(state)
}

@Composable
fun ViolationsScreenContent(state: ViolationsListState) {
    Box(
        modifier = Modifier
            .background(DarkGray)
            .fillMaxSize(),
    ) {
        Column {
            Text(
                modifier = Modifier.padding(18.dp),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                text = stringResource(R.string.distinct_violations_format, state.violations.size),
            )
            ViolationsList(state.violations.toImmutableList())
        }

        if (state.loading) {
            CircularProgressIndicator(
                modifier = Modifier.width(64.dp),
                color = PrimaryRed,
            )
        }
    }
}

@Preview
@Composable
fun ViolationsScreenContentPreview() {
    ViolationsScreenContent(
        ViolationsListState(
            violations = listOf(
                ViolationQuantity(1, "Name"),
                ViolationQuantity(10, "Name"),
                ViolationQuantity(123, "Name"),
                ViolationQuantity(1234, "Name"),
                ViolationQuantity(112, "Name"),
                ViolationQuantity(90, "Name"),
                ViolationQuantity(7, "Name"),
            )
        )
    )
}

@Preview
@Composable
fun ViolationsScreenContentPreviewLoading() {
    ViolationsScreenContent(
        ViolationsListState(
            loading = true,
            violations = listOf(
                ViolationQuantity(1, "Name"),
                ViolationQuantity(10, "Name"),
                ViolationQuantity(123, "Name"),
                ViolationQuantity(1234, "Name"),
                ViolationQuantity(112, "Name"),
                ViolationQuantity(90, "Name"),
                ViolationQuantity(7, "Name"),
            )
        )
    )
}
