package ru.aklem.aktimer.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import ru.aklem.aktimer.misc.AppSettings
import ru.aklem.aktimer.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel) {
    val settings = settingsViewModel.appSettings.collectAsState(initial = AppSettings())
    Column(
        Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {

        Header(name = "Timer screen")
        SettingParameter(
            name = "Show selected timer title",
            settings.value.showTitle,
            settingsViewModel::onShowTitleChange
        )
        SettingParameter(
            name = "Show selected timer periods",
            settings.value.showPeriods,
            settingsViewModel::onShowPeriodsChange
        )
        SettingParameter(
            name = "Show progress bar",
            settings.value.showProgressBar,
            settingsViewModel::onShowProgressBar
        )
        Spacer(modifier = Modifier.height(16.dp))

        Header(name = "Saved screen")
//        SettingParameter(name = "Select some setting for this screen")
        Spacer(modifier = Modifier.height(16.dp))

        Header(name = "Create screen")
//        SettingParameter(name = "Spinner as a time selector")
    }


}

@Composable
fun Header(name: String) {
    Text(text = name)
    Divider(modifier = Modifier.height(1.dp), color = Color.Black)
}

@Composable
fun SettingParameter(name: String, setting: Boolean, onClick: (Boolean) -> Job) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = name)
        Switch(checked = setting, onCheckedChange = { onClick(!setting) })
    }
}