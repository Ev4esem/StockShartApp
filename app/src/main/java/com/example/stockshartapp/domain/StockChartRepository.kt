package com.example.stockshartapp.domain

import com.example.stockshartapp.data.Bar
import com.example.stockshartapp.presentation.TimeFrame
import kotlinx.coroutines.flow.Flow

interface StockChartRepository {
    fun loadBars(timeFrame: TimeFrame): Flow<List<Bar>>
}