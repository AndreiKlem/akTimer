package ru.aklem.aktimer.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ru.aklem.aktimer.R
import ru.aklem.aktimer.data.Chart
import ru.aklem.aktimer.viewmodel.TimerViewModel

@Composable
fun SavedScreen(
    navController: NavController,
    timerViewModel: TimerViewModel,
    charts: List<Chart>?,
    onClick: (Int) -> Unit
) {
    if (charts.isNullOrEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "No timers created yet",
                style = TextStyle(fontSize = 20.sp),
                textAlign = TextAlign.Center
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            itemsIndexed(items = charts) { index, chart ->
                ChartCard(
                    navController = navController,
                    onChartSelected = timerViewModel::stop,
                    chart = chart,
                    index = index,
                    onClick = onClick
                )
            }
        }
    }
}

@Composable
fun ChartCard(
    navController: NavController,
    onChartSelected: () -> Unit,
    chart: Chart,
    index: Int,
    onClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth()
            .clickable {
                onChartSelected()
                onClick(index)
                navController.navigate("timer")
            },
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = chart.title,
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
            )
            if (chart.preparationTime > 0) {
                Text(text = "${chart.headerPreparation} ${getTimerText(chart.preparationTime)}")
            }
            Row(
                modifier = Modifier.height(intrinsicSize = IntrinsicSize.Max),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (chart.repeat > 1) {
                    Text(text = "${chart.repeat}X ")
                    Image(
                        painter = painterResource(id = R.drawable.ic_curly_brace),
                        contentDescription = null,
                        contentScale = ContentScale.Fit
                    )
                }
                Column {
                    Text(text = "${chart.headerAction} ${getTimerText(chart.actionTime)}")
                    if (chart.restTime > 0) {
                        Text(text = "${chart.headerRest} ${getTimerText(chart.restTime)}")
                    }
                }
            }
        }
    }
}