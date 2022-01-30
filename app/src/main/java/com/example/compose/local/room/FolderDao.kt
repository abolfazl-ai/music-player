package com.example.compose.local.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.compose.local.model.Folder
import com.example.compose.local.model.FolderWithSongs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Dao
interface FolderDao : SortInterface<Folder> {

    @Query(
        "SELECT FOLDER_PATH, FOLDER_TITLE, COUNT(SONGS.SONG_ID) AS FOLDER_TRACKS_NUMBER, SUM(SONGS.SONG_DURATION) AS FOLDER_TOTAL_DURATION FROM FOLDERS FOLDERS LEFT JOIN SONGS SONGS ON SONGS.SONG_FOLDER_PATH = FOLDERS.FOLDER_PATH " +
                "GROUP BY FOLDER_PATH, FOLDER_TITLE ORDER BY FOLDER_TITLE ASC"
    )
    override fun getByTitleASC(): Flow<List<Folder>>

    @Query(
        "SELECT FOLDER_PATH, FOLDER_TITLE, COUNT(SONGS.SONG_ID) AS FOLDER_TRACKS_NUMBER, SUM(SONGS.SONG_DURATION) AS FOLDER_TOTAL_DURATION FROM FOLDERS FOLDERS LEFT JOIN SONGS SONGS ON SONGS.SONG_FOLDER_PATH = FOLDERS.FOLDER_PATH " +
                "GROUP BY FOLDER_PATH, FOLDER_TITLE ORDER BY FOLDER_TITLE DESC"
    )
    override fun getByTitleDESC(): Flow<List<Folder>>

    override fun getByArtistASC(): Flow<List<Folder>> = emptyFlow()

    override fun getByArtistDESC(): Flow<List<Folder>> = emptyFlow()

    override fun getByAlbumASC(): Flow<List<Folder>> = emptyFlow()

    override fun getByAlbumDESC(): Flow<List<Folder>> = emptyFlow()

    override fun getByFileSizeASC(): Flow<List<Folder>> = emptyFlow()

    override fun getByFileSizeDESC(): Flow<List<Folder>> = emptyFlow()

    @Query(
        "SELECT FOLDER_PATH, FOLDER_TITLE, COUNT(SONGS.SONG_ID) AS FOLDER_TRACKS_NUMBER, SUM(SONGS.SONG_DURATION) AS FOLDER_TOTAL_DURATION FROM FOLDERS FOLDERS LEFT JOIN SONGS SONGS ON SONGS.SONG_FOLDER_PATH = FOLDERS.FOLDER_PATH " +
                "GROUP BY FOLDER_PATH, FOLDER_TITLE ORDER BY FOLDER_PATH ASC"
    )
    override fun getByFolderPathASC(): Flow<List<Folder>>

    @Query(
        "SELECT FOLDER_PATH, FOLDER_TITLE, COUNT(SONGS.SONG_ID) AS FOLDER_TRACKS_NUMBER, SUM(SONGS.SONG_DURATION) AS FOLDER_TOTAL_DURATION FROM FOLDERS FOLDERS LEFT JOIN SONGS SONGS ON SONGS.SONG_FOLDER_PATH = FOLDERS.FOLDER_PATH " +
                "GROUP BY FOLDER_PATH, FOLDER_TITLE ORDER BY FOLDER_PATH DESC"
    )
    override fun getByFolderPathDESC(): Flow<List<Folder>>

    @Query(
        "SELECT FOLDER_PATH, FOLDER_TITLE, COUNT(SONGS.SONG_ID) AS FOLDER_TRACKS_NUMBER, SUM(SONGS.SONG_DURATION) AS FOLDER_TOTAL_DURATION FROM FOLDERS FOLDERS LEFT JOIN SONGS SONGS ON SONGS.SONG_FOLDER_PATH = FOLDERS.FOLDER_PATH " +
                "GROUP BY FOLDER_PATH, FOLDER_TITLE ORDER BY FOLDER_TOTAL_DURATION ASC"
    )
    override fun getByDurationASC(): Flow<List<Folder>>

    @Query(
        "SELECT FOLDER_PATH, FOLDER_TITLE, COUNT(SONGS.SONG_ID) AS FOLDER_TRACKS_NUMBER, SUM(SONGS.SONG_DURATION) AS FOLDER_TOTAL_DURATION FROM FOLDERS FOLDERS LEFT JOIN SONGS SONGS ON SONGS.SONG_FOLDER_PATH = FOLDERS.FOLDER_PATH " +
                "GROUP BY FOLDER_PATH, FOLDER_TITLE ORDER BY FOLDER_TOTAL_DURATION DESC"
    )
    override fun getByDurationDESC(): Flow<List<Folder>>

    @Query(
        "SELECT FOLDER_PATH, FOLDER_TITLE, COUNT(SONGS.SONG_ID) AS FOLDER_TRACKS_NUMBER, SUM(SONGS.SONG_DURATION) AS FOLDER_TOTAL_DURATION FROM FOLDERS FOLDERS LEFT JOIN SONGS SONGS ON SONGS.SONG_FOLDER_PATH = FOLDERS.FOLDER_PATH " +
                "GROUP BY FOLDER_PATH, FOLDER_TITLE ORDER BY SONG_MODIFY_DATE ASC"
    )
    override fun getByDateASC(): Flow<List<Folder>>

    @Query(
        "SELECT FOLDER_PATH, FOLDER_TITLE, COUNT(SONGS.SONG_ID) AS FOLDER_TRACKS_NUMBER, SUM(SONGS.SONG_DURATION) AS FOLDER_TOTAL_DURATION FROM FOLDERS FOLDERS LEFT JOIN SONGS SONGS ON SONGS.SONG_FOLDER_PATH = FOLDERS.FOLDER_PATH " +
                "GROUP BY FOLDER_PATH, FOLDER_TITLE ORDER BY SONG_MODIFY_DATE DESC"
    )
    override fun getByDateDESC(): Flow<List<Folder>>

    @Query(
        "SELECT FOLDER_PATH, FOLDER_TITLE, COUNT(SONGS.SONG_ID) AS FOLDER_TRACKS_NUMBER, SUM(SONGS.SONG_DURATION) AS FOLDER_TOTAL_DURATION FROM FOLDERS FOLDERS LEFT JOIN SONGS SONGS ON SONGS.SONG_FOLDER_PATH = FOLDERS.FOLDER_PATH " +
                "GROUP BY FOLDER_PATH, FOLDER_TITLE ORDER BY FOLDER_TRACKS_NUMBER ASC"
    )
    override fun getBySongsCountASC(): Flow<List<Folder>>

    @Query(
        "SELECT FOLDER_PATH, FOLDER_TITLE, COUNT(SONGS.SONG_ID) AS FOLDER_TRACKS_NUMBER, SUM(SONGS.SONG_DURATION) AS FOLDER_TOTAL_DURATION FROM FOLDERS FOLDERS LEFT JOIN SONGS SONGS ON SONGS.SONG_FOLDER_PATH = FOLDERS.FOLDER_PATH " +
                "GROUP BY FOLDER_PATH, FOLDER_TITLE ORDER BY FOLDER_TRACKS_NUMBER DESC"
    )
    override fun getBySongsCountDESC(): Flow<List<Folder>>


    @Transaction
    @Query("SELECT * FROM folders")
    fun getFoldersWithSongs(): Flow<List<FolderWithSongs>>

    @Query("SELECT FOLDER_PATH, FOLDER_TITLE, COUNT(SONGS.SONG_ID) AS FOLDER_TRACKS_NUMBER, SUM(SONGS.SONG_DURATION) AS FOLDER_TOTAL_DURATION FROM FOLDERS FOLDERS LEFT JOIN SONGS SONGS ON SONGS.SONG_FOLDER_PATH = FOLDERS.FOLDER_PATH WHERE FOLDER_PATH = :path")
    suspend fun getFolderByPath(path: String): Folder
}