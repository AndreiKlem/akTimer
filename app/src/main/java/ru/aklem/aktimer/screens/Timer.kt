package ru.aklem.aktimer.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.aklem.aktimer.R
import ru.aklem.aktimer.ui.theme.AkTimerTheme
import ru.aklem.aktimer.viewmodel.TimerViewModel

@ExperimentalAnimationApi
@Composable
fun TimerScreen(
    onStart: (List<Int>) -> Unit,
    onPause: () -> Unit,
    onStop: () -> Unit,
    startValue: List<Int>,
    timerValue: Int,
    isRunning: Boolean
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        TimerText(
            onStart = onStart,
            onPause = onPause,
            onStop = onStop,
            startValue = startValue,
            timerValue = timerValue,
            running = isRunning
        )
    }
}

@ExperimentalAnimationApi
@Composable
@Preview
fun HomeScreenPreview() {
    AkTimerTheme {
        TimerScreen(
            onStart = TimerViewModel()::start,
            onPause = TimerViewModel()::pause,
            onStop = TimerViewModel()::stop,
            startValue = listOf(10, 20),
            timerValue = 100,
            isRunning = false
        )
    }
}

@ExperimentalAnimationApi
@Composable
fun TimerText(
    onStart: (List<Int>) -> Unit,
    onPause: () -> Unit,
    onStop: () -> Unit,
    startValue: List<Int>,
    timerValue: Int,
    running: Boolean
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = getTimerText(timerValue),
            style = MaterialTheme.typography.h2,
        )
        Row {
            AnimatedVisibility(visible = !running, enter = fadeIn(), exit = fadeOut()) {
                Card(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(4.dp)
                        .clickable(onClick = { onStart(startValue) }),
                    shape = CircleShape,
                    elevation = 4.dp,
                    backgroundColor = MaterialTheme.colors.primary
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_play),
                        contentDescription = "Play Button"
                    )
                }
            }
            AnimatedVisibility(visible = running, enter = fadeIn(), exit = fadeOut()) {
                Card(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(4.dp)
                        .clickable(onClick = { onPause() }),
                    shape = CircleShape,
                    elevation = 4.dp,
                    backgroundColor = MaterialTheme.colors.primary
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_pause),
                        contentDescription = "Pause Button"
                    )
                }
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Card(
                modifier = Modifier
                    .size(48.dp)
                    .padding(4.dp)
                    .clickable(onClick = { onStop() }),
                shape = CircleShape,
                elevation = 4.dp,
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_stop),
                    contentDescription = "Pause Button"
                )
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