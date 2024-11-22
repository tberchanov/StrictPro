package com.strictpro.ui.presentation.violations.list.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.strictpro.ui.domain.model.ViolationQuantity
import com.strictpro.ui.domain.model.ViolationType
import com.strictpro.ui.presentation.ui.theme.AccentRed
import com.strictpro.ui.presentation.ui.theme.PrimaryRed

@Composable
internal inline fun ViolationItem(
    violationsQuantity: ViolationQuantity,
    modifier: Modifier = Modifier,
    crossinline onClick: (ViolationQuantity) -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF292929))
            .clickable(
                onClick = { onClick(violationsQuantity) },
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = PrimaryRed),
            ),
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
                text = violationsQuantity.type.value,
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
    ViolationItem(ViolationQuantity(42, ViolationType("Name")))
}

@Preview
@Composable
private fun ViolationItemPreviewLonQuantity() {
    ViolationItem(ViolationQuantity(41415, ViolationType("Name")))
}