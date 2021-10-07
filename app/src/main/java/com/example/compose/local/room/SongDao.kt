package com.example.compose.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.example.compose.local.model.Song
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {

    @Query(
        "SELECT * FROM SONGS ORDER BY " +
                "CASE WHEN :sort = 'SONG_TITLE' AND :asc = 1 THEN SONG_TITLE END COLLATE NOCASE ASC, " +
                "CASE WHEN :sort = 'SONG_TITLE' AND :asc = 0 THEN SONG_TITLE END COLLATE NOCASE DESC, " +
                "CASE WHEN :sort = 'SONG_ARTIST_ID' AND :asc = 1 THEN SONG_ARTIST_ID END COLLATE NOCASE ASC, " +
                "CASE WHEN :sort = 'SONG_ARTIST_ID' AND :asc = 0 THEN SONG_ARTIST_ID END COLLATE NOCASE DESC, " +
                "CASE WHEN :sort = 'SONG_ALBUM_ID' AND :asc = 1 THEN SONG_ALBUM_ID END COLLATE NOCASE ASC, " +
                "CASE WHEN :sort = 'SONG_ALBUM_ID' AND :asc = 0 THEN SONG_ALBUM_ID END COLLATE NOCASE DESC, " +
                "CASE WHEN :sort = 'SONG_FILE_SIZE' AND :asc = 1 THEN SONG_FILE_SIZE END COLLATE NOCASE ASC, " +
                "CASE WHEN :sort = 'SONG_FILE_SIZE' AND :asc = 0 THEN SONG_FILE_SIZE END COLLATE NOCASE DESC, " +
                "CASE WHEN :sort = 'SONG_FOLDER_PATH' AND :asc = 1 THEN SONG_FOLDER_PATH END COLLATE NOCASE ASC, " +
                "CASE WHEN :sort = 'SONG_FOLDER_PATH' AND :asc = 0 THEN SONG_FOLDER_PATH END COLLATE NOCASE DESC, " +
                "CASE WHEN :sort = 'SONG_DURATION' AND :asc = 1 THEN SONG_DURATION END COLLATE NOCASE ASC, " +
                "CASE WHEN :sort = 'SONG_DURATION' AND :asc = 0 THEN SONG_DURATION END COLLATE NOCASE DESC, " +
                "CASE WHEN :sort = 'SONG_MODIFY_DATE' AND :asc = 1 THEN SONG_MODIFY_DATE END COLLATE NOCASE ASC, " +
                "CASE WHEN :sort = 'SONG_MODIFY_DATE' AND :asc = 0 THEN SONG_MODIFY_DATE END COLLATE NOCASE DESC"
    )
    fun getSongs(sort: String, asc: Boolean): Flow<List<Song>>

    @Query("SELECT * FROM SONGS WHERE SONG_ID = :id")
    suspend fun getSongById(id: Long): Song

    @Query("SELECT * FROM SONGS WHERE SONG_PATH = :path")
    suspend fun getSongByPath(path: String): Song

    @Delete
    suspend fun deleteSong(song: Song)

}