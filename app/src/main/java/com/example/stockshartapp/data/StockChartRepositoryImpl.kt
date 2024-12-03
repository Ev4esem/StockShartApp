package com.example.stockshartapp.data

import com.example.stockshartapp.domain.StockChartRepository
import com.example.stockshartapp.presentation.TimeFrame
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class StockChartRepositoryImpl(
   private val apiService: ApiService
): StockChartRepository {
    override fun loadBars(timeFrame: TimeFrame): Flow<List<Bar>> = flow {
        emit(apiService.loadBars(timeFrame.toTimeFrameStr()).barList)
    }
}

