package com.example.compose.ui.composables.screens

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.ui.composables.list_items.artist_item.ArtistItem
import com.example.compose.viewmodel.MainViewModel

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalAnimationGraphicsApi
val artistsScreen = @Composable { modifier: Modifier -> ArtistsScreen(modifier) }

@ExperimentalFoundationApi
@ExperimentalAnimationGraphicsApi
@ExperimentalMaterialApi
@Composable
fun ArtistsScreen(modifier: Modifier=Modifier, viewModel: MainViewModel = viewModel()) {

    val selectList = remember { mutableStateListOf<Int>() }

    val artists by viewModel.repository.allArtists.collectAsState(initial = emptyList())

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
        itemsIndexed(artists) { index, artist ->
            ArtistItem(
                artist = artist,
                index = index,
                selected = selectList.contains(index),
                onSelect = onSelect
            ) { if (selectList.isNotEmpty()) onSelect(index) }
        }
    }
}