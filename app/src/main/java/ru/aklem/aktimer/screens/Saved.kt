package ru.aklem.aktimer.screens

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.InternalCoroutinesApi
import ru.aklem.aktimer.R
import ru.aklem.aktimer.data.Chart
import ru.aklem.aktimer.viewmodel.ChartViewModel
import ru.aklem.aktimer.viewmodel.TimerViewModel

@ExperimentalFoundationApi
@InternalCoroutinesApi
@Composable
fun SavedScreen(
    navController: NavController,
    timerViewModel: TimerViewModel,
    chartViewModel: ChartViewModel,
) {
    val charts = chartViewModel.charts.observeAsState().value
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
        if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
            LazyColumn(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                itemsIndexed(items = charts) { _, chart ->
                    ChartCard(
                        navController = navController,
                        stopTimerOnChartSelected = timerViewModel::stop,
                        chart = chart,
                        onSelectChart = chartViewModel::onSelectChart,
                        setTimerPeriods = timerViewModel::setTimerPeriods
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                cells = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp)
            ) {
                itemsIndexed(items = charts) { _, chart ->
                    ChartCard(
                        navController = navController,
                        stopTimerOnChartSelected = timerViewModel::stop,
                        chart = chart,
                        onSelectChart = chartViewModel::onSelectChart,
                        setTimerPeriods = timerViewModel::setTimerPeriods
                    )
                }
            }
        }
    }
}

@Composable
fun ChartCard(
    navController: NavController,
    stopTimerOnChartSelected: () -> Unit,
    chart: Chart,
    onSelectChart: (Chart) -> Unit,
    setTimerPeriods: (Chart) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .fillMaxWidth()
            .clickable {
                stopTimerOnChartSelected()
                setTimerPeriods(chart)
                onSelectChart(chart)
                navController.navigate("timer")
            }
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = chart.title,
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
            )
            if (chart.preparationTime > 0) {
                GetPeriodDescription(
                    header = chart.headerPreparation,
                    time = chart.preparationTime,
                    case = chart.playPreparationSound
                )
            }
            Row(
                modifier = Modifier.height(intrinsicSize = IntrinsicSize.Max),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (chart.repeat > 0) {
                    Text(text = "${chart.repeat + 1}X ")
                    Image(
                        painter = painterResource(id = R.drawable.ic_curly_brace),
                        contentDescription = null,
                        contentScale = ContentScale.Fit
                    )
                }
                Column {
                    GetPeriodDescription(
                        header = chart.headerAction,
                        time = chart.actionTime,
                        case = chart.playActionSound
                    )
                    if (chart.restTime > 0) {
                        GetPeriodDescription(
                            header = chart.headerRest,
                            time = chart.restTime,
                            case = chart.playRestSound
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GetPeriodDescription(header: String, time: Int, case: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = header)
        Row {
            Text(text = getTimerText(time))
            if (case) Image(
                painter = painterResource(id = R.drawable.ic_note),
                contentDescription = "With sound",
                modifier = Modifier.align(Alignment.CenterVertically)
            ) else Image(
                painter = painterResource(id = R.drawable.ic_note),
                contentDescription = null,
                alpha = 0f,
            )
        }
    }
}

@Preview
@Composable
fun GetPeriodDescriptionPreview() {
    GetPeriodDescription(header = "Header", time = 150, case = true)
}