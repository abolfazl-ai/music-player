package com.example.compose.local.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.compose.local.model.Artist
import com.example.compose.local.model.ArtistWithAlbumsAndSongs
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtistDao {

    @Query("SELECT * FROM ARTISTS ORDER BY ARTIST_NAME")
    fun getArtists(): Flow<List<Artist>>

    @Transaction
    @Query("SELECT * FROM ARTISTS ORDER BY ARTIST_NAME")
    fun getArtistsWithAlbumsAndSongs(): Flow<List<ArtistWithAlbumsAndSongs>>

    @Query("SELECT * FROM ARTISTS WHERE ARTIST_ID = :id")
    suspend fun getArtistById(id: Long): Artist

    @Transaction
    @Query("SELECT * FROM ARTISTS WHERE ARTIST_ID = :id")
    suspend fun getArtistWithAlbumsAndSongs(id: Long): ArtistWithAlbumsAndSongs

}