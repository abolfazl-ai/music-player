package com.example.compose.ui.composables.main_screens

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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.ui.composables.list_items.gird_item.GridItem
import com.example.compose.utils.default_pictures.AlbumAndSize
import com.example.compose.viewmodel.MainViewModel
import com.skydoves.landscapist.glide.GlideImage

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@ExperimentalAnimationGraphicsApi
@ExperimentalMaterialApi
@Composable
fun AlbumsScreen(modifier: Modifier = Modifier, viewModel: MainViewModel = viewModel()) {

    val selectList = remember { mutableStateListOf<Int>() }

    val albums by viewModel.repository.allAlbums.collectAsState(initial = emptyList())

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
        itemsIndexed(albums) { index, album ->
            GridItem(
                title = album.name.trim(),
                subtitle =album.artist.trim(),
                picture = { GlideImage(imageModel = AlbumAndSize(album, it)) },
                selected = selectList.contains(index),
                onSelect = {onSelect(index)}
            ) { if (selectList.isNotEmpty()) onSelect(index) }
        }
    }
}