package com.example.stockshartapp.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockshartapp.data.ApiFactory
import com.example.stockshartapp.data.StockChartRepositoryImpl
import com.example.stockshartapp.domain.StockChartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class StockChartViewModel: ViewModel(), EventHandler<StockChartEvent> {

    private val apiService = ApiFactory.apiService
    private val TAG = this.javaClass.simpleName
    private val repository: StockChartRepository = StockChartRepositoryImpl(apiService)
    private val _screenState = MutableStateFlow<StockChartScreenState>(StockChartScreenState.Initial)
    val screenState: StateFlow<StockChartScreenState> = _screenState.asStateFlow()


    override fun handlerEvent(event: StockChartEvent) {
        when(event) {
            is StockChartEvent.SelectFrame -> loadBarList(event.frame)
        }
    }

    init {
        loadBarList()
    }

    private fun loadBarList(timeFrame: TimeFrame = TimeFrame.HOUR_1) {
        viewModelScope.launch {
            repository.loadBars(timeFrame)
                .onStart {
                    _screenState.value = StockChartScreenState.Loading
                }
                .catch {
                    Log.d(TAG, "Exception caught: $it")
                }
                .collect { barList ->
                    _screenState.value = StockChartScreenState.Content(
                        barList = barList,
                        timeFrame = timeFrame
                    )
                }
        }
    }

}

sealed interface StockChartEvent {
    data class SelectFrame(val frame: TimeFrame): StockChartEvent
}

interface EventHandler<T> {
    fun handlerEvent(event: T)
}