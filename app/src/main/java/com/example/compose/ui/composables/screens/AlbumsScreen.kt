package com.example.compose.ui.composables.screens

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.ui.composables.list_items.album_item.AlbumItem
import com.example.compose.ui.composables.list_items.artist_item.ArtistItem
import com.example.compose.ui.theme.Purple700
import com.example.compose.viewmodel.MainViewModel

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalAnimationGraphicsApi
val albumsScreen = @Composable { modifier: Modifier -> AlbumsScreen(modifier) }

@ExperimentalFoundationApi
@ExperimentalAnimationGraphicsApi
@ExperimentalMaterialApi
@Composable
fun AlbumsScreen(modifier: Modifier=Modifier, viewModel: MainViewModel = viewModel()) {

    val selectList = remember { mutableStateListOf<Int>() }

    val albums by viewModel.repository.allAlbums.collectAsState(initial = emptyList())

    val onSelect: (Int) -> Unit = remember {
        { if (selectList.contains(it)) selectList.remove(it) else selectList.add(it) }
    }

    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp, 36.dp, 8.dp, 88.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        cells = GridCells.Fixed(2)
    ) {
        itemsIndexed(albums) { index, album ->
            AlbumItem(
                album = album,
                index = index,
                selected = selectList.contains(index),
                onSelect = onSelect
            ) { if (selectList.isNotEmpty()) onSelect(index) }
        }
    }
}