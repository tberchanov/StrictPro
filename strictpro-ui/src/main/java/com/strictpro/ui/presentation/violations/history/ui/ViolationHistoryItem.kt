package com.strictpro.ui.presentation.violations.history.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

@Composable
internal fun StackTraceItems(
    stackTraceItems: List<String>,
    modifier: Modifier = Modifier,
) {
    val localDensity = LocalDensity.current
    Column(modifier = modifier) {
        stackTraceItems.forEach { item ->
            var columnHeightDp by remember {
                mutableStateOf(0.dp)
            }
            Row(
                modifier = Modifier
                    .onGloballyPositioned { coordinates ->
                        columnHeightDp = with(localDensity) { coordinates.size.height.toDp() }
                    }
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                StackTraceItemMarker(
                    modifier = Modifier
                        .size(width = 30.dp, height = columnHeightDp)
                        .padding(end = 8.dp),
                )
                Text(
                    modifier = Modifier.padding(vertical = 4.dp),
                    text = item,
                    color = Color.White,
                    fontSize = 12.sp,
                    lineHeight = 14.sp
                )
            }
        }
    }
}

@Preview
@Composable
private fun StackTraceItemMarkerPreview() {
    StackTraceItemMarker(modifier = Modifier.size(240.dp))
}

@Composable
internal fun StackTraceItemMarker(
    modifier: Modifier = Modifier,
    color: Color = Color.Red,
    thickness: Dp = 2.dp,
) {
    Canvas(modifier = modifier) {
        drawLine(
            color = color,
            start = Offset(thickness.toPx(), 0F),
            end = Offset(thickness.toPx(), size.height),
            strokeWidth = thickness.toPx(),
        )
        drawLine(
            color = color,
            start = Offset(thickness.toPx(), size.height / 2),
            end = Offset(size.width, size.height / 2),
            strokeWidth = thickness.toPx(),
        )
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