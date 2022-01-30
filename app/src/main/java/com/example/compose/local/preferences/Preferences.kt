package com.example.compose.local

import android.content.Context
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.preferencesDataStore

private const val MUSIC_PREFERENCES_NAME = "user_preferences"
val Context.dataStore by preferencesDataStore(
    name = MUSIC_PREFERENCES_NAME,
    produceMigrations = { context ->
        listOf(SharedPreferencesMigration(context, MUSIC_PREFERENCES_NAME))
    }
)