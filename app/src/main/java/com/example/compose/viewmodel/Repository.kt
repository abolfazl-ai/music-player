package com.example.compose.viewmodel

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.compose.local.MusicScannerWorker
import com.example.compose.local.model.Album
import com.example.compose.local.model.Artist
import com.example.compose.local.model.Folder
import com.example.compose.local.model.Song
import com.example.compose.local.preferences.AppPreferences
import com.example.compose.local.room.DataBase
import com.example.compose.local.room.getBySortOrder
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import javax.inject.Inject

class Repository @Inject constructor(
    @ApplicationContext private val context: Context,
    dataBase: DataBase,
    val preferences: AppPreferences,
) {

    private val songDao = dataBase.songDao
    private val folderDao = dataBase.folderDao
    private val artistDao = dataBase.artistDao
    private val albumDao = dataBase.albumDao
    private val playlistDao = dataBase.playlistDao

    suspend fun scan() {
        preferences.updateScanState(true)
        val request = OneTimeWorkRequestBuilder<MusicScannerWorker>().build()
        WorkManager.getInstance(context)
            .enqueueUniqueWork("MusicScan", ExistingWorkPolicy.KEEP, request)
    }

    // Song Repository
    suspend fun getSongById(id: Long) = songDao.getSongById(id)
    suspend fun getSongByPath(path: String) = songDao.getSongByPath(path)
    fun getSongsByFolderPath(folderPath: String) = songDao.getSongByFolderPath(folderPath)
    suspend fun deleteSong(song: Song) = songDao.deleteSong(song)
    val allSongs: Flow<List<Song>> = preferences.sortOrdersFlow.flatMapMerge {
        getBySortOrder(songDao, it.songsOrder)
    }

    // Folder Repository
    fun getFoldersWithSongs() = folderDao.getFoldersWithSongs()
    suspend fun getFolderByPath(path: String) = folderDao.getFolderByPath(path)
    val allFolders: Flow<List<Folder>> = preferences.sortOrdersFlow.flatMapMerge {
        getBySortOrder(folderDao, it.foldersOrder)
    }

    // Artist Repository
    fun getArtistsWithAlbumsAndSongs() = artistDao.getArtistsWithAlbumsAndSongs()
    suspend fun getArtistById(id: Long) = artistDao.getArtistById(id)
    val allArtists: Flow<List<Artist>> = preferences.sortOrdersFlow.flatMapMerge {
        getBySortOrder(artistDao, it.artistsOrder)
    }

    // Album Repository
    fun getAlbumsWithSongs() = albumDao.getAlbumsWithSongs()
    suspend fun getAlbumWithSongs(id: Long) = albumDao.getAlbumWithSongs(id)
    suspend fun getAlbumById(id: Long) = albumDao.getAlbumById(id)
    val allAlbums: Flow<List<Album>> = preferences.sortOrdersFlow.flatMapMerge {
        getBySortOrder(albumDao, it.albumsOrder)
    }

    // Playlist Repository
//    val allPlaylists = playlistDao.getPlaylists()
    fun getPlaylistWithItems(id: Int) = playlistDao.getPlaylistWithItems(id)
    fun getPlaylistsWithItems() = playlistDao.getPlaylistsWithItems()

}