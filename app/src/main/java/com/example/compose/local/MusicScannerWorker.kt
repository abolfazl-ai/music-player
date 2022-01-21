package com.example.compose.local

import android.content.Context
import android.icu.util.Calendar
import android.os.Build
import android.provider.MediaStore
import android.provider.MediaStore.Audio.*
import android.util.Log
import androidx.core.database.getStringOrNull
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.compose.local.model.Album
import com.example.compose.local.model.Artist
import com.example.compose.local.model.Folder
import com.example.compose.local.model.Song
import com.example.compose.local.room.DataBase
import kotlin.system.measureTimeMillis

class MusicScannerWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    private val dao by lazy { DataBase.getDataBase(context.applicationContext).modificationDao }
    private val date = Calendar.getInstance().timeInMillis

    override suspend fun doWork(): Result {
        measureTimeMillis {
//            if (MediaStore.getVersion(applicationContext)=="")
            querySongs()
            queryAlbums()
            queryArtists()
        }.also { Log.d("Abolfazl", "Scan finished in $it ms") }
        return Result.success()

/*        val state = Environment.getExternalStorageState()
        return if (state == Environment.MEDIA_MOUNTED || state == Environment.MEDIA_MOUNTED_READ_ONLY) {
            measureTimeMillis {
                scanFolder(Environment.getExternalStorageDirectory())
                removeDeletedItems()
            }.also { Log.d("AbolfazlDoWork", "Scan finished in $it ms") }
            Result.success()
        } else Result.failure()*/
    }

/*
    private suspend fun removeDeletedItems() = coroutineScope {
        val oldSongs = ArrayList<Song>()
        dao.getSongsToModify().map {
            async {
                if (!File(it.path).exists()) {
                    oldSongs.add(it)
                }
            }
        }.awaitAll()
        dao.deleteSongs(oldSongs)
    }

    private suspend fun scanFolder(dir: File) {

        if (!File(dir.absolutePath + "/.nomedia").exists()) {

            coroutineScope {

                val files = ArrayList<File>()

                dir.listFiles()?.forEach {
                    when {
                        it.isDirectory -> launch { scanFolder(it) }
                        it.isFile -> if (it.path.endsWith("mp3", ignoreCase = true)) files.add(it)
                    }
                }

                if (files.isNotEmpty()) {

                    val folder = Folder(dir.path, dir.name)

//                    withContext(Dispatchers.IO) {
                    with(files.map { songFile ->
                        async { scanSongAsync(songFile, folder) }
                    }.awaitAll()) {
                        dao.apply {
                            insertFolder(folder)
                            insertSongs(map { it.song })
                            insertAlbums(map { it.album })
                            insertArtists(map { it.artists }.flatten())
                            insertArtistsWithSong(map { it.artistAndSong }.flatten())
//                            }
                        }
                    }
                }
            }
        }
    }

    private fun scanSongAsync(songFile: File, folder: Folder): ScanResponse {

        val mediaExtractor = MediaExtractor()
        val mediaMetadataRetriever = MediaMetadataRetriever()

        try {
            mediaExtractor.setDataSource(songFile.absolutePath)
            mediaMetadataRetriever.setDataSource(songFile.absolutePath)
        } catch (e: IOException) {
        }

        val fileName = songFile.name
        val fileSize = songFile.length() / 1048576f
        var format: String
        val channel: Int
        val sampleRate: Int
        val bitRate: Int
        val duration: Long
        val title: String
        val artistName: String
        val albumName: String
        val albumArtist: String
        val genre: String
        val year: String
        val composer: String
        val trackNumber: String

        with(mediaExtractor.getTrackFormat(0)) {
            format = getString(KEY_MIME) ?: "Unknown"
            channel = getInteger(KEY_CHANNEL_COUNT)
            sampleRate = getInteger(KEY_SAMPLE_RATE)
            bitRate = getInteger(KEY_BIT_RATE)
            duration = getLong(KEY_DURATION)
        }
        with(mediaMetadataRetriever) {
            title =
                (extractMetadata(METADATA_KEY_TITLE)
                    ?: fileName).trim()
            artistName = (extractMetadata(METADATA_KEY_ARTIST)
                ?: "Unknown").trim()
            albumName =
                (extractMetadata(METADATA_KEY_ALBUM)
                    ?: "Single").trim()
            albumArtist =
                (extractMetadata(METADATA_KEY_ALBUMARTIST)
                    ?: "Unknown").trim()
            genre = (extractMetadata(METADATA_KEY_GENRE)
                ?: "Unknown Genre").trim()
            year =
                (extractMetadata(METADATA_KEY_YEAR) ?: "0").trim()
            composer = extractMetadata(METADATA_KEY_COMPOSER) ?: ""
            trackNumber =
                (extractMetadata(METADATA_KEY_CD_TRACK_NUMBER)
                    ?: "0").trim()
        }

        val artists = with(artistName) {
            when {
                contains(".Ft", true) -> split(
                    ".Ft",
                    ignoreCase = true
                )
                contains("Ft", true) -> split(
                    "Ft",
                    ignoreCase = true
                )
                contains(';', true) -> split(';', ignoreCase = true)
                else -> arrayListOf(this)
            }
        }.map { Artist(name = it) }

        val album = Album(albumName, albumArtist)

        val song = Song(
            title = title,
            album = album.name,
            artist = artistName,
            albumArtist = album.artist,
            composer = composer,
            folderPath = folder.path,
            path = songFile.path,
            albumId = album.id,
            duration = duration,
            genre = genre,
            fileSize = fileSize,
            format = format,
            year = year,
            trackNumber = trackNumber,
            channel = channel,
            bitRate = bitRate,
            sampleRate = sampleRate,
            fileName = fileName,
            modifyDate = date
        )

        mediaMetadataRetriever.release()
        mediaExtractor.release()

        return ScanResponse(song, album, artists, artists.map { ArtistAndSong(it.name, song.id) })
    }

    internal data class ScanResponse(
        val song: Song,
        val album: Album,
        val artists: List<Artist>,
        val artistAndSong: List<ArtistAndSong>
    )
*/

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
                        val title = (cursor.getStringOrNull(columns[Media.TITLE]!!) ?: fileName).trim()
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
