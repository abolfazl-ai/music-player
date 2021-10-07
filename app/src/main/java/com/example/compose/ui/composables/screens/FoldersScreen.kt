package com.example.compose.ui.composables.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.ui.composables.list_items.folder_item.FolderItem
import com.example.compose.ui.composables.list_items.song_item.SongItem
import com.example.compose.ui.theme.Blue700
import com.example.compose.ui.theme.Purple700
import com.example.compose.viewmodel.MainViewModel

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalAnimationGraphicsApi
val foldersScreen = @Composable { modifier: Modifier -> FoldersScreen(modifier) }

@ExperimentalAnimationApi
@ExperimentalAnimationGraphicsApi
@ExperimentalMaterialApi
@Composable
fun FoldersScreen(modifier: Modifier=Modifier, viewModel: MainViewModel = viewModel()) {

    var expandIndex by remember { mutableStateOf(-1) }
    val selectList = remember { mutableStateListOf<Int>() }

    val folders by viewModel.repository.allFolders.collectAsState(initial = emptyList())

    val onSelect: (Int) -> Unit = remember {
        { if (selectList.contains(it)) selectList.remove(it) else selectList.add(it) }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp, 36.dp, 8.dp, 88.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        itemsIndexed(folders) { index, folder ->
            FolderItem(
                folder,
                expandIndex == index,
                selectList.contains(index),
                { if (selectList.isNotEmpty()) onSelect(index) },
                { if (it) expandIndex = index else if (expandIndex == index) expandIndex = -1 },
                { onSelect(index) }
            )
        }
    }
}