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
import ru.aklem.aktimer.viewmodel.TimerViewModel

class MainActivity : ComponentActivity() {
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AkTimerTheme {
                val viewModel by viewModels<TimerViewModel>()
                val timerValue = viewModel.timerValue.collectAsState().value
                val isRunning = viewModel.isRunning.collectAsState().value
                val startValue = listOf(5, 3)
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainScreen(
                        onStartPause = viewModel::toggleStartPause,
                        onStop = viewModel::stop,
                        startValue = startValue,
                        timerValue = timerValue,
                        isRunning = isRunning
                    )
                }
            }
        }
    }
}