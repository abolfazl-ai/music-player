package com.example.compose.local.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.compose.local.model.Album
import com.example.compose.local.model.AlbumWithSongs
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao : SortInterface<Album> {

    @Query("SELECT * FROM ALBUMS ORDER BY ALBUM_NAME ASC")
    override fun getByTitleASC(): Flow<List<Album>>

    @Query("SELECT * FROM ALBUMS ORDER BY ALBUM_NAME DESC")
    override fun getByTitleDESC(): Flow<List<Album>>

    @Query("SELECT * FROM ALBUMS ORDER BY ALBUM_ARTIST ASC")
    override fun getByArtistASC(): Flow<List<Album>>

    @Query("SELECT * FROM ALBUMS ORDER BY ALBUM_ARTIST DESC")
    override fun getByArtistDESC(): Flow<List<Album>>

    @Query("SELECT * FROM ALBUMS ORDER BY ALBUM_NAME ASC")
    override fun getByAlbumASC(): Flow<List<Album>>

    @Query("SELECT * FROM ALBUMS ORDER BY ALBUM_NAME DESC")
    override fun getByAlbumDESC(): Flow<List<Album>>

    @Query("SELECT * FROM ALBUMS ORDER BY ALBUM_NAME ASC")
    override fun getByFileSizeASC(): Flow<List<Album>>

    @Query("SELECT * FROM ALBUMS ORDER BY ALBUM_NAME DESC")
    override fun getByFileSizeDESC(): Flow<List<Album>>

    @Query("SELECT * FROM ALBUMS ORDER BY ALBUM_NAME ASC")
    override fun getByFolderPathASC(): Flow<List<Album>>

    @Query("SELECT * FROM ALBUMS ORDER BY ALBUM_NAME DESC")
    override fun getByFolderPathDESC(): Flow<List<Album>>

    @Query("SELECT * FROM ALBUMS ORDER BY ALBUM_TRACKS_NUMBER ASC")
    override fun getByDurationASC(): Flow<List<Album>>

    @Query("SELECT * FROM ALBUMS ORDER BY ALBUM_TRACKS_NUMBER DESC")
    override fun getByDurationDESC(): Flow<List<Album>>

    @Query("SELECT * FROM ALBUMS ORDER BY ALBUM_MODIFY_DATE ASC")
    override fun getByDateASC(): Flow<List<Album>>

    @Query("SELECT * FROM ALBUMS ORDER BY ALBUM_MODIFY_DATE DESC")
    override fun getByDateDESC(): Flow<List<Album>>

    @Query("SELECT * FROM ALBUMS ORDER BY ALBUM_TRACKS_NUMBER ASC")
    override fun getBySongsCountASC(): Flow<List<Album>>

    @Query("SELECT * FROM ALBUMS ORDER BY ALBUM_TRACKS_NUMBER DESC")
    override fun getBySongsCountDESC(): Flow<List<Album>>


    @Transaction
    @Query("SELECT * FROM albums ORDER BY ALBUM_NAME")
    fun getAlbumsWithSongs(): Flow<List<AlbumWithSongs>>

    @Transaction
    @Query("SELECT * FROM ALBUMS WHERE ALBUM_ID = :id")
    suspend fun getAlbumWithSongs(id: Long): AlbumWithSongs

    @Query("SELECT * FROM ALBUMS WHERE ALBUM_ID = :id")
    suspend fun getAlbumById(id: Long): Album

}