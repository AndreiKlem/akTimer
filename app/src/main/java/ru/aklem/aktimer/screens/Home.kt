package ru.aklem.aktimer.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.aklem.aktimer.ui.theme.AkTimerTheme
import ru.aklem.aktimer.viewmodel.TimerViewModel

@ExperimentalAnimationApi
@Composable
fun HomeScreen(
    viewModel: TimerViewModel,
    timerValue: Int,
    isRunning: Boolean
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        TimerText(viewModel, timerValue, isRunning)
    }
}

@ExperimentalAnimationApi
@Composable
@Preview
fun HomeScreenPreview() {
    AkTimerTheme {
        HomeScreen(viewModel = TimerViewModel(), timerValue = 100, isRunning = false)
    }
}

@ExperimentalAnimationApi
@Composable
fun TimerText(viewModel: TimerViewModel, duration: Int, running: Boolean) {
    val initialTimerDuration = 63
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = getTimerText(duration),
            style = MaterialTheme.typography.h1,
        )
        Row {
            AnimatedVisibility(visible = !running) {
                Button(onClick = { viewModel.start(initialTimerDuration) }) {
                    Text(text = "Start")
                }
            }
            AnimatedVisibility(visible = running) {
                Button(onClick = { viewModel.pause() }) {
                    Text(text = "Pause")
                }
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Button(onClick = { viewModel.stop() }) {
                Text(text = "Stop")
            }
        }
    }
}

private fun getTimerText(duration: Int): String {
    val min = duration / 60
    val sec = duration % 60
    return "${formattedNumber(min)} : ${formattedNumber(sec)}"
}

private fun formattedNumber(number: Int): String {
    return number.toString().padStart(2, '0')
}