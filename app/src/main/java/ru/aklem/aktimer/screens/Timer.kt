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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.InternalCoroutinesApi
import ru.aklem.aktimer.R
import ru.aklem.aktimer.misc.Period
import ru.aklem.aktimer.viewmodel.ChartViewModel
import ru.aklem.aktimer.viewmodel.TimerViewModel

@InternalCoroutinesApi
@ExperimentalAnimationApi
@Composable
fun TimerScreen(
    timerViewModel: TimerViewModel
) {
    val timerValue = timerViewModel.timerValue.collectAsState().value
    val isRunning = timerViewModel.isRunning.collectAsState().value
    val currentPeriod = timerViewModel.currentPeriod.collectAsState().value
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CurrentPeriodInfo(period = currentPeriod)
        Timer(
            onStartPause = timerViewModel::toggleStartPause,
            onStop = timerViewModel::stop,
            timerValue = timerValue,
            isRunning = isRunning
        )
    }
}

@Composable
fun CurrentPeriodInfo(period: Period?) {
    period?.name?.let { Text(text = it) }
}

@Composable
fun Timer(
    onStartPause: () -> Unit,
    onStop: () -> Unit,
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
                    .clickable(onClick = { onStartPause() }),
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