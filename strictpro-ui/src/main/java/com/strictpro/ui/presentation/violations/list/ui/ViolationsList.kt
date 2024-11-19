package com.strictpro.ui.presentation.violations.list.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.strictpro.ui.domain.model.ViolationQuantity
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun ViolationsList(violations: ImmutableList<ViolationQuantity>) {
    LazyColumn {
        items(
            violations.size,
            key = { violations[it].violationName },
        ) { index ->
            // TODO on click open ViolationHistory of the selected type
            ViolationItem(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                violationsQuantity = violations[index],
            )
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF121212, // DarkGray
)
@Composable
fun ViolationsListPreview() {
    ViolationsList(
        listOf(
            ViolationQuantity(1, "Name"),
            ViolationQuantity(10, "Name"),
            ViolationQuantity(123, "Name"),
            ViolationQuantity(1234, "Name"),
            ViolationQuantity(112, "Name"),
            ViolationQuantity(90, "Name"),
            ViolationQuantity(7, "Name"),
        ).toImmutableList()
    )
}