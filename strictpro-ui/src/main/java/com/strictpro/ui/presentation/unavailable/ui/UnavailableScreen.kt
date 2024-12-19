package com.strictpro.ui.presentation.unavailable.ui

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.strictpro.ui.R
import com.strictpro.ui.presentation.ui.theme.DarkGray

@Composable
internal fun UnavailableScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGray)
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(1.0f))
        Text(
            stringResource(R.string.current_api_format, Build.VERSION.SDK_INT),
            modifier = Modifier.padding(bottom = 8.dp),
            fontSize = 20.sp,
            color = Color.White,
        )
        Text(
            stringResource(R.string.please_run_above_api_28),
            modifier = Modifier.padding(bottom = 8.dp),
            color = Color.White,
            textAlign = TextAlign.Center,
        )
        Text(
            stringResource(R.string.use_various_penalties),
            textAlign = TextAlign.Center,
            color = Color.White,
        )
        Spacer(modifier = Modifier.weight(1.0f))
        Column(
            modifier = Modifier
                .border(1.dp, Color.White, shape = RoundedCornerShape(4.dp))
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                stringResource(R.string.why),
                modifier = Modifier.padding(bottom = 8.dp),
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
            )
            Text(
                stringResource(R.string.why_unavailable_below_28),
                modifier = Modifier.padding(bottom = 8.dp),
                color = Color.White,
                textAlign = TextAlign.Center,
            )
        }
        Spacer(modifier = Modifier.weight(0.5f))
    }
}

@Preview
@Composable
private fun UnavailableScreenPreview() {
    UnavailableScreen()
}
