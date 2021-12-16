package ru.aklem.aktimer.screens

import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.flow.StateFlow
import ru.aklem.aktimer.R
import ru.aklem.aktimer.misc.ChartPeriods
import ru.aklem.aktimer.misc.ChartPeriods.*
import ru.aklem.aktimer.ui.theme.setsBackground

@ExperimentalAnimationApi
@Composable
fun CreateScreen(
    navController: NavController,
    title: String,
    onSetTitle: (String) -> Unit,
    onSetHeader: (ChartPeriods, String) -> Unit,
    header: (ChartPeriods) -> StateFlow<String>,
    onSetTime: (ChartPeriods, Int, Int) -> Unit,
    time: (ChartPeriods) -> StateFlow<Int>,
    onSetPlaySound: (ChartPeriods) -> Unit,
    playSound: (ChartPeriods) -> StateFlow<Boolean>,
    sets: Int,
    onSetRepeat: (String) -> Unit,
    createChart: () -> Unit
) {
    val preparationHeader = header(PREPARATION).collectAsState().value
    val actionHeader = header(ACTION).collectAsState().value
    val restHeader = header(REST).collectAsState().value
    val preparationTime = time(PREPARATION).collectAsState().value
    val actionTime = time(ACTION).collectAsState().value
    val restTime = time(REST).collectAsState().value
    val playPreparationSound = playSound(PREPARATION).collectAsState().value
    val playActionSound = playSound(ACTION).collectAsState().value
    val playRestSound = playSound(REST).collectAsState().value
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .verticalScroll(state = rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                when (checkInput(title, actionTime)) {
                    1 -> {
                        createChart()
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
                .align(Alignment.End)
        ) {
            Text(text = "Create Timer")
        }
        OutlinedTextField(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            placeholder = { Text(text = "Please enter a title") },
            value = title,
            onValueChange = { if (it.length < 40) onSetTitle(it) },
            singleLine = true
        )
        PrepareCard(
            preparationHeader,
            onSetHeader,
            preparationTime,
            onSetTime,
            onSetPlaySound,
            playPreparationSound
        )
        ActionCard(
            actionHeader,
            onSetHeader,
            actionTime,
            onSetTime,
            onSetPlaySound,
            playActionSound
        )
        RestCard(
            restHeader,
            onSetHeader,
            restTime,
            onSetTime,
            onSetPlaySound,
            playRestSound
        )
        RepeatCard(sets, onSetRepeat)
    }
}

fun checkInput(title: String, actionTime: Int): Int {
    var result = 1
    if (title.isBlank()) {
        result = 2

    } else if (actionTime <= 0) result = 3
    return result
}

@Composable
fun PrepareCard(
    headerPrepare: String,
    onSetHeader: (ChartPeriods, String) -> Unit,
    prepareTime: Int,
    onSetTime: (ChartPeriods, Int, Int) -> Unit,
    onSetPlaySound: (ChartPeriods) -> Unit,
    playPreparationSound: Boolean
) {
    CardTemplate(
        period = PREPARATION,
        header = headerPrepare,
        onSetHeader = onSetHeader,
        time = prepareTime,
        onSetTime = onSetTime,
        onSetPlaySound = onSetPlaySound,
        playSound = playPreparationSound,
    )
}

@Composable
fun ActionCard(
    headerAction: String,
    onSetHeader: (ChartPeriods, String) -> Unit,
    actionTime: Int,
    onSetTime: (ChartPeriods, Int, Int) -> Unit,
    onSetPlaySound: (ChartPeriods) -> Unit,
    playActionSound: Boolean
) {
    val topShape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
    CardTemplate(
        period = ACTION,
        header = headerAction,
        onSetHeader = onSetHeader,
        time = actionTime,
        onSetTime = onSetTime,
        onSetPlaySound = onSetPlaySound,
        playSound = playActionSound,
        cornersShape = topShape,
        cardBackground = setsBackground
    )
}

@Composable
fun RestCard(
    headerRest: String,
    onSetHeader: (ChartPeriods, String) -> Unit,
    restTime: Int,
    onSetTime: (ChartPeriods, Int, Int) -> Unit,
    onSetPlaySound: (ChartPeriods) -> Unit,
    playRestSound: Boolean
) {
    val middleShape = RoundedCornerShape(0.dp)
    CardTemplate(
        period = REST,
        header = headerRest,
        onSetHeader = onSetHeader,
        time = restTime,
        onSetTime = onSetTime,
        onSetPlaySound = onSetPlaySound,
        playSound = playRestSound,
        cornersShape = middleShape,
        cardBackground = setsBackground
    )
}

@Composable
fun RepeatCard(sets: Int, onSetsAmountChange: (String) -> Unit) {
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
            Text(text = "Repeat")
            OutlinedTextField(
                modifier = Modifier
                    .padding(start = 4.dp),
                label = { Text(text = if (sets == 1) "time" else "times") },
                placeholder = { Text(text = "max 99") },
                value = if (sets == 0) "" else sets.toString(),
                onValueChange = {
                    onSetsAmountChange(
                        when {
                            it.toIntOrNull() == null -> "0"
                            it.toInt() in 1..99 -> it
                            it.toInt() > 99 -> "99"
                            else -> "0"
                        }
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }
}

@Composable
fun CardTemplate(
    period: ChartPeriods,
    header: String,
    onSetHeader: (ChartPeriods, String) -> Unit,
    time: Int,
    onSetTime: (ChartPeriods, Int, Int) -> Unit,
    onSetPlaySound: (ChartPeriods) -> Unit,
    playSound: Boolean,
    cornersShape: RoundedCornerShape = RoundedCornerShape(8.dp),
    cardBackground: Color = MaterialTheme.colors.background
) {
    val minutes = time / 60
    val seconds = time % 60
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = cornersShape,
        backgroundColor = cardBackground
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = "Please enter a header") },
                value = header,
                onValueChange = { if (it.length < 40) onSetHeader(period, it) },
                singleLine = true
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                TimeInput(
                    label = "Minutes",
                    modifier = Modifier.weight(0.45f),
                    time = minutes,
                    onSetTime = { onSetTime(period, it.toInt(), seconds) }
                )
                Spacer(modifier = Modifier.padding(4.dp))
                TimeInput(
                    label = "Seconds",
                    modifier = Modifier.weight(0.45f),
                    time = seconds,
                    onSetTime = { onSetTime(period, minutes, it.toInt()) }
                )
                Image(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .weight(0.1f)
                        .clickable(onClick = {
                            onSetPlaySound(period)
                        }),
                    painter = painterResource(
                        id = if (playSound) R.drawable.ic_sound_on else R.drawable.ic_sound_off
                    ),
                    colorFilter = ColorFilter.tint(color = Color.Black),
                    contentDescription = "Play sound when time expires"
                )
            }
        }
    }
}

@Composable
fun TimeInput(label: String, modifier: Modifier, time: Int, onSetTime: (String) -> Unit) {
    OutlinedTextField(
        modifier = modifier,
        label = { Text(text = label) },
        placeholder = { Text(text = "max 59") },
        value = if (time == 0) "" else time.toString(),
        onValueChange = {
            onSetTime(
                when {
                    it.toIntOrNull() == null -> "0"
                    it.toInt() in 1..59 -> it
                    it.toInt() > 59 -> "59"
                    else -> "0"
                }
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

