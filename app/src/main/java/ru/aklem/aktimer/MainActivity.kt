package ru.aklem.aktimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import ru.aklem.aktimer.ui.theme.AkTimerTheme
import ru.aklem.aktimer.viewmodel.ChartViewModel
import ru.aklem.aktimer.viewmodel.TimerViewModel

class MainActivity : ComponentActivity() {
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AkTimerTheme {
                val timerViewModel by viewModels<TimerViewModel>()
                val chartViewModel by viewModels<ChartViewModel>()
                val timerValue = timerViewModel.timerValue.collectAsState().value
                val isRunning = timerViewModel.isRunning.collectAsState().value
                val startValue = listOf(5, 3)
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainScreen(
                        onStartPause = timerViewModel::toggleStartPause,
                        onStop = timerViewModel::stop,
                        chartViewModel = chartViewModel,
                        startValue = startValue,
                        timerValue = timerValue,
                        isRunning = isRunning
                    )
                }
            }
        }
    }
}