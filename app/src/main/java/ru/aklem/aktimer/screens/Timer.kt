package ru.aklem.aktimer.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.InternalCoroutinesApi
import ru.aklem.aktimer.R
import ru.aklem.aktimer.utils.AppSettings
import ru.aklem.aktimer.utils.Period
import ru.aklem.aktimer.viewmodel.ChartViewModel
import ru.aklem.aktimer.viewmodel.SettingsViewModel
import ru.aklem.aktimer.viewmodel.TimerViewModel

@InternalCoroutinesApi
@ExperimentalAnimationApi
@Composable
fun TimerScreen(
    settingsViewModel: SettingsViewModel,
    timerViewModel: TimerViewModel,
    chartViewModel: ChartViewModel
) {
    val timerValue = timerViewModel.timerValue.collectAsState().value
    val isRunning = timerViewModel.isRunning.collectAsState().value
    val currentPeriod = timerViewModel.currentPeriod.collectAsState().value
    val progressBarTime = timerViewModel.progressTime.collectAsState().value
    val progressStartTime = timerViewModel.progressStartTime
    val title = chartViewModel.selectedChart?.title
    val settings = settingsViewModel.appSettings.collectAsState(initial = AppSettings())
    var timerSelected by remember { mutableStateOf(false) }

    LaunchedEffect(settings) {
        timerViewModel.onRingtoneSet(settings.value.userSound)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(contentAlignment = Alignment.Center) {
            Timer(
                onStartPause = timerViewModel::toggleStartPause,
                onStop = timerViewModel::stop,
                timerValue = timerValue,
                isRunning = isRunning,
                timerSelected = timerSelected
            )
        }
        Column(
            modifier = Modifier
                .padding(top = 32.dp)
                .fillMaxWidth(),
            horizontalAlignment = CenterHorizontally
        ) {
            title?.let {
                timerSelected = true
                if (settings.value.showTitle) {
                    Text(
                        text = it,
                        modifier = Modifier.padding(bottom = 8.dp),
                        style = TextStyle(fontWeight = FontWeight.Thin, fontSize = 26.sp)
                    )
                }
                Spacer(Modifier.height(16.dp))
                if (settings.value.showPeriods) {
                    CurrentPeriodInfo(period = currentPeriod, isRunning = isRunning)
                }
                Spacer(Modifier.height(16.dp))
                if (settings.value.showProgressBar) {
                    TotalProgressBar(progressBarTime, progressStartTime, isRunning)
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun CurrentPeriodInfo(period: Period?, isRunning: Boolean) {
    period?.let { p ->
        AnimatedContent(targetState = p, transitionSpec = {
            if (isRunning) {
                slideInVertically(initialOffsetY = { height -> height }) + fadeIn() with
                        slideOutVertically(targetOffsetY = { height -> -height }) + fadeOut()
            } else {
                slideInVertically(initialOffsetY = { height -> -height }) + fadeIn() with
                        slideOutVertically(targetOffsetY = { height -> height }) + fadeOut()
            }.using(SizeTransform(clip = false))
        }) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(text = "${it.name}: ${getHeaderTime(it.time)}")
            }
        }
    }
}

@Composable
fun Timer(
    onStartPause: () -> Unit,
    onStop: () -> Unit,
    timerValue: Int,
    isRunning: Boolean,
    timerSelected: Boolean
) {
    Column(
        modifier = Modifier.fillMaxSize().alpha(if (timerSelected) 1f else 0.38f),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = getTimerText(timerValue),
            style = MaterialTheme.typography.h2,
        )
        Row {
            val rotation = animateFloatAsState(
                targetValue = if (isRunning) 180f else 0f,
                animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
            )
            Card(
                modifier = Modifier
                    .size(64.dp)
                    .padding(4.dp)
                    .graphicsLayer { rotationY = rotation.value }
                    .clickable(onClick = { onStartPause() }),
                shape = CircleShape,
                elevation = 4.dp,
                backgroundColor = MaterialTheme.colors.secondary
            ) {
                Image(
                    painter = painterResource(
                        id = if (rotation.value < 90) R.drawable.ic_play else R.drawable.ic_pause
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
                backgroundColor = MaterialTheme.colors.secondary
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_stop),
                    contentDescription = "Stop Button"
                )
            }
        }
    }
}

@Composable
fun TotalProgressBar(time: Int, start: Int, isRunning: Boolean) {
    val fraction by animateFloatAsState(
        targetValue = time.toFloat() / start,
        animationSpec = if (isRunning) tween(
            durationMillis = 1000, easing = LinearEasing
        ) else tween(durationMillis = 400)
    )
    Box(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(8.dp)
            .background(color = Color.LightGray, shape = RoundedCornerShape(8.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(fraction = fraction)
                .background(
                    color = MaterialTheme.colors.primaryVariant,
                    shape = RoundedCornerShape(8.dp)
                )
        )
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

fun getHeaderTime(duration: Int): String {
    val min = duration / 60
    val sec = duration % 60
    return "${if (min > 0) "$min min " else ""}${if (sec > 0) "$sec sec" else ""}"
}