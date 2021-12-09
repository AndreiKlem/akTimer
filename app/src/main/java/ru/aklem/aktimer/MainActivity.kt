package ru.aklem.aktimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.InternalCoroutinesApi
import ru.aklem.aktimer.ui.theme.AkTimerTheme
import ru.aklem.aktimer.viewmodel.ChartViewModel
import ru.aklem.aktimer.viewmodel.TimerViewModel

@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {
    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AkTimerTheme {
                val timerViewModel by viewModels<TimerViewModel>()
                val chartViewModel by viewModels<ChartViewModel>()
                val timerValue = timerViewModel.timerValue.collectAsState().value
                val isRunning = timerViewModel.isRunning.collectAsState().value
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainScreen(
                        onStartPause = timerViewModel::toggleStartPause,
                        onStop = timerViewModel::stop,
                        timerViewModel = timerViewModel,
                        chartViewModel = chartViewModel,
                        timerValue = timerValue,
                        isRunning = isRunning
                    )
                }
            }
        }
    }
}