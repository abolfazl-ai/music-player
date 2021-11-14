package com.example.compose.ui.composables.library_screens

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.ui.composables.icons.Artist
import com.example.compose.ui.composables.list_items.GridItem
import com.example.compose.viewmodel.MainViewModel

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun ArtistsLibrary(modifier: Modifier = Modifier, viewModel: MainViewModel = viewModel()) {

    val selectList = remember { mutableStateListOf<Int>() }

    val artists by viewModel.repository.allArtists.collectAsState(initial = emptyList())

    val onSelect: (Int) -> Unit = remember {
        { if (selectList.contains(it)) selectList.remove(it) else selectList.add(it) }
    }

    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        cells = GridCells.Fixed(2)
    ) {
        itemsIndexed(artists) { index, artist ->
            GridItem(
                title = artist.name.trim(),
                subtitle = "${artist.albumsNumber} albums ${artist.tracksNumber} tracks",
                picture = {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        imageVector = Icons.Filled.Artist,
                        contentDescription = null,
                        tint = Color.White
                    )
                },
                selected = selectList.contains(index),
                onSelect =  {onSelect(index)}
            ) { if (selectList.isNotEmpty()) onSelect(index) }
        }
    }
}