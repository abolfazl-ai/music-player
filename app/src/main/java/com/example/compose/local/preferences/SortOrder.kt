package com.example.compose.local.preferences

enum class SortOrder {
    TitleASC,
    TitleDESC,
    ArtistASC,
    ArtistDESC,
    AlbumASC,
    AlbumDESC,
    SizeASC,
    SizeDESC,
    FolderASC,
    FolderDESC,
    DurationASC,
    DurationDESC,
    ModifyDateASC,
    ModifyDateDESC,
    SongsCountASC,
    SongsCountDESC,
}

data class SortOrders(
    val songsOrder: SortOrder,
    val artistsOrder: SortOrder,
    val albumsOrder: SortOrder,
    val foldersOrder: SortOrder,
    val playlistsOrder: SortOrder,
)