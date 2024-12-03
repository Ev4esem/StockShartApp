package com.example.stockshartapp.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.sp
import com.example.stockshartapp.data.Bar
import java.util.Calendar
import java.util.Locale

fun DrawScope.drawTimeDelimiter(
    measurer: TextMeasurer,
    bar: Bar,
    nextBar: Bar?,
    timeFrame: TimeFrame,
    offsetX: Float
) {
    val calendar = bar.calendar
    val minutes = calendar.get(Calendar.MINUTE)
    val hours = calendar.get(Calendar.HOUR_OF_DAY)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val shouldDrawDelimiter = when(timeFrame) {
        TimeFrame.MIN_5 -> minutes == 0
        TimeFrame.MIN_15 -> minutes == 0 && hours % 2 == 0
        TimeFrame.MIN_30, TimeFrame.HOUR_1 -> {
            val nextBarDay = nextBar?.calendar?.get(Calendar.DAY_OF_MONTH)
            day != nextBarDay
        }
    }
    if (!shouldDrawDelimiter) return
    val nameOfMonth = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault() )
    val text = when(timeFrame) {
        TimeFrame.MIN_5, TimeFrame.MIN_15 -> String.format("%02d:00", hours)
        TimeFrame.MIN_30, TimeFrame.HOUR_1 -> String.format("%s %s", day, nameOfMonth)
    }
    val textLayoutResult = measurer.measure(
        text = text,
        style = TextStyle(
            color = Color.White,
            fontSize = 12.sp,
        )
    )
    drawLine(
        color = Color.Gray,
        start = Offset(offsetX, 0f),
        end = Offset(offsetX, size.height)
    )
    drawText(
        textLayoutResult = textLayoutResult,
        topLeft = Offset(offsetX - (textLayoutResult.size.width / 2), size.height)
    )
}