package com.example.compose.local.preferences

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private const val MUSIC_PREFERENCES_NAME = "user_preferences"
val Context.dataStore by preferencesDataStore(
    name = MUSIC_PREFERENCES_NAME,
    produceMigrations = { context ->
        listOf(SharedPreferencesMigration(context, MUSIC_PREFERENCES_NAME))
    }
)


class AppPreferences @Inject constructor(private val dataStore: DataStore<Preferences>) {

    private val TAG: String = "PreferencesRepo"

    private object PrefKeys {
        val SONGS_SORT_ORDER = stringPreferencesKey("songs_sort_order")
        val ARTISTS_SORT_ORDER = stringPreferencesKey("artists_sort_order")
        val ALBUMS_SORT_ORDER = stringPreferencesKey("albums_sort_order")
        val FOLDERS_SORT_ORDER = stringPreferencesKey("folders_sort_order")
        val PLAYLISTS_SORT_ORDER = stringPreferencesKey("playlists_sort_order")

        val PLAYING_STATE = stringPreferencesKey("playing_state")

        val IS_SCANNING = booleanPreferencesKey("is_scanning")

    }

    val playingStateFlow: Flow<PlayingState> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else throw exception
        }.map { preferences ->
            PlayingState.valueOf(preferences[PrefKeys.PLAYING_STATE] ?: PlayingState.PAUSE.name)
        }

    suspend fun updatePlayingState(state: PlayingState) = dataStore.edit { preferences ->
        preferences[PrefKeys.PLAYING_STATE] = state.name
    }


    val isScanning: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else throw exception
        }.map { preferences -> preferences[PrefKeys.IS_SCANNING] ?: false }

    suspend fun updateScanState(isScanning: Boolean) = dataStore.edit { preferences ->
        preferences[PrefKeys.IS_SCANNING] = isScanning
    }


    val sortOrdersFlow: Flow<SortOrders> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else throw exception
        }.map { preferences -> mapSortOrderPreferences(preferences) }


    suspend fun saveSongsSortOrder(order: SortOrder) = dataStore.edit { preferences ->
        preferences[PrefKeys.SONGS_SORT_ORDER] = order.name
    }

    suspend fun saveArtistsSortOrder(order: SortOrder) = dataStore.edit { preferences ->
        preferences[PrefKeys.ARTISTS_SORT_ORDER] = order.name
    }

    suspend fun saveAlbumsSortOrder(order: SortOrder) = dataStore.edit { preferences ->
        preferences[PrefKeys.ALBUMS_SORT_ORDER] = order.name
    }

    suspend fun saveFoldersSortOrder(order: SortOrder) = dataStore.edit { preferences ->
        preferences[PrefKeys.FOLDERS_SORT_ORDER] = order.name
    }

    suspend fun savePlaylistsSortOrder(order: SortOrder) = dataStore.edit { preferences ->
        preferences[PrefKeys.PLAYLISTS_SORT_ORDER] = order.name
    }


    private fun mapSortOrderPreferences(preferences: Preferences): SortOrders {

        val songOrder =
            SortOrder.valueOf(preferences[PrefKeys.SONGS_SORT_ORDER] ?: SortOrder.TitleASC.name)
        val artistsOrder =
            SortOrder.valueOf(preferences[PrefKeys.ARTISTS_SORT_ORDER] ?: SortOrder.TitleASC.name)
        val albumsOrder =
            SortOrder.valueOf(preferences[PrefKeys.ALBUMS_SORT_ORDER] ?: SortOrder.TitleASC.name)
        val foldersOrder =
            SortOrder.valueOf(preferences[PrefKeys.FOLDERS_SORT_ORDER] ?: SortOrder.TitleASC.name)
        val playlistsOrder =
            SortOrder.valueOf(preferences[PrefKeys.PLAYLISTS_SORT_ORDER] ?: SortOrder.TitleASC.name)

        return SortOrders(songOrder, artistsOrder, albumsOrder, foldersOrder, playlistsOrder)
    }
}