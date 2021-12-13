package ru.aklem.aktimer.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen() {
    Column(
        Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {

        Header(name = "Timer screen")
        SettingParameter(name = "Show selected timer title")
        SettingParameter(name = "Show selected timer periods")
        SettingParameter(name = "Show progress bar")
        Spacer(modifier = Modifier.height(16.dp))

        Header(name = "Saved screen")
        SettingParameter(name = "Select some setting for this screen")
        Spacer(modifier = Modifier.height(16.dp))

        Header(name = "Saved screen")
        SettingParameter(name = "Spinner as a time selector")
    }
}

@Composable
fun Header(name: String) {
    Text(text = name)
    Divider(modifier = Modifier.height(1.dp), color = Color.Black)
}

@Composable
fun SettingParameter(name: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        var check by remember { mutableStateOf(true) }
        Text(text = name)
        Switch(checked = check, onCheckedChange = { check = !check })
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}