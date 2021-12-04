package com.example.compose.ui.composables.library_screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.local.model.Song
import com.example.compose.ui.composables.list_items.ItemOptions
import com.example.compose.ui.composables.list_items.LinearItem
import com.example.compose.utils.default_pictures.SongAndSize
import com.example.compose.utils.kotlin_extensions.toTimeFormat
import com.example.compose.utils.resources.IconOptionsHeight
import com.example.compose.viewmodel.MainViewModel
import com.skydoves.landscapist.glide.GlideImage

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun SongsLibrary(modifier: Modifier = Modifier, viewModel: MainViewModel = viewModel()) {

    val state = viewModel.songScreenState.collectAsState()

    var expandIndex by rememberSaveable { mutableStateOf(-1) }
    val selectList = remember { mutableStateListOf<Int>() }

    val songs by viewModel.repository.getSongs(state.value.sortBy)
        .collectAsState(initial = emptyList())

    val onSelect: (Int) -> Unit = remember {
        { if (selectList.contains(it)) selectList.remove(it) else selectList.add(it) }
    }

    val options: @Composable BoxScope.() -> Unit = { ItemOptions() }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(6.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        itemsIndexed(songs) { index, song ->
            SongItem(
                song = song,
                itemOptions = options,
                expanded = expandIndex == index,
                selected = selectList.contains(index),
                onExpand = {
                    if (it) expandIndex = index else if (expandIndex == index) expandIndex = -1
                },
                onSelect = { onSelect(index) }
            ) { if (selectList.isNotEmpty()) onSelect(index) }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun SongItem(
    song: Song,
    itemOptions: @Composable BoxScope.() -> Unit = { ItemOptions() },
    itemOptionsHeight: Dp = IconOptionsHeight,
    expanded: Boolean = false,
    selected: Boolean = false,
    onExpand: (Boolean) -> Unit = {},
    onSelect: () -> Unit = {},
    onClick: () -> Unit = {},
) = LinearItem(
    title = song.title, subtitle = song.artist.replace(";", " & "),
    description = song.duration.toTimeFormat(),
    picture = { shape, size ->
        Icon(
            modifier = Modifier
                .fillMaxSize()
                .scale(0.99f)
                .clip(shape)
                .background(MaterialTheme.colorScheme.tertiary)
                .padding(8.dp),
            imageVector = Icons.Default.MusicNote,
            tint = MaterialTheme.colorScheme.onTertiary,
            contentDescription = null
        )
        GlideImage(SongAndSize(song, size))
    },
    expanded = expanded, selected = selected, onExpand = onExpand, onSelect = onSelect,
    onClick = onClick, itemOptions = itemOptions, itemOptionsHeight = itemOptionsHeight,
)