package com.example.compose.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.material.icons.rounded.Equalizer
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.compose.ui.composables.icons.Artist
import com.example.compose.ui.composables.icons.Folder
import com.example.compose.ui.composables.icons.Song

sealed class Page(val title: String, val icon: ImageVector, val route: String) {

    object Libraries {
        object HomePage : Page("Home Page", Icons.Filled.Home, "Libraries/HomePage")
        object Songs : Page("Songs", Icons.Filled.Song, "Libraries/Songs")
        object Folders : Page("Folders", Icons.Filled.Folder, "Libraries/Folders")
        object Artists : Page("Artists", Icons.Filled.Artist, "Libraries/Artists")
        object Albums : Page("Albums", Icons.Filled.Album, "Libraries/Albums")
        object Playlists : Page("Playlists", Icons.Filled.PlaylistPlay, "Libraries/Playlists")
    }

    object Equalizer : Page("Equalizer", Icons.Rounded.Equalizer, "Equalizer")
    object History : Page("History", Icons.Rounded.History, "History")
    object Favorites : Page("Favorites", Icons.Rounded.Favorite, "Favorites")
    object Settings : Page("Settings", Icons.Rounded.Settings, "Settings")
}

val Libraries = listOf(
    Page.Libraries.HomePage,
    Page.Libraries.Songs,
    Page.Libraries.Folders,
    Page.Libraries.Artists,
    Page.Libraries.Albums,
)