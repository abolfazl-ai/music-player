package com.example.compose.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.example.compose.local.model.Song
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao : SortInterface<Song> {

    @Query("SELECT * FROM SONGS ORDER BY SONG_TITLE ASC")
    override fun getByTitleASC(): Flow<List<Song>>

    @Query("SELECT * FROM SONGS ORDER BY SONG_TITLE DESC")
    override fun getByTitleDESC(): Flow<List<Song>>

    @Query("SELECT * FROM SONGS ORDER BY SONG_ARTIST ASC")
    override fun getByArtistASC(): Flow<List<Song>>

    @Query("SELECT * FROM SONGS ORDER BY SONG_ARTIST DESC")
    override fun getByArtistDESC(): Flow<List<Song>>

    @Query("SELECT * FROM SONGS ORDER BY SONG_ALBUM ASC")
    override fun getByAlbumASC(): Flow<List<Song>>

    @Query("SELECT * FROM SONGS ORDER BY SONG_ALBUM DESC")
    override fun getByAlbumDESC(): Flow<List<Song>>

    @Query("SELECT * FROM SONGS ORDER BY SONG_FILE_SIZE ASC")
    override fun getByFileSizeASC(): Flow<List<Song>>

    @Query("SELECT * FROM SONGS ORDER BY SONG_FILE_SIZE DESC")
    override fun getByFileSizeDESC(): Flow<List<Song>>

    @Query("SELECT * FROM SONGS ORDER BY SONG_FOLDER_PATH ASC")
    override fun getByFolderPathASC(): Flow<List<Song>>

    @Query("SELECT * FROM SONGS ORDER BY SONG_FOLDER_PATH DESC")
    override fun getByFolderPathDESC(): Flow<List<Song>>

    @Query("SELECT * FROM SONGS ORDER BY SONG_DURATION ASC")
    override fun getByDurationASC(): Flow<List<Song>>

    @Query("SELECT * FROM SONGS ORDER BY SONG_DURATION DESC")
    override fun getByDurationDESC(): Flow<List<Song>>

    @Query("SELECT * FROM SONGS ORDER BY SONG_MODIFY_DATE ASC")
    override fun getByDateASC(): Flow<List<Song>>

    @Query("SELECT * FROM SONGS ORDER BY SONG_MODIFY_DATE DESC")
    override fun getByDateDESC(): Flow<List<Song>>

    @Query("SELECT * FROM SONGS ORDER BY SONG_TITLE ASC")
    override fun getBySongsCountASC(): Flow<List<Song>>

    @Query("SELECT * FROM SONGS ORDER BY SONG_TITLE DESC")
    override fun getBySongsCountDESC(): Flow<List<Song>>


    @Query("SELECT * FROM SONGS WHERE SONG_ID = :id")
    suspend fun getSongById(id: Long): Song

    @Query("SELECT * FROM SONGS WHERE SONG_PATH = :path")
    suspend fun getSongByPath(path: String): Song

    @Delete
    suspend fun deleteSong(song: Song)

}