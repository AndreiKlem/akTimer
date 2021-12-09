package ru.aklem.aktimer

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import kotlinx.coroutines.InternalCoroutinesApi
import ru.aklem.aktimer.misc.Period
import ru.aklem.aktimer.screens.CreateScreen
import ru.aklem.aktimer.screens.SavedScreen
import ru.aklem.aktimer.screens.TimerScreen
import ru.aklem.aktimer.viewmodel.ChartViewModel
import ru.aklem.aktimer.viewmodel.TimerViewModel

@InternalCoroutinesApi
@ExperimentalAnimationApi
@Composable
fun BottomNavGraph(
    navController: NavHostController,
    onStartPause: (List<Period>) -> Unit,
    onStop: () -> Unit,
    timerViewModel: TimerViewModel,
    chartViewModel: ChartViewModel,
    timerValue: Int,
    isRunning: Boolean
) {
    var direction = "forward"
    var currentScreen = "timer"
    AnimatedNavHost(
        navController = navController,
        startDestination = BottomBarScreen.Timer.route
    ) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val nextScreen = destination.route.toString()
            direction = if (currentScreen == "saved" && nextScreen == "create" ||
                currentScreen == "timer" && nextScreen == "saved") "forward"
            else "backwards"
        }
        composable(
            route = BottomBarScreen.Timer.route,
            enterTransition = { enterAnimation("backwards") },
            exitTransition = { exitAnimation("forward") }
        ) {
            currentScreen = BottomBarScreen.Timer.route
            TimerScreen(
                chartViewModel = chartViewModel,
                onStartPause = onStartPause,
                onStop = onStop,
                timerValue = timerValue,
                isRunning = isRunning
            )
        }
        composable(
            route = BottomBarScreen.Saved.route,
            enterTransition = { enterAnimation(direction) },
            exitTransition = { exitAnimation(direction) }
        ) {
            currentScreen = BottomBarScreen.Saved.route
            val charts = chartViewModel.charts.observeAsState().value
            SavedScreen(
                navController = navController,
                timerViewModel = timerViewModel,
                charts = charts,
                onClick = chartViewModel::selectChart
            )
        }
        composable(
            route = BottomBarScreen.Create.route,
            enterTransition = { enterAnimation("forward") },
            exitTransition = { exitAnimation("backwards") }
        ) {
            currentScreen = BottomBarScreen.Create.route
            val title = chartViewModel.title.collectAsState().value
            val headerPrepare = chartViewModel.headerPreparation.collectAsState().value
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

private fun exitAnimation(direction: String): ExitTransition {
    return if (direction == "forward") {
        slideOutHorizontally(targetOffsetX = { -it / 2 }, animationSpec = tween(300)) +
                fadeOut(animationSpec = tween(300))
    } else {
        slideOutHorizontally(targetOffsetX = { it / 2 }, animationSpec = tween(300)) +
                fadeOut(animationSpec = tween(300))
    }
}

private fun enterAnimation(direction: String): EnterTransition {
    return if (direction == "forward") {
        slideInHorizontally(initialOffsetX = { it / 2 }, animationSpec = tween(300)) +
                fadeIn(animationSpec = tween(300))
    } else {
        slideInHorizontally(initialOffsetX = { -it / 2 }, animationSpec = tween(300)) +
                fadeIn(animationSpec = tween(300))
    }
}