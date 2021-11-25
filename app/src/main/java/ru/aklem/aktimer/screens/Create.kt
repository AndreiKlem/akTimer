package ru.aklem.aktimer.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ru.aklem.aktimer.Chart
import ru.aklem.aktimer.R
import ru.aklem.aktimer.ui.theme.setsBackground
import ru.aklem.aktimer.viewmodel.ChartViewModel

@ExperimentalAnimationApi
@Composable
fun CreateScreen(navController: NavController, chartViewModel: ChartViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val chart = Chart()
        var title by remember { mutableStateOf(chart.title) }
        OutlinedTextField(
            modifier = Modifier.padding(8.dp).align(Alignment.CenterHorizontally),
            value = title,
            onValueChange = { if (it.length < 40) title = it },
            placeholder = { Text(text = "Title") },
            textStyle = TextStyle(fontSize = 20.sp),
            singleLine = true
        )
        PrepareCard(header = chart.headerPrepare)
        ActionCard()
        RestCard()
        RepeatCard()
        Button(
            onClick = { navController.navigate("timer") },
            modifier = Modifier
                .align(Alignment.End)
                .padding(8.dp)
        ) {
            Text(text = "Create Timer")
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun PrepareCard(header: String) {
    CardTemplate(header = header)
}

@Composable
fun ActionCard() {
    val topShape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
    CardTemplate(header = "Action", cornersShape = topShape, cardBackground = setsBackground)
}

@Composable
fun RestCard() {
    val middleShape = RoundedCornerShape(0.dp)
    CardTemplate(header = "Rest", cornersShape = middleShape, cardBackground = setsBackground)
}

@Composable
fun RepeatCard() {
    var sets by rememberSaveable { mutableStateOf("1") }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp),
        backgroundColor = setsBackground
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Repeat", fontSize = 20.sp)
            TextField(
                modifier = Modifier
                    .padding(start = 4.dp, end = 4.dp)
                    .width(60.dp),
                value = sets,
                onValueChange = {
                    sets = when {
                        it.isEmpty() -> ""
                        it.toIntOrNull() == null -> ""
                        it.toInt() in 1..99 -> it
                        it.toInt() > 99 -> "99"
                        else -> "1"
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = TextStyle(fontSize = 20.sp)
            )
            Text(text = if (sets == "1") "time" else "times", fontSize = 20.sp)
        }
    }
}

@Composable
fun CardTemplate(
    chart: Chart,
    cornersShape: RoundedCornerShape = RoundedCornerShape(8.dp),
    cardBackground: Color = MaterialTheme.colors.background
) {
    var
    var soundOn by remember { mutableStateOf(true) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp,
        shape = cornersShape,
        backgroundColor = cardBackground
    ) {
        Column(modifier = Modifier.padding(4.dp)) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = header,
                fontSize = 20.sp
            )
            Row(
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                TimeInput(label = "Minutes", modifier = Modifier.weight(0.45f))
                TimeInput(label = "Seconds", modifier = Modifier.weight(0.45f))
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
fun TimeInput(label: String, modifier: Modifier) {
    var timeValue by rememberSaveable { mutableStateOf("0") }
    OutlinedTextField(
        modifier = modifier.padding(horizontal = 4.dp),
        label = { Text(text = label) },
        placeholder = { Text(text = "max 59") },
        value = timeValue,
        onValueChange = {
            timeValue = when {
                it.isEmpty() -> ""
                it.toIntOrNull() == null -> ""
                it.toInt() in 1..59 -> it
                it.toInt() > 59 -> "59"
                else -> "0"
            }
        },
        singleLine = true,
        textStyle = TextStyle(fontSize = 20.sp),
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