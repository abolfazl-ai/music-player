package com.example.compose.local

import android.content.Context
import android.icu.util.Calendar
import android.os.Build
import android.provider.MediaStore
import android.provider.MediaStore.Audio.*
import android.util.Log
import androidx.core.database.getStringOrNull
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.compose.local.model.Album
import com.example.compose.local.model.Artist
import com.example.compose.local.model.Folder
import com.example.compose.local.model.Song
import com.example.compose.local.preferences.AppPreferences
import com.example.compose.local.room.DataBase
import com.example.compose.local.room.ModificationDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import javax.inject.Inject
import kotlin.system.measureTimeMillis

@HiltWorker
class MusicScannerWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    val dao: ModificationDao,
    val preferences: AppPreferences,
) : CoroutineWorker(context, params) {

    private val date = Calendar.getInstance().timeInMillis

    override suspend fun doWork(): Result {
        measureTimeMillis {
            querySongs()
            queryAlbums()
            queryArtists()
        }.also { Log.d("Abolfazl", "Scan finished in $it ms") }
        preferences.updateScanState(false)
        return Result.success()
    }

    private suspend fun querySongs() {

        val songs = ArrayList<Song>()
        val folders = ArrayList<Folder>()

        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        else Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            Media.IS_MUSIC,
            Media._ID,
            Media.DATA,
            Media.SIZE,
            Media.MIME_TYPE,
            Media.YEAR,
            Media.DURATION,
            Media.TITLE,
            Media.ALBUM,
            Media.ALBUM_ID,
            Media.ARTIST,
            Media.ARTIST_ID,
            Albums.ARTIST,
            Media.COMPOSER,
            Media.TRACK,
        )

        applicationContext.contentResolver.query(collection, projection, null, null, null)
            ?.use { cursor ->

                val columns = projection.associateWith { cursor.getColumnIndexOrThrow(it) }

                while (cursor.moveToNext()) {

                    val path = cursor.getStringOrNull(columns[Media.DATA]!!)
                    if (cursor.getInt(columns[Media.IS_MUSIC]!!) == 1 && path != null) {

                        val id = cursor.getLong(columns[Media._ID]!!)
                        val fileName = path.substring(path.lastIndexOf("/") + 1)
                        val fileSize = cursor.getInt(columns[Media.SIZE]!!) / 1048576f
                        val folderPath = path.substring(0, path.lastIndexOf("/"))
                        val folderName = folderPath.substring(folderPath.lastIndexOf("/") + 1)
                        val mimeType = cursor.getStringOrNull(columns[Media.MIME_TYPE]!!) ?: ""
                        val duration = cursor.getLong(columns[Media.DURATION]!!)
                        val year = cursor.getInt(columns[Media.YEAR]!!)
                        val composer = cursor.getStringOrNull(columns[Media.COMPOSER]!!) ?: ""
                        val trackNumber = cursor.getInt(columns[Media.TRACK]!!)
                        val title =
                            (cursor.getStringOrNull(columns[Media.TITLE]!!) ?: fileName).trim()
                        val artist = cursor.getStringOrNull(columns[Media.ARTIST]!!) ?: "Unknown"
                        val artistId = cursor.getLong(columns[Media.ARTIST_ID]!!)
                        val album = cursor.getStringOrNull(columns[Media.ALBUM]!!) ?: "Unknown"
                        val albumId = cursor.getLong(columns[Media.ALBUM_ID]!!)
                        val albumArtist = cursor.getStringOrNull(columns[Albums.ARTIST]!!) ?: artist

                        Folder(folderPath, folderName).also { folders.add(it) }

                        Song(
                            id,
                            title,
                            album,
                            albumId,
                            albumArtist,
                            artist,
                            artistId,
                            path,
                            fileName,
                            fileSize,
                            folderPath,
                            duration,
                            composer,
                            mimeType,
                            year,
                            trackNumber,
                            date
                        ).also { songs.add(it) }
                    }
                }
            }
        dao.insertSongs(songs)
        dao.insertFolders(folders)
    }

    private suspend fun queryAlbums() {

        val albums = ArrayList<Album>()

        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            Albums.getContentUri(MediaStore.VOLUME_EXTERNAL)
        else Albums.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            Albums._ID,
            Albums.ALBUM,
            Media.ARTIST_ID,
            Albums.ARTIST,
            Albums.NUMBER_OF_SONGS,
            Albums.FIRST_YEAR,
        )

        applicationContext.contentResolver.query(collection, projection, null, null, null)
            ?.use { cursor ->
                val columns = projection.map { it to cursor.getColumnIndexOrThrow(it) }.toMap()
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(columns[Albums._ID]!!)
                    val name = cursor.getStringOrNull(columns[Albums.ALBUM]!!) ?: "Unknown"
                    val artistId = cursor.getLong(columns[Media.ARTIST_ID]!!)
                    val artist = cursor.getStringOrNull(columns[Albums.ARTIST]!!) ?: "Unknown"
                    val tracksNumber = cursor.getInt(columns[Albums.NUMBER_OF_SONGS]!!)
                    val year = cursor.getInt(columns[Albums.FIRST_YEAR]!!)

                    Album(id, name, artistId, artist, tracksNumber, year, date)
                        .also { albums.add(it) }
                }
            }
        dao.insertAlbums(albums)
    }

    private suspend fun queryArtists() {

        val artists = ArrayList<Artist>()

        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            Artists.getContentUri(MediaStore.VOLUME_EXTERNAL)
        else Artists.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            Artists._ID,
            Artists.ARTIST,
            Artists.NUMBER_OF_ALBUMS,
            Artists.NUMBER_OF_TRACKS
        )

        applicationContext.contentResolver.query(collection, projection, null, null, null)
            ?.use { cursor ->
                val columns = projection.map { it to cursor.getColumnIndexOrThrow(it) }.toMap()
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(columns[Artists._ID]!!)
                    val name = cursor.getStringOrNull(columns[Artists.ARTIST]!!) ?: "Unknown"
                    val albumsNumber = cursor.getInt(columns[Artists.NUMBER_OF_ALBUMS]!!)
                    val tracksNumber = cursor.getInt(columns[Artists.NUMBER_OF_TRACKS]!!)

                    Artist(id, name, albumsNumber, tracksNumber, date).also { artists.add(it) }
                }
            }
        dao.insertArtists(artists)
    }

}
