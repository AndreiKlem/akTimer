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
import androidx.compose.runtime.*
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
import ru.aklem.aktimer.ui.theme.setsBackground
import kotlin.reflect.KFunction1

@ExperimentalAnimationApi
@Composable
fun CreateScreen(
    navController: NavController,
    title: String,
    onTitleChange: (String) -> Unit,
    headerPrepare: String,
    onHeaderPrepareChange: (String) -> Unit,
    prepareTime: Int,
    onPrepareTimeChange: (Int, Int) -> Unit,
    headerAction: String,
    onHeaderActionChange: (String) -> Unit,
    actionTime: Int,
    onActionTimeChange: (Int, Int) -> Unit,
    headerRest: String,
    onHeaderRestChange: (String) -> Unit,
    restTime: Int,
    onRestTimeChange: (Int, Int) -> Unit,
    onPlaySoundChange: (ChartPeriods) -> Unit,
    playSound: KFunction1<ChartPeriods, StateFlow<Boolean>>,
    sets: Int,
    onSetsAmountChange: (String) -> Unit,
    createChart: () -> Unit
) {
    val playPreparationSound = playSound(ChartPeriods.PREPARATION).collectAsState().value
    val playActionSound = playSound(ChartPeriods.ACTION).collectAsState().value
    val playRestSound = playSound(ChartPeriods.REST).collectAsState().value
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
            onValueChange = { if (it.length < 40) onTitleChange(it) },
            singleLine = true
        )
        PrepareCard(
            headerPrepare,
            onHeaderPrepareChange,
            prepareTime,
            onPrepareTimeChange,
            onPlaySoundChange,
            playPreparationSound
        )
        ActionCard(
            headerAction,
            onHeaderActionChange,
            actionTime,
            onActionTimeChange,
            onPlaySoundChange,
            playActionSound
        )
        RestCard(
            headerRest,
            onHeaderRestChange,
            restTime,
            onRestTimeChange,
            onPlaySoundChange,
            playRestSound
        )
        RepeatCard(sets, onSetsAmountChange)
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
    onHeaderPrepareChange: (String) -> Unit,
    prepareTime: Int,
    onPrepareTimeChange: (Int, Int) -> Unit,
    onPlaySoundChange: (ChartPeriods) -> Unit,
    playPreparationSound: Boolean
) {
    CardTemplate(
        period = ChartPeriods.PREPARATION,
        header = headerPrepare,
        onHeaderChange = onHeaderPrepareChange,
        time = prepareTime,
        onTimeChange = onPrepareTimeChange,
        onPlaySoundChange = onPlaySoundChange,
        playSound = playPreparationSound,
    )
}

@Composable
fun ActionCard(
    headerAction: String,
    onHeaderActionChange: (String) -> Unit,
    actionTime: Int,
    onActionTimeChange: (Int, Int) -> Unit,
    onPlaySoundChange: (ChartPeriods) -> Unit,
    playActionSound: Boolean
) {
    val topShape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
    CardTemplate(
        period = ChartPeriods.ACTION,
        header = headerAction,
        onHeaderChange = onHeaderActionChange,
        time = actionTime,
        onTimeChange = onActionTimeChange,
        onPlaySoundChange = onPlaySoundChange,
        playSound = playActionSound,
        cornersShape = topShape,
        cardBackground = setsBackground
    )
}

@Composable
fun RestCard(
    headerRest: String,
    onHeaderRestChange: (String) -> Unit,
    restTime: Int,
    onRestTimeChange: (Int, Int) -> Unit,
    onPlaySoundChange: (ChartPeriods) -> Unit,
    playRestSound: Boolean
) {
    val middleShape = RoundedCornerShape(0.dp)
    CardTemplate(
        period = ChartPeriods.REST,
        header = headerRest,
        onHeaderChange = onHeaderRestChange,
        time = restTime,
        onTimeChange = onRestTimeChange,
        onPlaySoundChange = onPlaySoundChange,
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
    onHeaderChange: (String) -> Unit,
    time: Int,
    onTimeChange: (Int, Int) -> Unit,
    onPlaySoundChange: (ChartPeriods) -> Unit,
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
                onValueChange = { if (it.length < 40) onHeaderChange(it) },
                singleLine = true
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                TimeInput(
                    label = "Minutes",
                    modifier = Modifier.weight(0.45f),
                    time = minutes,
                    onTimeValueChange = { onTimeChange(it.toInt(), seconds) }
                )
                Spacer(modifier = Modifier.padding(4.dp))
                TimeInput(
                    label = "Seconds",
                    modifier = Modifier.weight(0.45f),
                    time = seconds,
                    onTimeValueChange = { onTimeChange(minutes, it.toInt()) }
                )
                Image(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .weight(0.1f)
                        .clickable(onClick = {
                            onPlaySoundChange(period)
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
fun TimeInput(label: String, modifier: Modifier, time: Int, onTimeValueChange: (String) -> Unit) {
    OutlinedTextField(
        modifier = modifier,
        label = { Text(text = label) },
        placeholder = { Text(text = "max 59") },
        value = if (time == 0) "" else time.toString(),
        onValueChange = {
            onTimeValueChange(
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