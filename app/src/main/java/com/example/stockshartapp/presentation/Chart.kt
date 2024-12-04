package com.example.stockshartapp.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
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
    var isShouldShowCurrentPrice by remember {
        mutableStateOf(false)
    }
    var currentPriceOffset by remember {
        mutableStateOf(Offset.Zero)
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
            }
            .pointerInput(Unit) {
                detectDragGesturesAfterLongPress(
                    onDragStart = {
                        isShouldShowCurrentPrice = true
                        currentPriceOffset = it
                    },
                    onDrag = { change, _ ->
                        currentPriceOffset = change.position
                    },
                    onDragEnd = {
                        isShouldShowCurrentPrice = false
                    }
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
        if (isShouldShowCurrentPrice) {
            drawLinePrice(
                offset = currentPriceOffset,
                pxPerPoint = pxPerPoint,
                measurer = textMeasurer,
                min = min,
            )
        }
    }
}

private fun DrawScope.drawLinePrice(
    offset: Offset,
    pxPerPoint: Float,
    min: Float,
    measurer: TextMeasurer,
) {
    val textLayoutResult = measurer.measure(
        text = (min + ((size.height - offset.y) / pxPerPoint)).toString(),
        style = TextStyle(
            color = Color.White
        )
    )
    drawLine(
        color = Color.White,
        start = Offset(x = offset.x, y = 0f),
        end = offset,
        pathEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(4.dp.toPx(), 4.dp.toPx())
        )
    )
    drawLine(
        color = Color.White,
        start = Offset(x = 0f, y = offset.y),
        end = offset,
        pathEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(4.dp.toPx(), 4.dp.toPx())
        )
    )
    drawLine(
        color = Color.White,
        start = Offset(x = offset.x, y = size.height),
        end = offset,
        pathEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(4.dp.toPx(), 4.dp.toPx())
        )
    )
    drawLine(
        color = Color.White,
        start = Offset(x = size.width, y = offset.y),
        end = offset,
        pathEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(4.dp.toPx(), 4.dp.toPx())
        )
    )
    drawText(
        textLayoutResult = textLayoutResult,
        topLeft = Offset(x = offset.x + 10.dp.toPx(), y = offset.y - 20.dp.toPx())
    )
}