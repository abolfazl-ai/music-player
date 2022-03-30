package com.example.compose.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SONGS")
data class Song(
    @PrimaryKey @ColumnInfo(name = "SONG_ID") val id: Long,
    @ColumnInfo(name = "SONG_TITLE") val title: String,
    @ColumnInfo(name = "SONG_ALBUM") val album: String,
    @ColumnInfo(name = "SONG_ALBUM_ID") val albumId: Long,
    @ColumnInfo(name = "SONG_ALBUM_ARTIST") val albumArtist: String,
    @ColumnInfo(name = "SONG_ARTIST") val artist: String,
    @ColumnInfo(name = "SONG_ARTIST_ID") val artistId: Long,
    @ColumnInfo(name = "SONG_PATH") val path: String,
    @ColumnInfo(name = "SONG_FILE_NAME") val fileName: String,
    @ColumnInfo(name = "SONG_FILE_SIZE") val fileSize: Float,
    @ColumnInfo(name = "SONG_FOLDER_PATH") val folderPath: String,
    @ColumnInfo(name = "SONG_DURATION") val duration: Long,
    @ColumnInfo(name = "SONG_COMPOSER") val composer: String,
    @ColumnInfo(name = "SONG_MIME_TYPE") val mimeType: String,
    @ColumnInfo(name = "SONG_YEAR") val year: Int,
    @ColumnInfo(name = "SONG_TRACK_NUMBER") val trackNumber: Int,
    @ColumnInfo(name = "SONG_MODIFY_DATE") val modifyDate: Long
)