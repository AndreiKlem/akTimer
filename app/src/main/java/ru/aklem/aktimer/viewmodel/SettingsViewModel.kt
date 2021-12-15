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

    val showTitle = preferencesManager.showTitle

    fun onShowTitleChange(value: Boolean) = viewModelScope.launch {
        preferencesManager.updateShowTitle(value)
    }
}