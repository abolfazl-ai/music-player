package com.example.compose.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.compose.local.MusicScannerWorker
import com.example.compose.local.model.Song
import com.example.compose.local.room.DataBase

class Repository(private val context: Context) {

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
    fun getSongs(sort: Song.Sort = Song.Sort.TitleASC) = songDao.getSongs(sort.sort, sort.asc)
    suspend fun getSongById(id: Long) = songDao.getSongById(id)
    suspend fun getSongByPath(path: String) = songDao.getSongByPath(path)
    suspend fun deleteSong(song: Song) = songDao.deleteSong(song)

    // Folder Repository
    val allFolders = folderDao.getFolders()
    fun getFoldersWithSongs() = folderDao.getFoldersWithSongs()
    suspend fun getFolderByPath(path: String) = folderDao.getFolderByPath(path)

    // Artist Repository
    val allArtists = artistDao.getArtists()
    fun getArtistsWithAlbumsAndSongs() = artistDao.getArtistsWithAlbumsAndSongs()
    suspend fun getArtistById(id: Long) = artistDao.getArtistById(id)

    // Album Repository
    val allAlbums = albumDao.getAlbums()
    fun getAlbumsWithSongs() = albumDao.getAlbumsWithSongs()
    suspend fun getAlbumWithSongs(id: Long) = albumDao.getAlbumWithSongs(id)
    suspend fun getAlbumById(id: Long) = albumDao.getAlbumById(id)

    // Playlist Repository
//    val allPlaylists = playlistDao.getPlaylists()
    fun getPlaylistWithItems(id: Int) = playlistDao.getPlaylistWithItems(id)
    fun getPlaylistsWithItems() = playlistDao.getPlaylistsWithItems()

}