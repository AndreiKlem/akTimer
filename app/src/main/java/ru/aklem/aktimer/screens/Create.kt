package ru.aklem.aktimer.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
            Row {
                Text(
                    text = "Min",
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier
                        .padding(start = 4.dp, end = 8.dp)
                        .align(Alignment.CenterVertically)
                )
                Spinner(upTo = 5)
                Spinner(upTo = 9)
                Text(
                    text = "Sec",
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
                        .align(Alignment.CenterVertically)

                )
                Spinner(upTo = 5)
                Spinner(upTo = 9)
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