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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.compose.local.model.Song
import com.example.compose.ui.composables.list_items.ItemOptions
import com.example.compose.ui.composables.list_items.LinearItem
import com.example.compose.ui.composables.util_composables.LoadSongCover
import com.example.compose.utils.kotlin_extensions.toTimeFormat
import com.example.compose.utils.resources.IconOptionsHeight
import com.example.compose.viewmodel.MainViewModel

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun SongsLibrary(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {

    val selectList = remember { mutableStateListOf<Int>() }
    val onSelect: (Int) -> Unit = remember {
        { if (selectList.contains(it)) selectList.remove(it) else selectList.add(it) }
    }

    val songs by viewModel.repository.allSongs.collectAsState(initial = emptyList())

    SongList(
        modifier = modifier,
        songs = songs,
        selectList = selectList,
        onSelect = onSelect,
        onItemClick = { if (selectList.isNotEmpty()) onSelect(it) }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SongList(
    modifier: Modifier = Modifier,
    songs: List<Song>,
    options: @Composable BoxScope.() -> Unit = { ItemOptions() },
    selectList: List<Int>,
    onSelect: (index: Int) -> Unit,
    onItemClick: (index: Int) -> Unit
) {
    var expandIndex by rememberSaveable { mutableStateOf(-1) }

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
                    if (it) expandIndex = index
                    else if (expandIndex == index) expandIndex = -1
                },
                onSelect = { onSelect(index) },
                onClick = { onItemClick(index) }
            )
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
    picture = {
        LoadSongCover(song = song, placeHolder = {
            Icon(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.tertiary)
                    .padding(8.dp),
                imageVector = Icons.Default.MusicNote,
                tint = MaterialTheme.colorScheme.onTertiary,
                contentDescription = null
            )
        })
    },
    expanded = expanded, selected = selected, onExpand = onExpand, onSelect = onSelect,
    onClick = onClick, itemOptions = itemOptions, itemOptionsHeight = itemOptionsHeight,
)