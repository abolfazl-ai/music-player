package com.example.compose.local.room

import com.example.compose.local.preferences.SortOrder
import com.example.compose.local.preferences.SortOrder.*
import kotlinx.coroutines.flow.Flow

interface SortInterface<T> {

    fun getByTitleASC(): Flow<List<T>>
    fun getByTitleDESC(): Flow<List<T>>
    fun getByArtistASC(): Flow<List<T>>
    fun getByArtistDESC(): Flow<List<T>>
    fun getByAlbumASC(): Flow<List<T>>
    fun getByAlbumDESC(): Flow<List<T>>
    fun getByFileSizeASC(): Flow<List<T>>
    fun getByFileSizeDESC(): Flow<List<T>>
    fun getByFolderPathASC(): Flow<List<T>>
    fun getByFolderPathDESC(): Flow<List<T>>
    fun getByDurationASC(): Flow<List<T>>
    fun getByDurationDESC(): Flow<List<T>>
    fun getByDateASC(): Flow<List<T>>
    fun getByDateDESC(): Flow<List<T>>
    fun getBySongsCountASC(): Flow<List<T>>
    fun getBySongsCountDESC(): Flow<List<T>>

}

fun <T : Any> getBySortOrder(dao: SortInterface<T>, sort: SortOrder): Flow<List<T>> = when (sort) {
    TitleASC -> dao.getByTitleASC()
    TitleDESC -> dao.getByTitleDESC()
    ArtistASC -> dao.getByArtistASC()
    ArtistDESC -> dao.getByArtistDESC()
    AlbumASC -> dao.getByAlbumASC()
    AlbumDESC -> dao.getByAlbumDESC()
    SizeASC -> dao.getByFileSizeASC()
    SizeDESC -> dao.getByFileSizeDESC()
    FolderASC -> dao.getByFolderPathASC()
    FolderDESC -> dao.getByFolderPathDESC()
    DurationASC -> dao.getByDurationASC()
    DurationDESC -> dao.getByDurationDESC()
    ModifyDateASC -> dao.getByDateASC()
    ModifyDateDESC -> dao.getByDateDESC()
    SongsCountASC -> dao.getBySongsCountASC()
    SongsCountDESC -> dao.getBySongsCountDESC()
}