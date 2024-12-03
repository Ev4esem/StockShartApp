package com.example.stockshartapp.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

private const val MIN_VISIBLE_BARS_COUNT = 20

@Composable
fun Chart(
    modifier: Modifier = Modifier,
    timeFrame: TimeFrame,
    uiState: State<StockChartUiState>,
    onUiStateChange: (StockChartUiState) -> Unit
) {
    val currentState = uiState.value
    val transformableState = rememberTransformableState { zoomChange, panChange, _ ->
        val visibleBarsCount = (currentState.visibleBarsCount / zoomChange).roundToInt()
            .coerceIn(MIN_VISIBLE_BARS_COUNT, currentState.barList.size)

        val scrolledBy = (currentState.scrolledBy + panChange.x)
            .coerceIn(0f, currentState.barWidth * currentState.barList.size - currentState.stockChartWidth)
        onUiStateChange(
            currentState.copy(
                visibleBarsCount = visibleBarsCount,
                scrolledBy = scrolledBy
            )
        )
    }
    val textMeasurer = rememberTextMeasurer()
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .clipToBounds()
            .padding(vertical = 32.dp)
            .transformable(transformableState)
            .onSizeChanged {
                onUiStateChange(
                    currentState.copy(
                        stockChartWidth = it.width.toFloat(),
                        stockChartHeight = it.height.toFloat()
                    )
                )
            },
    ) {
        val min = currentState.min
        val pxPerPoint = currentState.pxPerPoint

        translate(left = currentState.scrolledBy) {
            currentState.barList.forEachIndexed { index, bar ->
                val offsetX = size.width - index * currentState.barWidth
                drawTimeDelimiter(
                    measurer = textMeasurer,
                    bar = bar,
                    nextBar = if (index < currentState.barList.size - 1) currentState.barList[index + 1] else null,
                    timeFrame = timeFrame,
                    offsetX = offsetX,
                )
                drawLine(
                    color = if (bar.open < bar.close) Color.Green else Color.Red,
                    start = Offset(x = offsetX, y = size.height - ((bar.low - min) * pxPerPoint)),
                    end = Offset(x = offsetX, y = size.height - ((bar.high - min) * pxPerPoint)),
                    strokeWidth = 1.dp.toPx(),
                )
                drawLine(
                    color = if (bar.open < bar.close) Color.Green else Color.Red,
                    start = Offset(x = offsetX, y = size.height - ((bar.open - min) * pxPerPoint)),
                    end = Offset(x = offsetX, y = size.height - ((bar.close - min) * pxPerPoint)),
                    strokeWidth = currentState.barWidth / 2,
                )
            }
        }
    }
}

