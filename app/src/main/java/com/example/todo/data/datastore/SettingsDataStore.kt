package com.example.todo.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsDataStore(private val context: Context) {

    companion object {
        private val COMPLETED_COLOR_ENABLED = booleanPreferencesKey("completed_color_enabled")
        private val JSON_IMPORTED = booleanPreferencesKey("json_imported")
    }

    val completedColorEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[COMPLETED_COLOR_ENABLED] ?: false }

    val jsonImported: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[JSON_IMPORTED] ?: false }

    suspend fun setCompletedColorEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[COMPLETED_COLOR_ENABLED] = enabled
        }
    }

    suspend fun setJsonImported(imported: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[JSON_IMPORTED] = imported
        }
    }
}
