package ru.aklem.aktimer

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.aklem.aktimer.screens.CreateScreen
import ru.aklem.aktimer.screens.SavedScreen
import ru.aklem.aktimer.screens.TimerScreen
import ru.aklem.aktimer.viewmodel.ChartViewModel

@ExperimentalAnimationApi
@Composable
fun BottomNavGraph(
    navController: NavHostController,
    onStartPause: (List<Int>) -> Unit,
    onStop: () -> Unit,
    chartViewModel: ChartViewModel,
    startValue: List<Int>,
    timerValue: Int,
    isRunning: Boolean
) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Timer.route
    ) {
        composable(route = BottomBarScreen.Timer.route) {
            TimerScreen(
                onStartPause = onStartPause,
                onStop = onStop,
                startValue = startValue,
                timerValue = timerValue,
                isRunning = isRunning
            )
        }
        composable(route = BottomBarScreen.Saved.route) {
            SavedScreen()
        }
        composable(route = BottomBarScreen.Create.route) {
            val title = chartViewModel.title.collectAsState().value
            val headerPrepare = chartViewModel.headerPrepare.collectAsState().value
            val headerAction = chartViewModel.headerAction.collectAsState().value
            val headerRest = chartViewModel.headerRest.collectAsState().value
            CreateScreen(
                navController = navController,
                title = title,
                onTitleChange = chartViewModel::onTitleChange,
                headerPrepare = headerPrepare,
                onHeaderPrepareChange = chartViewModel::onHeaderPrepareChange,
                headerAction = headerAction,
                onHeaderActionChange = chartViewModel::onHeaderActionChange,
                headerRest = headerRest,
                onHeaderRestChange = chartViewModel::onHeaderRestChange
            )
        }
    }
}