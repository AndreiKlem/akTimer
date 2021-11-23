package ru.aklem.aktimer.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.aklem.aktimer.R
import ru.aklem.aktimer.ui.theme.AkTimerTheme

@ExperimentalAnimationApi
@Composable
fun CreateScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        PrepareCard()
    }
}

@ExperimentalAnimationApi
@Composable
fun PrepareCard() {
    var prepareMinutes by rememberSaveable { mutableStateOf("") }
    var prepareSeconds by rememberSaveable { mutableStateOf("") }
    val maxChars = 2
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(4.dp)) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "Preparation",
                style = MaterialTheme.typography.h5
            )
            Row(
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .weight(weight = 1f, fill = false)
                        .align(Alignment.CenterVertically),
                    label = { Text(text = "Minutes") },
                    placeholder = { Text(text = "max 59") },
                    value = prepareMinutes,
                    onValueChange = {
                        if (it.isEmpty()) {
                            prepareMinutes = ""
                        } else if (it.length <= maxChars) {
                            prepareMinutes = if (it.toInt() < 60) it else "59"
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .weight(weight = 1f, fill = false)
                        .align(Alignment.CenterVertically),
                    label = { Text(text = "Seconds") },
                    placeholder = { Text(text = "max 59") },
                    value = prepareSeconds,
                    onValueChange = {
                        if (it.isEmpty()) {
                            prepareSeconds = ""
                        } else if (it.length <= maxChars) {
                            prepareSeconds = if (it.toInt() < 60) it else "59"
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }
    }
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

@ExperimentalAnimationApi
@Composable
@Preview
fun CreateScreenPreview() {
    AkTimerTheme {
        CreateScreen()
    }
}

@ExperimentalAnimationApi
@Composable
@Preview
fun PrepareCardPreview() {
    PrepareCard()
}