package com.example.compose.local.model

import androidx.room.*

@Entity(tableName = "FOLDERS")
data class Folder(
    @PrimaryKey @ColumnInfo(name = "FOLDER_PATH") val path: String,
    @ColumnInfo(name = "FOLDER_TITLE") val title: String,
    @ColumnInfo(name = "FOLDER_TRACKS_NUMBER") val tracksNumber: Int = 0
)

data class FolderWithSongs(
    @Embedded val folder: Folder,
    @Relation(parentColumn = "FOLDER_PATH", entityColumn = "SONG_FOLDER_PATH")
    val songs: List<Song>
)
