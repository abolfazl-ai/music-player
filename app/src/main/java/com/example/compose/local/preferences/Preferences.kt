package com.example.compose.local

import android.content.Context
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.preferencesDataStore

private const val MUSIC_PREFERENCES_NAME = "user_preferences"
val Context.dataStore by preferencesDataStore(
    name = MUSIC_PREFERENCES_NAME,
    produceMigrations = { context ->
        // Since we're migrating from SharedPreferences, add a migration based on the
        // SharedPreferences name
        listOf(SharedPreferencesMigration(context, MUSIC_PREFERENCES_NAME))
    }
)