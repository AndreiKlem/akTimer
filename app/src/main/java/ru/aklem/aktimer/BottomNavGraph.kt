package ru.aklem.aktimer

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.aklem.aktimer.screens.CreateScreen
import ru.aklem.aktimer.screens.SavedScreen
import ru.aklem.aktimer.screens.TimerScreen

@ExperimentalAnimationApi
@Composable
fun BottomNavGraph(
    navController: NavHostController,
    onStartPause: (List<Int>) -> Unit,
    onStop: () -> Unit,
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
            CreateScreen(navController)
        }
    }
}