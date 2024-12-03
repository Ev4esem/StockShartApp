package com.example.stockshartapp.data

import com.example.stockshartapp.presentation.TimeFrame

fun TimeFrame.toTimeFrameStr(): String = when(this) {
    TimeFrame.MIN_5 -> "5/minute"
    TimeFrame.MIN_15 -> "15/minute"
    TimeFrame.MIN_30 -> "30/minute"
    TimeFrame.HOUR_1 -> "1/hour"
}
