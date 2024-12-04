package com.example.stockshartapp.presentation

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.stockshartapp.data.Bar
import kotlinx.parcelize.Parcelize
import kotlin.math.roundToInt

@Parcelize
data class StockChartUiState(
    val visibleBarsCount: Int = 100,
    val stockChartWidth: Float = 1f,
    val stockChartHeight: Float = 1f,
    val scrolledBy: Float = 0f,
    val barList: List<Bar>,
) : Parcelable {
    val barWidth: Float
        get() = stockChartWidth / visibleBarsCount

    private val visibleBars: List<Bar>
        get() {
            val startIndex = (scrolledBy / barWidth).roundToInt().coerceAtLeast(0)
            val endIndex = (startIndex + visibleBarsCount).coerceAtMost(barList.size)
            return barList.subList(startIndex, endIndex)
        }
    val max: Float
        get() = visibleBars.maxOf { it.high }
    val min: Float
        get() = visibleBars.minOf { it.low }
    val pxPerPoint: Float
        get() = stockChartHeight / (max - min)
}

@Composable
fun rememberStockChartUiState(bars: List<Bar>): MutableState<StockChartUiState> = rememberSaveable {
    mutableStateOf(StockChartUiState(barList = bars))
}