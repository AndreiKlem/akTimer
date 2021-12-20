package ru.aklem.aktimer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.aklem.aktimer.data.PreferencesManager
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
): ViewModel() {

    val appSettings = preferencesManager.settingsFlow

    fun onShowTitleChange(value: Boolean) = viewModelScope.launch {
        preferencesManager.updateShowTitle(value)
    }

    fun onShowPeriodsChange(value: Boolean) = viewModelScope.launch {
        preferencesManager.updateShowPeriods(value)
    }

    fun onShowProgressBar(value: Boolean) = viewModelScope.launch {
        preferencesManager.updateShowProgressBar(value)
    }

    fun onShowPreparation(value: Boolean) = viewModelScope.launch {
        preferencesManager.updateShowPreparation(value)
    }

    fun onShowRest(value: Boolean) = viewModelScope.launch {
        preferencesManager.updateShowRest(value)
    }

    fun onUpdateSound(value: String) = viewModelScope.launch {
        preferencesManager.updateUserSound(value)
    }
}

