package ru.aklem.aktimer

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: Int
) {
    object Saved: BottomBarScreen(
        route = "saved",
        title = "Saved",
        icon = R.drawable.ic_saved
    )
    object Timer: BottomBarScreen(
        route = "timer",
        title = "Timer",
        icon = R.drawable.ic_timer
    )
    object Create: BottomBarScreen(
        route = "create/{tag}",
        title = "Create",
        icon = R.drawable.ic_create
    )
    object Settings: BottomBarScreen(
        route = "settings",
        title = "Settings",
        icon = R.drawable.ic_settings
    )
}
