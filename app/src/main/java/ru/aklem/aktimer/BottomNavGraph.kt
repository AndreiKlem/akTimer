package ru.aklem.aktimer

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalFocusManager
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import kotlinx.coroutines.InternalCoroutinesApi
import ru.aklem.aktimer.utils.Directions
import ru.aklem.aktimer.utils.Directions.BACKWARDS
import ru.aklem.aktimer.utils.Directions.FORWARD
import ru.aklem.aktimer.screens.CreateScreen
import ru.aklem.aktimer.screens.SavedScreen
import ru.aklem.aktimer.screens.SettingsScreen
import ru.aklem.aktimer.screens.TimerScreen
import ru.aklem.aktimer.viewmodel.ChartViewModel
import ru.aklem.aktimer.viewmodel.SettingsViewModel
import ru.aklem.aktimer.viewmodel.TimerViewModel

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@InternalCoroutinesApi
@ExperimentalAnimationApi
@Composable
fun BottomNavGraph(
    navController: NavHostController
) {
    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    val timerViewModel = hiltViewModel<TimerViewModel>()
    val chartViewModel = hiltViewModel<ChartViewModel>()
    var direction = FORWARD
    var currentScreen = remember { mutableStateOf(BottomBarScreen.Saved.route).value }
    AnimatedNavHost(
        navController = navController,
        startDestination = BottomBarScreen.Saved.route
    ) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val nextScreen = destination.route.toString()
            direction = if (getPosition(nextScreen) > getPosition(currentScreen)) FORWARD
            else BACKWARDS
            currentScreen = nextScreen
        }
        composable(
            route = BottomBarScreen.Saved.route,
            enterTransition = { enterAnimation(BACKWARDS) },
            exitTransition = { exitAnimation(FORWARD) }
        ) {
            SavedScreen(
                navController = navController,
                timerViewModel = timerViewModel,
                chartViewModel = chartViewModel
            )
        }
        composable(
            route = BottomBarScreen.Timer.route,
            enterTransition = { enterAnimation(direction) },
            exitTransition = { exitAnimation(direction) }
        ) {
            TimerScreen(
                timerViewModel = timerViewModel,
                chartViewModel = chartViewModel,
                settingsViewModel = settingsViewModel
            )
        }
        composable(
            route = BottomBarScreen.Create.route,
            arguments = listOf(navArgument("tag") { type = NavType.StringType }),
            enterTransition = { enterAnimation(direction) },
            exitTransition = { exitAnimation(direction) }
        ) {backStackEntry ->
            val title = chartViewModel.title.collectAsState().value
            val sets = chartViewModel.repeat.collectAsState().value
            val focusManager = LocalFocusManager.current
            CreateScreen(
                navController = navController,
                tag = backStackEntry.arguments?.getString("tag"),
                title = title,
                onTitleChange = chartViewModel::onTitleChange,
                onHeaderChange = chartViewModel::onHeaderChange,
                header = chartViewModel::getHeader,
                onTimeChange = chartViewModel::onTimeChange,
                time = chartViewModel::getTime,
                onSetPlaySound = chartViewModel::onSetPlaySound,
                playSound = chartViewModel::getPlaySound,
                sets = sets,
                onRepeatChange = chartViewModel::onRepeatChange,
                updateChart = chartViewModel::onUpdateChart,
                createChart = chartViewModel::createChart,
                settingsViewModel = settingsViewModel,
                focusManager = focusManager
            )
        }
        composable(
            route = BottomBarScreen.Settings.route,
            enterTransition = { enterAnimation(FORWARD) },
            exitTransition = { exitAnimation(BACKWARDS) }
        ) {
            SettingsScreen(settingsViewModel)
        }
    }
}

private fun getPosition(route: String): Int {
    return when (route) {
        "saved" -> 1
        "timer" -> 2
        "create/{tag}" -> 3
        else -> 4
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