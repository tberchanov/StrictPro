package com.strictpro.ui.presentation.ui.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun StackTraceItems(
    stackTraceItems: List<String>,
    modifier: Modifier = Modifier,
    cutLast: Boolean = false,
    highlightPredicate: (String) -> Boolean = { false },
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
                    cutBottom = cutLast && stackTraceItems.last() == item,
                    modifier = Modifier
                        .size(width = 30.dp, height = columnHeightDp)
                        .padding(end = 8.dp),
                )
                val isHighlighted = remember(item) { highlightPredicate(item) }
                Text(
                    modifier = Modifier.padding(vertical = 4.dp),
                    fontWeight = if (isHighlighted) FontWeight.Bold else Companion.Normal,
                    text = item,
                    color = Color.White,
                    fontSize = if (isHighlighted) 14.sp else 12.sp,
                    lineHeight = 14.sp
                )
            }
        }
    }
}

@Preview
@Composable
private fun StackTraceItemsPreview() {
    StackTraceItems(
        cutLast = true,
        stackTraceItems = listOf(
            "violation1",
            "violation2",
            "violation3",
            "violation4",
        ),
        highlightPredicate = { it == "violation2" },
    )
}

@Preview
@Composable
private fun StackTraceItemMarkerPreview() {
    StackTraceItemMarker(modifier = Modifier.size(240.dp), cutBottom = true)
}

@Composable
internal fun StackTraceItemMarker(
    cutBottom: Boolean = false,
    modifier: Modifier = Modifier,
    color: Color = Color.Red,
    thickness: Dp = 2.dp,
) {
    val cutBottomCoefficient = if (cutBottom) 2 else 1
    Canvas(modifier = modifier) {
        drawLine(
            color = color,
            start = Offset(thickness.toPx(), 0F),
            end = Offset(thickness.toPx(), size.height / cutBottomCoefficient),
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