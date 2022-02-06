package com.example.compose.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val MUSIC_PREFERENCES_NAME = "user_preferences"
val Context.dataStore by preferencesDataStore(
    name = MUSIC_PREFERENCES_NAME,
    produceMigrations = { context ->
        listOf(SharedPreferencesMigration(context, MUSIC_PREFERENCES_NAME))
    }
)

class AppPreferences @Inject constructor(private val dataStore: DataStore<Preferences>) {

    private object PrefKeys {
        val SONGS_SORT_ORDER = stringPreferencesKey("songs_sort_order")
        val ARTISTS_SORT_ORDER = stringPreferencesKey("artists_sort_order")
        val ALBUMS_SORT_ORDER = stringPreferencesKey("albums_sort_order")
        val FOLDERS_SORT_ORDER = stringPreferencesKey("folders_sort_order")
        val PLAYLISTS_SORT_ORDER = stringPreferencesKey("playlists_sort_order")

        val PLAYING_STATE = booleanPreferencesKey("playing_state")
        val CURRENT_INDEX = intPreferencesKey("current_index")

        val IS_SCANNING = booleanPreferencesKey("is_scanning")

    }

    val playStateFlow: Flow<Boolean> = dataStore.data.catch { emit(emptyPreferences()) }.map { it[PrefKeys.PLAYING_STATE] ?: false }
    suspend fun updatePlayingState(isPlaying: Boolean) = dataStore.edit { it[PrefKeys.PLAYING_STATE] = isPlaying }

    val currentIndexFlow: Flow<Int> = dataStore.data.catch { emit(emptyPreferences()) }.map { it[PrefKeys.CURRENT_INDEX] ?: 0 }
    suspend fun updateCurrentIndex(index: Int) = dataStore.edit { it[PrefKeys.CURRENT_INDEX] = index }

    val isScanning: Flow<Boolean> = dataStore.data.catch { emit(emptyPreferences()) }.map { it[PrefKeys.IS_SCANNING] ?: false }
    suspend fun updateScanState(isScanning: Boolean) = dataStore.edit { it[PrefKeys.IS_SCANNING] = isScanning }


    val sortOrdersFlow: Flow<SortOrders> = dataStore.data.catch { emit(emptyPreferences()) }.map { mapSortOrderPreferences(it) }
    suspend fun saveSongsSortOrder(order: SortOrder) = dataStore.edit { it[PrefKeys.SONGS_SORT_ORDER] = order.name }
    suspend fun saveArtistsSortOrder(order: SortOrder) = dataStore.edit { it[PrefKeys.ARTISTS_SORT_ORDER] = order.name }
    suspend fun saveAlbumsSortOrder(order: SortOrder) = dataStore.edit { it[PrefKeys.ALBUMS_SORT_ORDER] = order.name }
    suspend fun saveFoldersSortOrder(order: SortOrder) = dataStore.edit { it[PrefKeys.FOLDERS_SORT_ORDER] = order.name }
    suspend fun savePlaylistsSortOrder(order: SortOrder) = dataStore.edit { it[PrefKeys.PLAYLISTS_SORT_ORDER] = order.name }

    private fun mapSortOrderPreferences(pref: Preferences): SortOrders {

        val songOrder = SortOrder.valueOf(pref[PrefKeys.SONGS_SORT_ORDER] ?: SortOrder.TitleASC.name)
        val artistsOrder = SortOrder.valueOf(pref[PrefKeys.ARTISTS_SORT_ORDER] ?: SortOrder.TitleASC.name)
        val albumsOrder = SortOrder.valueOf(pref[PrefKeys.ALBUMS_SORT_ORDER] ?: SortOrder.TitleASC.name)
        val foldersOrder = SortOrder.valueOf(pref[PrefKeys.FOLDERS_SORT_ORDER] ?: SortOrder.TitleASC.name)
        val playlistsOrder = SortOrder.valueOf(pref[PrefKeys.PLAYLISTS_SORT_ORDER] ?: SortOrder.TitleASC.name)

        return SortOrders(songOrder, artistsOrder, albumsOrder, foldersOrder, playlistsOrder)
    }
}