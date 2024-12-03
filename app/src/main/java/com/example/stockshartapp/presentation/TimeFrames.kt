package com.example.stockshartapp.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TimeFrames(
    currentFrame: TimeFrame,
    onChooseFrame: (TimeFrame) -> Unit,
) {

    Row(
        modifier = Modifier
            .wrapContentSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        TimeFrame.entries.forEach { timeFrame ->
            val label = when(timeFrame) {
                TimeFrame.MIN_5 -> "M5"
                TimeFrame.MIN_15 -> "M15"
                TimeFrame.MIN_30 -> "M30"
                TimeFrame.HOUR_1 -> "H1"
            }
            val isSelected = currentFrame == timeFrame
            AssistChip(
                onClick = {
                    onChooseFrame(timeFrame)
                },
                label = {
                    Text(label)
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (isSelected) Color.White else Color.Black,
                    labelColor = if (isSelected) Color.Black else Color.White,
                )

            )
        }
    }
}