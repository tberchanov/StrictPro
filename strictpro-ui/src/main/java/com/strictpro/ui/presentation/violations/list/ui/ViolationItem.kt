package com.strictpro.ui.presentation.violations.list.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.strictpro.ui.domain.model.ViolationQuantity
import com.strictpro.ui.presentation.ui.theme.AccentRed

@Composable
fun ViolationItem(
    violationsQuantity: ViolationQuantity,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF292929)),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp),
        ) {
            ViolationsCounter(violationsQuantity.quantity)
            Box(modifier = Modifier.size(12.dp))
            Text(
                color = Color.White,
                fontSize = 16.sp,
                text = violationsQuantity.violationName,
            )
        }
    }
}

@Composable
private fun ViolationsCounter(quantity: Int) {
    Box(
        modifier = Modifier
            .defaultMinSize(32.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(AccentRed),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier
                .padding(8.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            text = quantity.toString(),
        )
    }
}

@Preview
@Composable
private fun ViolationItemPreview() {
    ViolationItem(ViolationQuantity(42, "Name"))
}

@Preview
@Composable
fun ViolationItemPreviewLonQuantity() {
    ViolationItem(ViolationQuantity(41415, "Name"))
}