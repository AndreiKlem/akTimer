package ru.aklem.aktimer.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import kotlinx.coroutines.Job
import ru.aklem.aktimer.R
import ru.aklem.aktimer.utils.AppSettings
import ru.aklem.aktimer.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel) {
    val context = LocalContext.current
    val settings = settingsViewModel.appSettings.collectAsState(initial = AppSettings())
    val launcher = rememberLauncherForActivityResult(PickRingtone()) {
        it?.let { settingsViewModel.onUpdateSound(it.toString()) }
    }
    var expanded by remember { mutableStateOf(false) }
    Column(
        Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {

        Header(name = stringResource(id = R.string.timer_screen))
        SettingParameter(
            name = stringResource(id = R.string.title_show_setting),
            settings.value.showTitle,
            settingsViewModel::onShowTitleChange
        )
        SettingParameter(
            name = stringResource(id = R.string.period_show_setting),
            settings.value.showPeriods,
            settingsViewModel::onShowPeriodsChange
        )
        SettingParameter(
            name = stringResource(id = R.string.progress_bar_show_setting),
            settings.value.showProgressBar,
            settingsViewModel::onShowProgressBar
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.change_sound_setting),
                modifier = Modifier.weight(1f)
            )
            Box(Modifier.padding(end = 8.dp)) {
                Text(
                    text = if (settings.value.userSound.isNotEmpty()) {
                        RingtoneManager.getRingtone(context, settings.value.userSound.toUri())
                            .getTitle(context)
                    } else stringResource(id = R.string.default_sound),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.widthIn(max = 200.dp).clickable { expanded = !expanded },
                    color = MaterialTheme.colors.secondary
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = !expanded },
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    if (settings.value.userSound.isNotEmpty()) {
                        Text(
                            text = stringResource(id = R.string.default_sound),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    settingsViewModel.onUpdateSound("")
                                    expanded = !expanded
                                },
                            fontSize = 22.sp
                        )
                    }
                    Text(
                        text = stringResource(id = R.string.another_sound),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                launcher.launch(RingtoneManager.TYPE_NOTIFICATION)
                                expanded = !expanded
                            },
                        fontSize = 22.sp
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

//        Header(name = "Saved screen")
//        SettingParameter(name = "Select some setting for this screen")
        Spacer(modifier = Modifier.height(16.dp))

        Header(name = stringResource(id = R.string.create_screen))
        SettingParameter(
            name = stringResource(id = R.string.preparation_show_setting),
            settings.value.showPreparation,
            settingsViewModel::onShowPreparation
        )
        SettingParameter(
            name = stringResource(id = R.string.rest_show_setting),
            setting = settings.value.showRest,
            onClick = settingsViewModel::onShowRest
        )
    }
}

@Composable
fun Header(name: String) {
    Text(text = name, style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Thin))
    Divider(modifier = Modifier.height(1.dp), color = Color.Black)
}

@Composable
fun SettingParameter(name: String, setting: Boolean, onClick: (Boolean) -> Job) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = name, modifier = Modifier.alpha(if (setting) 1f else 0.38f))
        Switch(
            checked = setting,
            onCheckedChange = { onClick(!setting) },
            colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colors.secondary)
        )
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