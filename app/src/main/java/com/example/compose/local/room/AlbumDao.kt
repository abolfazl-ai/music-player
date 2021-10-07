package com.example.compose.local.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.compose.local.model.Album
import com.example.compose.local.model.AlbumWithSongs
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {

    @Query("SELECT * FROM ALBUMS ORDER BY ALBUM_NAME")
    fun getAlbums(): Flow<List<Album>>

    @Transaction
    @Query("SELECT * FROM albums ORDER BY ALBUM_NAME")
    fun getAlbumsWithSongs(): Flow<List<AlbumWithSongs>>

    @Transaction
    @Query("SELECT * FROM ALBUMS WHERE ALBUM_ID = :id")
    suspend fun getAlbumWithSongs(id: Long): AlbumWithSongs

    @Query("SELECT * FROM ALBUMS WHERE ALBUM_ID = :id")
    suspend fun getAlbumById(id: Long): Album

}