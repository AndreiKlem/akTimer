package ru.aklem.aktimer.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import ru.aklem.aktimer.utils.AppSettings
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "PreferencesManager"

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val dataStore = context.dataStore

    val settingsFlow = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val showTitle = preferences[PreferencesKeys.SHOW_TITLE] ?: true
            val showPeriods = preferences[PreferencesKeys.SHOW_PERIODS] ?: true
            val showProgressBar = preferences[PreferencesKeys.SHOW_PROGRESS_BAR] ?: true
            val showPreparation = preferences[PreferencesKeys.SHOW_PREPARATION] ?: true
            val showRest = preferences[PreferencesKeys.SHOW_REST] ?: true
            val userSound = preferences[PreferencesKeys.USER_SOUND] ?: ""
            AppSettings(showTitle, showPeriods, showProgressBar, showPreparation, showRest, userSound)
        }

    suspend fun updateShowTitle(showTitle: Boolean) {
        dataStore.edit { preferences -> preferences[PreferencesKeys.SHOW_TITLE] = showTitle }
    }

    suspend fun updateShowPeriods(showPeriods: Boolean) {
        dataStore.edit { preferences -> preferences[PreferencesKeys.SHOW_PERIODS] = showPeriods }
    }

    suspend fun updateShowProgressBar(showProgressBar: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SHOW_PROGRESS_BAR] = showProgressBar
        }
    }

    suspend fun updateShowPreparation(showPreparation: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SHOW_PREPARATION] = showPreparation
        }
    }

    suspend fun updateShowRest(showRest: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SHOW_REST] = showRest
        }
    }

    suspend fun updateUserSound(uri: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_SOUND] = uri
        }
    }

    private object PreferencesKeys {
        val SHOW_TITLE = booleanPreferencesKey(name = "show_title")
        val SHOW_PERIODS = booleanPreferencesKey(name = "show_periods")
        val SHOW_PROGRESS_BAR = booleanPreferencesKey(name = "show_progress_bar")
        val SHOW_PREPARATION = booleanPreferencesKey(name = "show_preparation")
        val SHOW_REST = booleanPreferencesKey(name = "show_rest")
        val USER_SOUND = stringPreferencesKey(name = "user_sound")
    }
}