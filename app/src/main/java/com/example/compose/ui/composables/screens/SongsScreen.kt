package com.example.compose.ui.composables.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.ui.composables.list_items.song_item.SongItem
import com.example.compose.viewmodel.MainViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalAnimationGraphicsApi
val songsScreen = @Composable { modifier: Modifier -> SongsScreen(modifier) }

@ExperimentalAnimationApi
@ExperimentalAnimationGraphicsApi
@ExperimentalMaterialApi
@Composable
fun SongsScreen(modifier: Modifier = Modifier, viewModel: MainViewModel = viewModel()) {

    val state = viewModel.songScreenState.collectAsState()

    val scope = rememberCoroutineScope()

    val selectList = remember { mutableStateListOf<Int>() }

    val songs by viewModel.repository.getSongs().collectAsState(initial = emptyList())

    val onSelect: (Int) -> Unit = remember {
        { if (selectList.contains(it)) selectList.remove(it) else selectList.add(it) }
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = state.value.isRefreshing),
        onRefresh = { viewModel.refresh() }) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp, 36.dp, 8.dp, 88.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp),
                        elevation = 0.dp,
                        onClick = {}
                    ) {
                        Row(
                            Modifier.padding(10.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.CenterVertically),
                                text = "Play all",
                                style = MaterialTheme.typography.subtitle1,
                            )
                        }
                    }
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp),
                        elevation = 0.dp,
                        onClick = {}
                    ) {
                        Row(
                            Modifier.padding(10.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                modifier = Modifier.padding(3.dp),
                                imageVector = Icons.Default.ThumbUp,
                                contentDescription = null
                            )
                            Text(
                                modifier = Modifier.align(Alignment.CenterVertically),
                                text = "Shuffle",
                                style = MaterialTheme.typography.subtitle1,
                            )
                        }
                    }
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp),
                        elevation = 0.dp,
                        onClick = {}
                    ) {
                        Row(
                            Modifier.padding(10.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                modifier = Modifier.padding(3.dp),
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null,
                                tint = MaterialTheme.colors.primary
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.CenterVertically),
                                text = "Favorites",
                                color = MaterialTheme.colors.primary,
                                style = MaterialTheme.typography.subtitle1,
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
            }

            itemsIndexed(songs) { index, song ->
                SongItem(
                    song,
                    expanded = state.value.expandedIndex == index,
                    selected = selectList.contains(index),
                    onExpand = { viewModel.setExpandedIndex(it, index) },
                    onSelect = { onSelect(index) }
                ) { if (selectList.isNotEmpty()) onSelect(index) }
            }
        }
    }
}