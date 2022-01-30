package com.example.compose.local.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.compose.local.model.Artist
import com.example.compose.local.model.ArtistWithAlbumsAndSongs
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtistDao : SortInterface<Artist> {

    @Query("SELECT * FROM ARTISTS ORDER BY ARTIST_NAME ASC")
    override fun getByTitleASC(): Flow<List<Artist>>

    @Query("SELECT * FROM ARTISTS ORDER BY ARTIST_NAME DESC")
    override fun getByTitleDESC(): Flow<List<Artist>>

    @Query("SELECT * FROM ARTISTS ORDER BY ARTIST_NAME ASC")
    override fun getByArtistASC(): Flow<List<Artist>>

    @Query("SELECT * FROM ARTISTS ORDER BY ARTIST_NAME DESC")
    override fun getByArtistDESC(): Flow<List<Artist>>

    @Query("SELECT * FROM ARTISTS ORDER BY ARTIST_NAME ASC")
    override fun getByAlbumASC(): Flow<List<Artist>>

    @Query("SELECT * FROM ARTISTS ORDER BY ARTIST_NAME DESC")
    override fun getByAlbumDESC(): Flow<List<Artist>>

    @Query("SELECT * FROM ARTISTS ORDER BY ARTIST_NAME ASC")
    override fun getByFileSizeASC(): Flow<List<Artist>>

    @Query("SELECT * FROM ARTISTS ORDER BY ARTIST_NAME DESC")
    override fun getByFileSizeDESC(): Flow<List<Artist>>

    @Query("SELECT * FROM ARTISTS ORDER BY ARTIST_NAME ASC")
    override fun getByFolderPathASC(): Flow<List<Artist>>

    @Query("SELECT * FROM ARTISTS ORDER BY ARTIST_NAME DESC")
    override fun getByFolderPathDESC(): Flow<List<Artist>>

    @Query("SELECT * FROM ARTISTS ORDER BY ARTIST_TRACKS_NUMBER ASC")
    override fun getByDurationASC(): Flow<List<Artist>>

    @Query("SELECT * FROM ARTISTS ORDER BY ARTIST_TRACKS_NUMBER DESC")
    override fun getByDurationDESC(): Flow<List<Artist>>

    @Query("SELECT * FROM ARTISTS ORDER BY ARTIST_MODIFY_DATE ASC")
    override fun getByDateASC(): Flow<List<Artist>>

    @Query("SELECT * FROM ARTISTS ORDER BY ARTIST_MODIFY_DATE DESC")
    override fun getByDateDESC(): Flow<List<Artist>>

    @Query("SELECT * FROM ARTISTS ORDER BY ARTIST_TRACKS_NUMBER ASC")
    override fun getBySongsCountASC(): Flow<List<Artist>>

    @Query("SELECT * FROM ARTISTS ORDER BY ARTIST_TRACKS_NUMBER DESC")
    override fun getBySongsCountDESC(): Flow<List<Artist>>


    @Transaction
    @Query("SELECT * FROM ARTISTS ORDER BY ARTIST_NAME")
    fun getArtistsWithAlbumsAndSongs(): Flow<List<ArtistWithAlbumsAndSongs>>

    @Query("SELECT * FROM ARTISTS WHERE ARTIST_ID = :id")
    suspend fun getArtistById(id: Long): Artist

    @Transaction
    @Query("SELECT * FROM ARTISTS WHERE ARTIST_ID = :id")
    suspend fun getArtistWithAlbumsAndSongs(id: Long): ArtistWithAlbumsAndSongs

}