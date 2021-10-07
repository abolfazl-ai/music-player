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
) {
    sealed class Sort(val sort: String, val asc: Boolean) {
        object TitleASC : Sort("SONG_TITLE", true)
        object TitleDESC : Sort("SONG_TITLE", false)
        object ArtistASC : Sort("SONG_ARTIST_ID", true)
        object ArtistDESC : Sort("SONG_ARTIST_ID", false)
        object AlbumASC : Sort("SONG_ALBUM_ID", true)
        object AlbumDESC : Sort("SONG_ALBUM_ID", false)
        object SizeASC : Sort("SONG_FILE_SIZE", true)
        object SizeDESC : Sort("SONG_FILE_SIZE", false)
        object FolderASC : Sort("SONG_FOLDER_PATH", true)
        object FolderDESC : Sort("SONG_FOLDER_PATH", false)
        object DurationASC : Sort("SONG_DURATION", true)
        object DurationDESC : Sort("SONG_DURATION", false)
        object ModifyDateASC : Sort("SONG_MODIFY_DATE", true)
        object ModifyDateDESC : Sort("SONG_MODIFY_DATE", false)
    }
}
