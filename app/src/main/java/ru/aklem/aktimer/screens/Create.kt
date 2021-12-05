package ru.aklem.aktimer.screens

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ru.aklem.aktimer.R
import ru.aklem.aktimer.ui.theme.setsBackground

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
    sets: Int,
    onSetsAmountChange: (String) -> Unit,
    createChart: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally),
            placeholder = { Text(text = "Please enter a title") },
            value = title,
            onValueChange = { if (it.length < 40) onTitleChange(it) },
            textStyle = TextStyle(fontSize = 18.sp),
            singleLine = true
        )
        PrepareCard(headerPrepare, onHeaderPrepareChange, prepareTime, onPrepareTimeChange)
        ActionCard(headerAction, onHeaderActionChange, actionTime, onActionTimeChange)
        RestCard(headerRest, onHeaderRestChange, restTime, onRestTimeChange)
        RepeatCard(sets, onSetsAmountChange)
        Button(
            onClick = {
                if (isValidInput(actionTime)) {
                    createChart()
                    navController.navigate("saved")
                } else {
                    Log.d(TAG, "CreateScreen: invalid input")
                }
            },
            modifier = Modifier
                .align(Alignment.End)
                .padding(8.dp)
        ) {
            Text(text = "Create Timer")
        }
    }
}

fun isValidInput(time: Int): Boolean {
    var result = false
    if (time > 0) result = true
    return result
}

@Composable
fun PrepareCard(
    headerPrepare: String,
    onHeaderPrepareChange: (String) -> Unit,
    prepareTime: Int,
    onPrepareTimeChange: (Int, Int) -> Unit
) {
    CardTemplate(
        header = headerPrepare,
        onHeaderChange = onHeaderPrepareChange,
        time = prepareTime,
        onTimeChange = onPrepareTimeChange
    )
}

@Composable
fun ActionCard(
    headerAction: String,
    onHeaderActionChange: (String) -> Unit,
    actionTime: Int,
    onActionTimeChange: (Int, Int) -> Unit
) {
    val topShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
    CardTemplate(
        header = headerAction,
        onHeaderChange = onHeaderActionChange,
        time = actionTime,
        onTimeChange = onActionTimeChange,
        cornersShape = topShape,
        cardBackground = setsBackground
    )
}

@Composable
fun RestCard(
    headerRest: String,
    onHeaderRestChange: (String) -> Unit,
    restTime: Int,
    onRestTimeChange: (Int, Int) -> Unit
) {
    val middleShape = RoundedCornerShape(0.dp)
    CardTemplate(
        header = headerRest,
        onHeaderChange = onHeaderRestChange,
        time = restTime,
        onTimeChange = onRestTimeChange,
        cornersShape = middleShape,
        cardBackground = setsBackground
    )
}

@Composable
fun RepeatCard(sets: Int, onSetsAmountChange: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, top = 4.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp),
        backgroundColor = setsBackground
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Repeat", fontSize = 18.sp)
            OutlinedTextField(
                modifier = Modifier
                    .padding(start = 4.dp, end = 4.dp)
                    .fillMaxWidth(0.3f),
                label = { Text(text = if (sets == 1) "time" else "times", fontSize = 18.sp) },
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = TextStyle(fontSize = 18.sp)
            )
        }
    }
}

@Composable
fun CardTemplate(
    header: String,
    onHeaderChange: (String) -> Unit,
    time: Int,
    onTimeChange: (Int, Int) -> Unit,
    cornersShape: RoundedCornerShape = RoundedCornerShape(12.dp),
    cardBackground: Color = MaterialTheme.colors.background
) {
    var soundOn by remember { mutableStateOf(true) }
    val minutes = time / 60
    val seconds = time % 60
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        elevation = 4.dp,
        shape = cornersShape,
        backgroundColor = cardBackground
    ) {
        Column(modifier = Modifier.padding(4.dp)) {
            OutlinedTextField(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                placeholder = { Text(text = "Please enter a header") },
                value = header,
                onValueChange = { if (it.length < 40) onHeaderChange(it) },
                textStyle = TextStyle(fontSize = 18.sp),
                singleLine = true
            )
            Row(
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                TimeInput(
                    label = "Minutes",
                    modifier = Modifier.weight(0.45f),
                    time = minutes,
                    onTimeValueChange = { onTimeChange(it.toInt(), seconds) }
                )
                TimeInput(
                    label = "Seconds",
                    modifier = Modifier.weight(0.45f),
                    time = seconds,
                    onTimeValueChange = { onTimeChange(minutes, it.toInt()) }
                )
                Image(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .weight(0.1f)
                        .align(alignment = Alignment.CenterVertically)
                        .clickable(onClick = { soundOn = !soundOn }),
                    painter = painterResource(id = if (soundOn) R.drawable.ic_sound else R.drawable.ic_sound_off),
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
        modifier = modifier.padding(horizontal = 4.dp),
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
        textStyle = TextStyle(fontSize = 18.sp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@ExperimentalAnimationApi
@Composable
fun Spinner(upTo: Int) {
    var spinnerValue by remember { mutableStateOf(0) }
    Column {
        Image(
            modifier = Modifier.clickable(onClick = {
                if (spinnerValue < upTo) spinnerValue++
            }),
            painter = painterResource(id = R.drawable.ic_arrow_up),
            contentDescription = "Increase",
        )
        AnimatedContent(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            targetState = spinnerValue,
            transitionSpec = {
                if (targetState > initialState) {
                    slideInVertically({ height -> height }) + fadeIn() with
                            (slideOutVertically({ height -> -height })) + fadeOut()
                } else {
                    slideInVertically({ height -> -height }) + fadeIn() with
                            (slideOutVertically({ height -> height }) + fadeOut())
                }.using(SizeTransform(clip = false))
            }
        ) { targetValue ->
            Text(
                text = "$targetValue",
                style = MaterialTheme.typography.h4
            )
        }
        Image(
            modifier = Modifier.clickable(onClick = {
                if (spinnerValue > 0) spinnerValue--
            }),
            painter = painterResource(id = R.drawable.ic_arrow_down),
            contentDescription = "Decrease"
        )
    }
}