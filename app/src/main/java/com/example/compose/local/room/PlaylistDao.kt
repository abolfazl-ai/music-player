package com.example.compose.local.room

import androidx.room.*
import com.example.compose.local.model.PlayList
import com.example.compose.local.model.PlaylistItem
import com.example.compose.local.model.PlaylistWithItems
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylist(playList: PlayList): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylistItem(item: PlaylistItem)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylistItems(items: List<PlaylistItem>)

    @Delete
    suspend fun deletePlaylist(playList: PlayList)

/*    @Query("SELECT id, name, COUNT(i.`index`) AS songs_count, is_current FROM playlists p LEFT JOIN playlist_items i ON i.playlist_id = p.id GROUP BY id")
    fun getPlaylists(): Flow<List<PlayList>>*/

    @Transaction
    @Query("SELECT * FROM PLAYLISTS WHERE id = :id")
    fun getPlaylistWithItems(id: Int): Flow<PlaylistWithItems>

    @Transaction
    @Query("SELECT * FROM PLAYLISTS")
    fun getPlaylistsWithItems(): Flow<List<PlaylistWithItems>>

    @Transaction
    @Query("SELECT * FROM PLAYLISTS WHERE IS_CURRENT = 1 LIMIT 1")
    suspend fun getCurrentPlaylist(): PlaylistWithItems?

/*    @Query("DELETE FROM playlist_items WHERE playlist_id = :id")
    suspend fun clearCurrentPlaylist(id: Int)*/
}