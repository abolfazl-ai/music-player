package com.example.compose.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.compose.ui.composables.icons.Album
import com.example.compose.ui.composables.icons.Artist
import com.example.compose.ui.composables.icons.Folder
import com.example.compose.ui.composables.icons.Song

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {

    object Home : Screen("screen/home", "Home", Icons.Filled.Home)
    object Songs : Screen("screen/songs", "All Songs", Icons.Filled.Song)
    object Folders : Screen("screen/folders", "Folders", Icons.Filled.Folder)
    object Artists : Screen("screen/artists", "Artists", Icons.Filled.Artist)
    object Albums : Screen("screen/albums", "Albums", Icons.Filled.Album)

}

val screens = listOf(Screen.Home, Screen.Songs, Screen.Folders, Screen.Artists, Screen.Albums)