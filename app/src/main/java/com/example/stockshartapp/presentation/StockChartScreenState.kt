package com.example.stockshartapp.presentation

import com.example.stockshartapp.data.Bar

sealed interface StockChartScreenState {
    data object Initial: StockChartScreenState
    data object Loading: StockChartScreenState
    data class Content(
        val barList: List<Bar>,
        val timeFrame: TimeFrame,
    ): StockChartScreenState
}