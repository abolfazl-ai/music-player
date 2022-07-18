package com.example.compose.ui.composables.library_screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.compose.ui.composables.list_items.GridItem
import com.example.compose.utils.image_loader.CoilAlbumFetcher
import com.example.compose.viewmodel.MainViewModel

@OptIn(ExperimentalCoilApi::class)
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun AlbumsLibrary(modifier: Modifier = Modifier, viewModel: MainViewModel = hiltViewModel()) {

    val selectList = remember { mutableStateListOf<Int>() }

    val albums by viewModel.repository.allAlbums.collectAsState(initial = emptyList())

    val onSelect: (Int) -> Unit = remember {
        { if (selectList.contains(it)) selectList.remove(it) else selectList.add(it) }
    }

    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(6.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        columns = GridCells.Fixed(2)
    ) {
        itemsIndexed(albums) { index, album ->
            GridItem(
                title = album.name.trim(),
                subtitle = album.artist.trim(),
                picture = {
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = ImageRequest.Builder(LocalContext.current)
                            .fetcherFactory(CoilAlbumFetcher.Factory)
                            .data(album).size(it).crossfade(true).build(),
                        contentScale = ContentScale.Crop,
                        contentDescription = album.name
                    )
                },
                selected = selectList.contains(index),
                onSelect = { onSelect(index) }
            ) { if (selectList.isNotEmpty()) onSelect(index) }
        }
    }
}