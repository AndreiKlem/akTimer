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
import ru.aklem.aktimer.misc.Period
import ru.aklem.aktimer.viewmodel.ChartViewModel

@InternalCoroutinesApi
@ExperimentalAnimationApi
@Composable
fun TimerScreen(
    chartViewModel: ChartViewModel,
    onStartPause: (List<Period>) -> Unit,
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
            periods = getPeriods(chartViewModel),
            timerValue = timerValue,
            isRunning = isRunning
        )
    }
}

@Composable
fun TimerText(
    onStartPause: (List<Period>) -> Unit,
    onStop: () -> Unit,
    periods: List<Period>,
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
                    .clickable(onClick = { onStartPause(periods) }),
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

fun getTimerText(duration: Int): String {
    val min = duration / 60
    val sec = duration % 60
    return "${formattedNumber(min)} : ${formattedNumber(sec)}"
}

fun formattedNumber(number: Int): String {
    return number.toString().padStart(2, '0')
}

@InternalCoroutinesApi
fun getPeriods(chartViewModel: ChartViewModel): List<Period> {
    val chart = chartViewModel.selectedChart
    val periods = mutableListOf<Period>()
    chart?.let {
        if (it.preparationTime > 0) {
            periods.add(Period(name = it.headerPreparation, time = it.preparationTime))
        }
        for (i in 0..(it.repeat)) {
            periods.add(Period(name = it.headerAction, time = it.actionTime))
            if (it.restTime > 0) periods.add(Period(name = it.headerRest, time = it.restTime))
        }
    }
    return periods
}