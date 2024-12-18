package com.strictpro.ui.presentation.violations.history.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.strictpro.ui.presentation.ui.common.StackTraceItems
import com.strictpro.ui.presentation.ui.theme.LightGray
import com.strictpro.ui.presentation.ui.theme.PrimaryRed
import com.strictpro.ui.presentation.violations.history.model.ViolationHistoryItemUI
import com.strictpro.ui.presentation.violations.history.util.formatViolationDate

@Composable
internal fun ViolationHistoryItem(
    violationHistoryItemUI: ViolationHistoryItemUI,
    modifier: Modifier = Modifier,
    onClick: (ViolationHistoryItemUI) -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF292929))
            .clickable(
                onClick = { onClick(violationHistoryItemUI) },
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = PrimaryRed),
            ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                color = Color.White,
                fontSize = 16.sp,
                text = violationHistoryItemUI.violationName,
            )
            Box(modifier = Modifier.size(12.dp))
            StackTraceItems(stackTraceItems = violationHistoryItemUI.filteredStackTraceItems)
            Box(modifier = Modifier.size(12.dp))
            Text(
                color = LightGray,
                text = violationHistoryItemUI.formattedDate,
            )
        }
    }
}

@Preview
@Composable
private fun ViolationHistoryItemPreview() {
    ViolationHistoryItem(
        ViolationHistoryItemUI(
            violationId = "0",
            dateMillis = 0,
            formattedDate = formatViolationDate(System.currentTimeMillis()),
            violationName = "Violation info",
            filteredStackTraceItems = listOf(
                "Stack trace item 1",
                "Stack trace item 2",
                "Stack trace item 3",
            ),
        )
    )
}