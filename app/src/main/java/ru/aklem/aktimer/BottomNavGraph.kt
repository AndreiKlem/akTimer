package ru.aklem.aktimer

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.coroutines.InternalCoroutinesApi
import ru.aklem.aktimer.screens.CreateScreen
import ru.aklem.aktimer.screens.SavedScreen
import ru.aklem.aktimer.screens.TimerScreen
import ru.aklem.aktimer.viewmodel.ChartViewModel

@InternalCoroutinesApi
@ExperimentalAnimationApi
@Composable
fun BottomNavGraph(
    navController: NavHostController,
    onStartPause: (List<Int>) -> Unit,
    onStop: () -> Unit,
    chartViewModel: ChartViewModel,
    timerValue: Int,
    isRunning: Boolean
) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Timer.route
    ) {
        composable(route = BottomBarScreen.Timer.route) {
            TimerScreen(
                chartViewModel = chartViewModel,
                onStartPause = onStartPause,
                onStop = onStop,
                timerValue = timerValue,
                isRunning = isRunning
            )
        }
        composable(route = BottomBarScreen.Saved.route) {
            val charts = chartViewModel.charts.observeAsState().value
            SavedScreen(
                navController = navController,
                charts = charts,
                onClick = chartViewModel::selectChart
            )
        }
        composable(route = BottomBarScreen.Create.route) {
            val title = chartViewModel.title.collectAsState().value
            val headerPrepare = chartViewModel.headerPrepare.collectAsState().value
            val prepareTime = chartViewModel.prepareTime.collectAsState().value
            val headerAction = chartViewModel.headerAction.collectAsState().value
            val actionTime = chartViewModel.actionTime.collectAsState().value
            val headerRest = chartViewModel.headerRest.collectAsState().value
            val restTime = chartViewModel.restTime.collectAsState().value
            val sets = chartViewModel.repeat.collectAsState().value
            CreateScreen(
                navController = navController,
                title = title,
                onTitleChange = chartViewModel::onTitleChange,
                headerPrepare = headerPrepare,
                onHeaderPrepareChange = chartViewModel::onHeaderPrepareChange,
                prepareTime = prepareTime,
                onPrepareTimeChange = chartViewModel::onPrepareTimeChange,
                headerAction = headerAction,
                onHeaderActionChange = chartViewModel::onHeaderActionChange,
                actionTime = actionTime,
                onActionTimeChange = chartViewModel::onActionTimeChange,
                headerRest = headerRest,
                onHeaderRestChange = chartViewModel::onHeaderRestChange,
                restTime = restTime,
                onRestTimeChange = chartViewModel::onRestTimeChange,
                sets = sets,
                onSetsAmountChange = chartViewModel::onRepeatChange,
                createChart = chartViewModel::createChart
            )
        }
    }
}