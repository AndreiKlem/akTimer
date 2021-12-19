package ru.aklem.aktimer.screens

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.res.AssetManager
import android.media.RingtoneManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Job
import ru.aklem.aktimer.misc.AppSettings
import ru.aklem.aktimer.viewmodel.SettingsViewModel
import ru.aklem.aktimer.viewmodel.TimerViewModel

@Composable
fun SettingsScreen(timerViewModel: TimerViewModel, settingsViewModel: SettingsViewModel) {
    val context = LocalContext.current
    val settings = settingsViewModel.appSettings.collectAsState(initial = AppSettings())
    val ringtoneUri = remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(PickRingtone()) {
        ringtoneUri.value = it
        timerViewModel.onRingtoneSet(ringtoneUri.value)
    }
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
        SettingParameter(
            name = "Show preparation period",
            settings.value.showPreparation,
            settingsViewModel::onShowPreparation
        )
        SettingParameter(
            name = "Show rest period",
            setting = settings.value.showRest,
            onClick = settingsViewModel::onShowRest
        )
        Button(onClick = {
            launcher.launch(RingtoneManager.TYPE_NOTIFICATION)
        }) {
            Text(text = "Ringtone picker")
        }
        Text(
            text = RingtoneManager.getRingtone(context, ringtoneUri.value).getTitle(context),
            fontSize = 24.sp
        )
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

class PickRingtone : ActivityResultContract<Int, Uri?>() {
    override fun createIntent(context: Context, input: Int) =
        Intent(RingtoneManager.ACTION_RINGTONE_PICKER).apply {
            putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, input)
        }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        if (resultCode != Activity.RESULT_OK) return null
        return intent?.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
    }
}