package com.example.compose.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.compose.ui.composables.icons.*
import com.example.compose.ui.composables.screens.*
import com.example.compose.ui.theme.*

sealed class Screen(
    val title: String,
    val icon: ImageVector,
    val color: Color,
    val options: List<OptionItem>
) {

    object Home : Screen(
        "Home", Icons.Filled.Home, HomeColor, listOf(
            OptionItem(OptionId.HOME_SHUFFLE, "Shuffle", Icons.Rounded.Shuffle),
            OptionItem(OptionId.HOME_SETTINGS, "Settings", Icons.Rounded.Settings),
        )
    )

    object Songs : Screen(
        "All Songs", Icons.Filled.Song, SongsColor, listOf(
            OptionItem(OptionId.SONGS_PLAY_ALL, "Play all", Icons.Rounded.PlayArrow),
            OptionItem(OptionId.SONGS_SORT, "Sort", Icons.Rounded.SortByAlpha),
            OptionItem(OptionId.SONGS_SETTINGS, "Settings", Icons.Rounded.Settings),
        )
    )

    object Folders : Screen(
        "Folders", Icons.Filled.Folder, FolderColor, listOf(
            OptionItem(OptionId.FOLDERS_SORT, "Sort", Icons.Rounded.SortByAlpha),
            OptionItem(OptionId.FOLDERS_SETTINGS, "Settings", Icons.Rounded.Settings),
        )
    )

    object Artists : Screen(
        "Artists", Icons.Filled.Artist, ArtistsColor, listOf(
            OptionItem(OptionId.ARTISTS_SORT, "Sort", Icons.Rounded.SortByAlpha),
            OptionItem(OptionId.ARTISTS_VIEW, "View", Icons.Rounded.GridView),
            OptionItem(OptionId.ARTISTS_SETTINGS, "Settings", Icons.Rounded.Settings),
        )
    )

    object Albums : Screen(
        "Albums", Icons.Filled.Album, AlbumsColor, listOf(
            OptionItem(OptionId.ALBUMS_SORT, "Sort", Icons.Rounded.SortByAlpha),
            OptionItem(OptionId.ALBUMS_VIEW, "View", Icons.Rounded.GridView),
            OptionItem(OptionId.ALBUMS_SETTINGS, "Settings", Icons.Rounded.Settings),
        )
    )

}

data class OptionItem(val id: OptionId, val title: String, val icon: ImageVector)

enum class OptionId {
    //Home ids
    HOME_FAVORITES,
    HOME_HISTORY,
    HOME_SHUFFLE,
    HOME_SETTINGS,

    //Songs ids
    SONGS_PLAY_ALL,
    SONGS_SHUFFLE,
    SONGS_SORT,
    SONGS_SETTINGS,

    //Folders ids
    FOLDERS_SORT,
    FOLDERS_SETTINGS,

    //Artists ids
    ARTISTS_SORT,
    ARTISTS_VIEW,
    ARTISTS_SETTINGS,

    //Albums ids
    ALBUMS_SORT,
    ALBUMS_VIEW,
    ALBUMS_SETTINGS,
}

@ExperimentalFoundationApi
@ExperimentalAnimationGraphicsApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
fun getScreenComposable(screen: Screen): @Composable (Modifier) -> Unit = when (screen) {
    Screen.Home -> HomeScreen
    Screen.Songs -> songsScreen
    Screen.Folders -> foldersScreen
    Screen.Artists -> artistsScreen
    Screen.Albums -> albumsScreen
}
