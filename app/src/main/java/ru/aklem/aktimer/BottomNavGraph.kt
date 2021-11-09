package ru.aklem.aktimer

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.aklem.aktimer.screens.CreateScreen
import ru.aklem.aktimer.screens.HomeScreen
import ru.aklem.aktimer.screens.SavedScreen
import ru.aklem.aktimer.viewmodel.TimerViewModel

@ExperimentalAnimationApi
@Composable
fun BottomNavGraph(
    navController: NavHostController,
    viewModel: TimerViewModel,
    timerValue: Int,
    isRunning: Boolean
) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route
    ) {
        composable(route = BottomBarScreen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                timerValue = timerValue,
                isRunning = isRunning
            )
        }
        composable(route = BottomBarScreen.Saved.route) {
            SavedScreen()
        }
        composable(route = BottomBarScreen.Create.route) {
            CreateScreen()
        }
    }
}