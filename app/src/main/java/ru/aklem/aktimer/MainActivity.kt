package ru.aklem.aktimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import ru.aklem.aktimer.ui.theme.AkTimerTheme
import ru.aklem.aktimer.viewmodel.ChartViewModel
import ru.aklem.aktimer.viewmodel.TimerViewModel

@AndroidEntryPoint
@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {

    @ExperimentalFoundationApi
    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AkTimerTheme {
                val timerViewModel by viewModels<TimerViewModel>()
                val chartViewModel by viewModels<ChartViewModel>()
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainScreen(
                        timerViewModel = timerViewModel,
                        chartViewModel = chartViewModel
                    )
                }
            }
        }
    }
}