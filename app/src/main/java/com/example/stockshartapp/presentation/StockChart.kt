package com.example.stockshartapp.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.rememberTextMeasurer
import com.example.stockshartapp.data.Bar

@Composable
fun StockChart(
    modifier: Modifier = Modifier,
    bars: List<Bar>,
    currentFrame: TimeFrame,
    onSelectTimeFrame: (TimeFrame) -> Unit,
) {
    val uiState = rememberStockChartUiState(bars)
    val textMeasurer = rememberTextMeasurer()

    Chart(
        modifier = modifier,
        uiState = uiState,
        timeFrame = currentFrame,
        onUiStateChange = { stockChartUiState ->
            uiState.value = stockChartUiState
        }
    )
    bars.firstOrNull()?.let { bar ->
        Prices(
            uiState = uiState,
            lastPrice = bar.close,
            textMeasurer = textMeasurer,
            modifier = modifier
        )
    }
    TimeFrames(
        currentFrame = currentFrame,
        onChooseFrame = onSelectTimeFrame,
    )
}





