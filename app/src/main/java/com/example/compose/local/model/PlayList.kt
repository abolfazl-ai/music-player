package com.example.compose.local.model

import androidx.room.*

@Entity(tableName = "PLAYLISTS")
data class PlayList(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "ID") val id: Int,
    @ColumnInfo(name = "NAME") val name: String,
    @ColumnInfo(name = "TRACKS_NUMBER") val tracksNumber: Int = 0,
    @ColumnInfo(name = "IS_CURRENT") val isCurrent: Boolean = false
)

@Entity(tableName = "PLAYLIST_ITEM", primaryKeys = ["INDEX", "PLAYLIST_ID"])
data class PlaylistItem(
    @ColumnInfo(name = "INDEX") val index: Int,
    @ColumnInfo(name = "PLAYLIST_ID") val playlistId: Int,
    @Embedded val song: Song
)

data class PlaylistWithItems(
    @Embedded val playlist: PlayList,
    @Relation(
        parentColumn = "ID",
        entityColumn = "PLAYLIST_ID",
    )
    val items: List<PlaylistItem>
)