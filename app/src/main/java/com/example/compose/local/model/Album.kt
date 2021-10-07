package com.example.compose.local.model

import androidx.room.*

@Entity(tableName = "ALBUMS")
data class Album(
    @PrimaryKey @ColumnInfo(name = "ALBUM_ID") val id: Long,
    @ColumnInfo(name = "ALBUM_NAME") val name: String,
    @ColumnInfo(name = "ALBUM_ARTIST_ID") val artistId: Long,
    @ColumnInfo(name = "ALBUM_ARTIST") val artist: String,
    @ColumnInfo(name = "ALBUM_TRACKS_NUMBER") val tracksNumber: Int = 0,
    @ColumnInfo(name = "ALBUM_YEAR") val year: Int = 0,
    @ColumnInfo(name = "ALBUM_MODIFY_DATE") val modifyDate: Long
)

data class AlbumWithSongs(
    @Embedded val album: Album,
    @Relation(parentColumn = "ALBUM_ID", entityColumn = "SONG_ALBUM_ID")
    val songs: List<Song>
)