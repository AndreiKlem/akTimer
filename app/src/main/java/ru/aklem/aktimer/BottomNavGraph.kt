package ru.aklem.aktimer

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import kotlinx.coroutines.InternalCoroutinesApi
import ru.aklem.aktimer.misc.Directions
import ru.aklem.aktimer.misc.Directions.BACKWARDS
import ru.aklem.aktimer.misc.Directions.FORWARD
import ru.aklem.aktimer.screens.CreateScreen
import ru.aklem.aktimer.screens.SavedScreen
import ru.aklem.aktimer.screens.TimerScreen
import ru.aklem.aktimer.viewmodel.ChartViewModel
import ru.aklem.aktimer.viewmodel.TimerViewModel

@ExperimentalFoundationApi
@InternalCoroutinesApi
@ExperimentalAnimationApi
@Composable
fun BottomNavGraph(
    navController: NavHostController,
    timerViewModel: TimerViewModel,
    chartViewModel: ChartViewModel
) {
    var direction = FORWARD
    var currentScreen = remember { mutableStateOf(BottomBarScreen.Timer.route).value }
    AnimatedNavHost(
        navController = navController,
        startDestination = BottomBarScreen.Timer.route
    ) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val nextScreen = destination.route.toString()
            direction = if (currentScreen == BottomBarScreen.Saved.route &&
                nextScreen == BottomBarScreen.Create.route
                || currentScreen == BottomBarScreen.Timer.route &&
                nextScreen == BottomBarScreen.Saved.route) FORWARD
            else BACKWARDS
        }
        composable(
            route = BottomBarScreen.Timer.route,
            enterTransition = { enterAnimation(BACKWARDS) },
            exitTransition = { exitAnimation(FORWARD) }
        ) {
            currentScreen = BottomBarScreen.Timer.route
            TimerScreen(
                timerViewModel = timerViewModel
            )
        }
        composable(
            route = BottomBarScreen.Saved.route,
            enterTransition = { enterAnimation(direction) },
            exitTransition = { exitAnimation(direction) }
        ) {
            currentScreen = BottomBarScreen.Saved.route
            SavedScreen(
                navController = navController,
                timerViewModel = timerViewModel,
                chartViewModel = chartViewModel
            )
        }
        composable(
            route = BottomBarScreen.Create.route,
            enterTransition = { enterAnimation(FORWARD) },
            exitTransition = { exitAnimation(BACKWARDS) }
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
                onPlaySoundChange = chartViewModel::onPlaySoundChange,
                playSound = chartViewModel::getPlaySoundStatus,
                sets = sets,
                onSetsAmountChange = chartViewModel::onRepeatChange,
                createChart = chartViewModel::createChart
            )
        }
    }
}

private fun exitAnimation(direction: Directions): ExitTransition {
    return if (direction == FORWARD) {
        slideOutHorizontally(targetOffsetX = { -it / 2 }, animationSpec = tween(300)) +
                fadeOut(animationSpec = tween(300))
    } else {
        slideOutHorizontally(targetOffsetX = { it / 2 }, animationSpec = tween(300)) +
                fadeOut(animationSpec = tween(300))
    }
}

private fun enterAnimation(direction: Directions): EnterTransition {
    return if (direction == FORWARD) {
        slideInHorizontally(initialOffsetX = { it / 2 }, animationSpec = tween(300)) +
                fadeIn(animationSpec = tween(300))
    } else {
        slideInHorizontally(initialOffsetX = { -it / 2 }, animationSpec = tween(300)) +
                fadeIn(animationSpec = tween(300))
    }
}