package com.example.compose.viewmodel

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.compose.local.MusicScannerWorker
import com.example.compose.local.dataStore
import com.example.compose.local.model.Album
import com.example.compose.local.model.Artist
import com.example.compose.local.model.Folder
import com.example.compose.local.model.Song
import com.example.compose.local.preferences.PreferencesRepository
import com.example.compose.local.preferences.SortOrder
import com.example.compose.local.preferences.SortOrders
import com.example.compose.local.room.DataBase
import com.example.compose.local.room.getBySortOrder
import kotlinx.coroutines.flow.*

class Repository(private val context: Context, private val sortOrders: Flow<SortOrders>) {

    private val dataBase = DataBase.getDataBase(context.applicationContext)
    private val songDao = dataBase.songDao
    private val folderDao = dataBase.folderDao
    private val artistDao = dataBase.artistDao
    private val albumDao = dataBase.albumDao
    private val playlistDao = dataBase.playlistDao

    private val workManager by lazy { WorkManager.getInstance(context.applicationContext) }

    fun scan() {
        val request = OneTimeWorkRequestBuilder<MusicScannerWorker>().build()
        workManager.enqueueUniqueWork("MusicScan", ExistingWorkPolicy.KEEP, request)
    }

    // Song Repository
    suspend fun getSongById(id: Long) = songDao.getSongById(id)
    suspend fun getSongByPath(path: String) = songDao.getSongByPath(path)
    suspend fun deleteSong(song: Song) = songDao.deleteSong(song)
    val allSongs: Flow<List<Song>> = sortOrders.flatMapMerge {
        getBySortOrder(songDao, it.songsOrder)
    }

    // Folder Repository
    fun getFoldersWithSongs() = folderDao.getFoldersWithSongs()
    suspend fun getFolderByPath(path: String) = folderDao.getFolderByPath(path)
    val allFolders: Flow<List<Folder>> = sortOrders.flatMapMerge {
        getBySortOrder(folderDao, it.foldersOrder)
    }

    // Artist Repository
    fun getArtistsWithAlbumsAndSongs() = artistDao.getArtistsWithAlbumsAndSongs()
    suspend fun getArtistById(id: Long) = artistDao.getArtistById(id)
    val allArtists: Flow<List<Artist>> = sortOrders.flatMapMerge {
        getBySortOrder(artistDao, it.artistsOrder)
    }

    // Album Repository
    fun getAlbumsWithSongs() = albumDao.getAlbumsWithSongs()
    suspend fun getAlbumWithSongs(id: Long) = albumDao.getAlbumWithSongs(id)
    suspend fun getAlbumById(id: Long) = albumDao.getAlbumById(id)
    val allAlbums: Flow<List<Album>> = sortOrders.flatMapMerge {
        getBySortOrder(albumDao, it.albumsOrder)
    }

    // Playlist Repository
//    val allPlaylists = playlistDao.getPlaylists()
    fun getPlaylistWithItems(id: Int) = playlistDao.getPlaylistWithItems(id)
    fun getPlaylistsWithItems() = playlistDao.getPlaylistsWithItems()

}