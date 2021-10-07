package com.example.compose.local.model

import androidx.room.*

@Entity(tableName = "ARTISTS")
data class Artist(
    @PrimaryKey @ColumnInfo(name = "ARTIST_ID") val id: Long,
    @ColumnInfo(name = "ARTIST_NAME") val name: String,
    @ColumnInfo(name = "ARTIST_ALBUMS_NUMBER") val albumsNumber: Int = 0,
    @ColumnInfo(name = "ARTIST_TRACKS_NUMBER") val tracksNumber: Int = 0,
    @ColumnInfo(name = "ARTIST_MODIFY_DATE") val modifyDate: Long
)

data class ArtistWithAlbumsAndSongs(
    @Embedded val artist: Artist,
    @Relation(
        parentColumn = "ARTIST_ID",
        entityColumn = "ALBUM_ARTIST_ID",
    )
    val albums: List<Album>,
    @Relation(
        parentColumn = "ARTIST_ID",
        entityColumn = "SONG_ARTIST_ID",
    )
    val songs: List<Song>
)