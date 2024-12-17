package com.strictpro.ui.presentation.violations.list.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.strictpro.ui.domain.model.ViolationQuantity
import com.strictpro.ui.domain.model.ViolationType
import com.strictpro.ui.presentation.ui.ListItemHorizontalPadding
import com.strictpro.ui.presentation.ui.ListItemVerticalPadding
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun ViolationsList(
    violations: ImmutableList<ViolationQuantity>,
    onClick: (ViolationQuantity) -> Unit = {},
) {
    LazyColumn(modifier = Modifier.padding(bottom = ListItemVerticalPadding)) {
        items(
            violations.size,
            key = { violations[it].type.value },
        ) { index ->
            ViolationItem(
                modifier = Modifier.padding(
                    start = ListItemHorizontalPadding,
                    end = ListItemHorizontalPadding,
                    top = ListItemVerticalPadding,
                ),
                violationsQuantity = violations[index],
                onClick = onClick,
            )
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF121212, // DarkGray
)
@Composable
internal fun ViolationsListPreview() {
    ViolationsList(
        listOf(
            ViolationQuantity(1, ViolationType("Name")),
            ViolationQuantity(10, ViolationType("Name")),
            ViolationQuantity(123, ViolationType("Name")),
            ViolationQuantity(1234, ViolationType("Name")),
            ViolationQuantity(112, ViolationType("Name")),
            ViolationQuantity(90, ViolationType("Name")),
            ViolationQuantity(7, ViolationType("Name")),
        ).toImmutableList()
    )
}