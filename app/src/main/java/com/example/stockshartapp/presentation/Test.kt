package com.example.stockshartapp.presentation

import android.os.Parcelable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.parcelize.Parcelize

@Composable
fun Test() {
    var number by rememberSaveable(
        saver = TestData.Saver
    ) {
        mutableStateOf(TestData(0, ""))
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { number = number.copy(number = number.number + 1) },
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "Text $number")
    }
}

data class TestData(val number: Int, val text: String) {
    companion object {
        val Saver: Saver<MutableState<TestData>, Any> = listSaver(
            save = {
                val testData = it.value
                listOf(testData.number, testData.text)
            },
            restore = {
                val testData = TestData(
                    number = it[0] as Int,
                    text = it[1] as String,
                )
                mutableStateOf(testData)
            }
        )
    }
}