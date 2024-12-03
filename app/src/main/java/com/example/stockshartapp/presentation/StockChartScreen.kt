package com.example.stockshartapp.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun StockChartScreen(
    modifier: Modifier = Modifier,
) {
    val viewModel: StockChartViewModel = viewModel()
    val screenState = viewModel.screenState.collectAsState()
    when (val currentState = screenState.value) {
        is StockChartScreenState.Content -> {
            StockChart(
                modifier = modifier,
                bars = currentState.barList,
                currentFrame = currentState.timeFrame,
                onSelectTimeFrame = { timeFrame ->
                    viewModel.handlerEvent(
                        StockChartEvent.SelectFrame(timeFrame)
                    )
                }
            )
        }

        is StockChartScreenState.Initial -> { /* no on */ }

        is StockChartScreenState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}