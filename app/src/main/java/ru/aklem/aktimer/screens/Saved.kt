package ru.aklem.aktimer.screens

import android.content.ContentValues.TAG
import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import ru.aklem.aktimer.R
import ru.aklem.aktimer.data.Chart
import ru.aklem.aktimer.viewmodel.ChartViewModel
import ru.aklem.aktimer.viewmodel.TimerViewModel

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@InternalCoroutinesApi
@Composable
fun SavedScreen(
    navController: NavController,
    timerViewModel: TimerViewModel,
    chartViewModel: ChartViewModel,
) {
    val charts = chartViewModel.charts.observeAsState().value
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(modifier = Modifier.fillMaxSize(), scaffoldState = scaffoldState, snackbarHost = {
        SnackbarHost(it) { data ->
            Snackbar(
                snackbarData = data,
                backgroundColor = MaterialTheme.colors.primaryVariant,
                actionColor = MaterialTheme.colors.secondary
            )
        }
    }) {
        if (charts.isNullOrEmpty()) {
            val infiniteTransition = rememberInfiniteTransition()
            val bottomPadding by infiniteTransition.animateValue(
                initialValue = 32.dp,
                targetValue = 16.dp,
                typeConverter = Dp.VectorConverter,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000),
                    repeatMode = RepeatMode.Reverse
                )
            )
            Box(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.align(alignment = Alignment.Center)) {
                    Text(
                        text = stringResource(id = R.string.no_timers),
                        style = TextStyle(fontSize = 20.sp)
                    )
                }
                Box(
                    modifier = Modifier
                        .align(alignment = Alignment.BottomEnd)
                        .padding(bottom = bottomPadding, end = 180.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_arrow_down),
                        contentDescription = "Arrow down"
                    )
                }
                // Several timers for testing purposes
                /* Button(
                    modifier = Modifier.padding(top = 32.dp),
                    onClick = { chartViewModel.insertTestChartsToDatabase(getTestCharts()) }
                ) {
                    Text(text = stringResource(id = R.string.insert_timers))
                }*/
            }
        } else {
            if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
                LazyColumn(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    state = rememberLazyListState()
                ) {
                    items(items = charts, key = { it.id }) { item ->
                        Row(Modifier.animateItemPlacement(animationSpec = tween(300))) {
                            ChartCard(
                                navController = navController,
                                coroutineScope = coroutineScope,
                                scaffoldState = scaffoldState,
                                stopTimerOnChartSelected = timerViewModel::stop,
                                chart = item,
                                onSelectChart = chartViewModel::onSelectChart,
                                setTimerPeriods = timerViewModel::setTimerPeriods,
                                onRestoreChart = chartViewModel::onRestoreChart,
                                onEditChart = chartViewModel::onEditChart,
                                onDeleteChart = chartViewModel::onDeleteChart
                            )
                        }
                    }
                }
            } else {
                LazyVerticalGrid(
                    cells = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp)
                ) {
                    itemsIndexed(items = charts) { _, item ->
                        ChartCard(
                            navController = navController,
                            coroutineScope = coroutineScope,
                            scaffoldState = scaffoldState,
                            stopTimerOnChartSelected = timerViewModel::stop,
                            chart = item,
                            onSelectChart = chartViewModel::onSelectChart,
                            setTimerPeriods = timerViewModel::setTimerPeriods,
                            onRestoreChart = chartViewModel::onRestoreChart,
                            onEditChart = chartViewModel::onEditChart,
                            onDeleteChart = chartViewModel::onDeleteChart
                        )
                    }
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun ChartCard(
    navController: NavController,
    coroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState,
    stopTimerOnChartSelected: () -> Unit,
    chart: Chart,
    onSelectChart: (Chart) -> Unit,
    setTimerPeriods: (Chart) -> Unit,
    onRestoreChart: (Chart) -> Unit,
    onEditChart: (Chart) -> Unit,
    onDeleteChart: (Chart) -> Unit
) {
    val snackBarMessage = stringResource(id = R.string.timer_deleted)
    val snackBarActionLabel = stringResource(id = R.string.undo)
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth()
            .animateContentSize(animationSpec = tween(400))
            .clickable {
                stopTimerOnChartSelected()
                setTimerPeriods(chart)
                onSelectChart(chart)
                navController.navigate("timer")
            }
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(modifier = Modifier.padding(bottom = 8.dp)) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = chart.title,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Thin
                    )
                )
                Box {
                    Image(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = stringResource(id = R.string.settings),
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colors.secondary),
                        modifier = Modifier.clickable {
                            expanded = true
                        }
                    )
                    DropdownMenu(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        expanded = expanded,
                        onDismissRequest = { expanded = false }) {
                        Text(
                            text = stringResource(id = R.string.edit),
                            color = MaterialTheme.colors.secondary,
                            modifier = Modifier.clickable {
                                onEditChart(chart)
                                navController.navigate(route = "create/edit")
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(id = R.string.delete),
                            color = MaterialTheme.colors.secondary,
                            modifier = Modifier.clickable {
                                onDeleteChart(chart)
                                coroutineScope.launch {
                                    val snackBarResult =
                                        scaffoldState.snackbarHostState.showSnackbar(
                                            message = snackBarMessage,
                                            actionLabel = snackBarActionLabel
                                        )
                                    when (snackBarResult) {
                                        SnackbarResult.Dismissed -> Log.d(TAG, "SnackBar dismissed")
                                        SnackbarResult.ActionPerformed -> {
                                            onRestoreChart(chart)
                                        }
                                    }
                                }
                            }
                        )
                    }
                }
            }

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
                if (chart.repeat > 1) {
                    Text(
                        modifier = Modifier.padding(end = 4.dp),
                        text = "${chart.repeat}X "
                    )
                    if (chart.restTime > 0) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_curly_brace),
                            contentDescription = null,
                            contentScale = ContentScale.Fit
                        )
                    }
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
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp),
            text = header,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(text = getTimerText(time))
        Image(
            painter = painterResource(id = R.drawable.ic_note),
            alignment = Alignment.BottomCenter,
            contentDescription = null,
            alpha = if (case) 1f else 0f,
        )
    }
}