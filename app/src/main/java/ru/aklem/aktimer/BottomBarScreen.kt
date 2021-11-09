package ru.aklem.aktimer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home: BottomBarScreen(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )
    object Saved: BottomBarScreen(
        route = "saved",
        title = "Saved",
        icon = Icons.Default.Favorite
    )
    object Create: BottomBarScreen(
        route = "create",
        title = "Create",
        icon = Icons.Default.Create
    )
}
