package ru.aklem.aktimer.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.InternalCoroutinesApi
import ru.aklem.aktimer.R
import ru.aklem.aktimer.viewmodel.ChartViewModel

@InternalCoroutinesApi
@ExperimentalAnimationApi
@Composable
fun TimerScreen(
    chartViewModel: ChartViewModel,
    onStartPause: (List<Int>) -> Unit,
    onStop: () -> Unit,
    timerValue: Int,
    isRunning: Boolean
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        TimerText(
            onStartPause = onStartPause,
            onStop = onStop,
            startValue = getChartTimes(chartViewModel),
            timerValue = timerValue,
            isRunning = isRunning
        )
    }
}

@Composable
fun TimerText(
    onStartPause: (List<Int>) -> Unit,
    onStop: () -> Unit,
    startValue: List<Int>,
    timerValue: Int,
    isRunning: Boolean
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
            val rotation = animateFloatAsState(
                targetValue = if (isRunning) 180f else 0f,
                animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
            )
            Card(
                modifier = Modifier
                    .size(64.dp)
                    .padding(4.dp)
                    .graphicsLayer { rotationY = rotation.value }
                    .clickable(onClick = { onStartPause(startValue) }),
                shape = CircleShape,
                elevation = 4.dp,
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Image(
                    painter = painterResource(
                        id = if (rotation.value <= 90) R.drawable.ic_play else R.drawable.ic_pause
                    ),
                    contentDescription = "Play Button"
                )
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Card(
                modifier = Modifier
                    .size(48.dp)
                    .padding(4.dp)
                    .align(alignment = Alignment.CenterVertically)
                    .clickable(onClick = { onStop() }),
                shape = CircleShape,
                elevation = 4.dp,
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_stop),
                    contentDescription = "Stop Button"
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

fun formattedNumber(number: Int): String {
    return number.toString().padStart(2, '0')
}

@InternalCoroutinesApi
fun getChartTimes(chartViewModel: ChartViewModel): List<Int> {
    val chart = chartViewModel.selectedChart
    val timeValues = mutableListOf<Int>()
    if (chart != null) {
        timeValues.add(chart.preparationTime)
        for (i in 0..(chart.repeat)) {
            timeValues.add(chart.actionTime)
            timeValues.add(chart.restTime)
        }
    }
    return timeValues
}