package com.example.compose.local.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.compose.local.model.Folder
import com.example.compose.local.model.FolderWithSongs
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDao {

    @Query("SELECT FOLDER_PATH, FOLDER_TITLE, COUNT(SONGS.SONG_ID) AS FOLDER_TRACKS_NUMBER, SUM(SONGS.SONG_DURATION) AS FOLDER_TOTAL_DURATION FROM FOLDERS FOLDERS LEFT JOIN SONGS SONGS ON SONGS.SONG_FOLDER_PATH = FOLDERS.FOLDER_PATH GROUP BY FOLDER_PATH, FOLDER_TITLE")
    fun getFolders(): Flow<List<Folder>>

    @Transaction
    @Query("SELECT * FROM folders")
    fun getFoldersWithSongs(): Flow<List<FolderWithSongs>>

    @Query("SELECT FOLDER_PATH, FOLDER_TITLE, COUNT(SONGS.SONG_ID) AS FOLDER_TRACKS_NUMBER, SUM(SONGS.SONG_DURATION) AS FOLDER_TOTAL_DURATION FROM FOLDERS FOLDERS LEFT JOIN SONGS SONGS ON SONGS.SONG_FOLDER_PATH = FOLDERS.FOLDER_PATH WHERE FOLDER_PATH = :path")
    suspend fun getFolderByPath(path: String): Folder

}