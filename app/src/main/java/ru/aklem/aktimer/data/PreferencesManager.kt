package ru.aklem.aktimer.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "PreferencesManager"

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val dataStore = context.dataStore

    val showTitle = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.SHOW_TITLE] ?: true
        }

    suspend fun updateShowTitle(showTitle: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SHOW_TITLE] = showTitle
        }
    }

    private object PreferencesKeys {
        val SHOW_TITLE = booleanPreferencesKey(name = "show_title")
    }
}