package ru.aklem.aktimer.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import kotlinx.coroutines.flow.StateFlow
import ru.aklem.aktimer.R
import ru.aklem.aktimer.misc.AppSettings
import ru.aklem.aktimer.misc.ChartPeriods
import ru.aklem.aktimer.misc.ChartPeriods.*
import ru.aklem.aktimer.ui.theme.setsBackground
import ru.aklem.aktimer.viewmodel.SettingsViewModel

@ExperimentalAnimationApi
@Composable
fun CreateScreen(
    navController: NavController,
    tag: String?,
    title: String,
    onTitleChange: (String) -> Unit,
    onHeaderChange: (ChartPeriods, String) -> Unit,
    header: (ChartPeriods) -> StateFlow<String>,
    onTimeChange: (ChartPeriods, Int, Int) -> Unit,
    time: (ChartPeriods) -> StateFlow<Int>,
    onSetPlaySound: (ChartPeriods) -> Unit,
    playSound: (ChartPeriods) -> StateFlow<Boolean>,
    sets: Int,
    onRepeatChange: (Int) -> Unit,
    updateChart: () -> Unit,
    createChart: () -> Unit,
    settingsViewModel: SettingsViewModel
) {
    val preparationHeader by header(PREPARATION).collectAsState()
    val actionHeader by header(ACTION).collectAsState()
    val restHeader by header(REST).collectAsState()
    val preparationTime by time(PREPARATION).collectAsState()
    val actionTime by time(ACTION).collectAsState()
    val restTime by time(REST).collectAsState()
    val playPreparationSound by playSound(PREPARATION).collectAsState()
    val playActionSound by playSound(ACTION).collectAsState()
    val playRestSound by playSound(REST).collectAsState()
    val settings = settingsViewModel.appSettings.collectAsState(initial = AppSettings())
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = CenterHorizontally
    ) {
        Button(
            onClick = {
                when (checkInput(title, actionTime)) {
                    1 -> {
                        if (tag == "edit") updateChart() else createChart()
                        navController.navigate("saved")
                    }
                    2 -> {
                        Toast.makeText(
                            navController.context,
                            "Please enter a title",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    3 -> {
                        Toast.makeText(
                            navController.context,
                            "Please enter a time for action",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            },
            modifier = Modifier
                .padding(end = 8.dp)
                .align(End)
        ) {
            Text(text = if (tag == "edit") "Apply changes" else "Create Timer")
        }
        OutlinedTextField(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .fillMaxWidth()
                .align(CenterHorizontally),
            placeholder = { Text(text = "Please enter a title") },
            value = title,
            onValueChange = { if (it.length < 40) onTitleChange(it) },
            singleLine = true
        )
        if (settings.value.showPreparation) {
            PrepareCard(
                preparationHeader,
                onHeaderChange,
                preparationTime,
                onTimeChange,
                onSetPlaySound,
                playPreparationSound
            )
        }
        ActionCard(
            actionHeader,
            onHeaderChange,
            actionTime,
            onTimeChange,
            onSetPlaySound,
            playActionSound
        )
        if (settings.value.showRest) {
            RestCard(
                restHeader,
                onHeaderChange,
                restTime,
                onTimeChange,
                onSetPlaySound,
                playRestSound
            )
        }
        RepeatCard(sets, onRepeatChange)
    }
}

fun checkInput(title: String, actionTime: Int): Int {
    var result = 1
    if (title.isBlank()) {
        result = 2

    } else if (actionTime <= 0) result = 3
    return result
}

@ExperimentalAnimationApi
@Composable
fun PrepareCard(
    headerPrepare: String,
    onHeaderChange: (ChartPeriods, String) -> Unit,
    prepareTime: Int,
    onTimeChange: (ChartPeriods, Int, Int) -> Unit,
    onSetPlaySound: (ChartPeriods) -> Unit,
    playPreparationSound: Boolean
) {
    CardTemplate(
        period = PREPARATION,
        header = headerPrepare,
        onHeaderChange = onHeaderChange,
        time = prepareTime,
        onTimeChange = onTimeChange,
        onSetPlaySound = onSetPlaySound,
        playSound = playPreparationSound,
    )
}

@ExperimentalAnimationApi
@Composable
fun ActionCard(
    headerAction: String,
    onHeaderChange: (ChartPeriods, String) -> Unit,
    actionTime: Int,
    onTimeChange: (ChartPeriods, Int, Int) -> Unit,
    onSetPlaySound: (ChartPeriods) -> Unit,
    playActionSound: Boolean
) {
    val topShape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
    CardTemplate(
        period = ACTION,
        header = headerAction,
        onHeaderChange = onHeaderChange,
        time = actionTime,
        onTimeChange = onTimeChange,
        onSetPlaySound = onSetPlaySound,
        playSound = playActionSound,
        cornersShape = topShape,
        cardBackground = setsBackground
    )
}

@ExperimentalAnimationApi
@Composable
fun RestCard(
    headerRest: String,
    onHeaderChange: (ChartPeriods, String) -> Unit,
    restTime: Int,
    onTimeChange: (ChartPeriods, Int, Int) -> Unit,
    onSetPlaySound: (ChartPeriods) -> Unit,
    playRestSound: Boolean
) {
    val middleShape = RoundedCornerShape(0.dp)
    CardTemplate(
        period = REST,
        header = headerRest,
        onHeaderChange = onHeaderChange,
        time = restTime,
        onTimeChange = onTimeChange,
        onSetPlaySound = onSetPlaySound,
        playSound = playRestSound,
        cornersShape = middleShape,
        cardBackground = setsBackground
    )
}

@ExperimentalAnimationApi
@Composable
fun RepeatCard(sets: Int, onSetsChange: (Int) -> Unit) {
    val range = (1..99).toList()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp),
        backgroundColor = setsBackground
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Selector(range = range, value = sets, onValueChange = onSetsChange)
            Spacer(modifier = Modifier.width(8.dp))
            Crossfade(targetState = sets) { sets ->
                if (sets > 1) Text("sets") else Text("set")
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun CardTemplate(
    period: ChartPeriods,
    header: String,
    onHeaderChange: (ChartPeriods, String) -> Unit,
    time: Int,
    onTimeChange: (ChartPeriods, Int, Int) -> Unit,
    onSetPlaySound: (ChartPeriods) -> Unit,
    playSound: Boolean,
    cornersShape: RoundedCornerShape = RoundedCornerShape(8.dp),
    cardBackground: Color = MaterialTheme.colors.background
) {
    val minutes = time / 60
    val seconds = time % 60
    val range = (0..59).toList()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = cornersShape,
        backgroundColor = cardBackground
    ) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(end = 8.dp),
                placeholder = { Text(text = "Please enter a header") },
                value = header,
                onValueChange = { if (it.length < 40) onHeaderChange(period, it) },
                singleLine = true
            )
            Selector(range = range, value = minutes, onValueChange = {
                onTimeChange(period, it, seconds)
            })
            Text(text = " : ")
            Selector(range = range, value = seconds, onValueChange = {
                onTimeChange(period, minutes, it)
            })
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                modifier = Modifier
                    .weight(0.1f)
                    .clickable(onClick = {
                        onSetPlaySound(period)
                    }),
                painter = painterResource(
                    id = if (playSound) R.drawable.ic_sound_on else R.drawable.ic_sound_off
                ),
                contentScale = ContentScale.None,
                colorFilter = ColorFilter.tint(color = Color.Black),
                contentDescription = "Play sound when time expires"
            )
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun Selector(range: List<Int>, value: Int, onValueChange: (Int) -> Unit) {
    val showPopup = remember { mutableStateOf(false) }
    val doubleDigit = range.size == 60
    Box {
        Text(
            text = if (doubleDigit) formattedNumber(value) else value.toString(),
            fontSize = 24.sp,
            modifier = Modifier.clickable { showPopup.value = true },
        )
        Popup(alignment = Alignment.Center, onDismissRequest = { showPopup.value = false }) {
            AnimatedVisibility(
                visible = showPopup.value,
                enter = scaleIn(tween(100)),
                exit = scaleOut(tween(100))
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 60.dp, height = 180.dp)
                        .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = CenterHorizontally,
                        state = rememberLazyListState(
                            initialFirstVisibleItemIndex = if (value > 0) value - 1 else value
                        )
                    ) {
                        items(items = range) { item ->
                            Text(
                                text = if (doubleDigit) formattedNumber(item) else item.toString(),
                                fontSize = 26.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        onValueChange(item)
                                        showPopup.value = false
                                    }
                            )
                            if (item != 99) {
                                Divider(modifier = Modifier.fillMaxWidth(0.6f), thickness = 1.dp)
                            }
                        }
                    }
                }
            }
        }
    }
}