package ru.aklem.aktimer

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: Int
) {
    object Timer: BottomBarScreen(
        route = "timer",
        title = "Timer",
        icon = R.drawable.ic_timer
    )
    object Saved: BottomBarScreen(
        route = "saved",
        title = "Saved",
        icon = R.drawable.ic_saved
    )
    object Create: BottomBarScreen(
        route = "create",
        title = "Create",
        icon = R.drawable.ic_create
    )
}
