package com.example.compose.ui.composables.library_screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.ui.composables.list_items.linear_item.LinearItem
import com.example.compose.ui.theme.DarkGray
import com.example.compose.utils.kotlin_extensions.toTimeFormat
import com.example.compose.viewmodel.MainViewModel

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalAnimationGraphicsApi
@ExperimentalMaterialApi
@Composable
fun FoldersLibrary(modifier: Modifier = Modifier, viewModel: MainViewModel = viewModel()) {

    var expandIndex by remember { mutableStateOf(-1) }
    val selectList = remember { mutableStateListOf<Int>() }

    val folders by viewModel.repository.allFolders.collectAsState(initial = emptyList())

    val onSelect: (Int) -> Unit = remember {
        { if (selectList.contains(it)) selectList.remove(it) else selectList.add(it) }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        itemsIndexed(folders) { index, folder ->
            LinearItem(
                title = folder.title,
                subtitle = "Contains ${folder.tracksNumber} tracks",
                description = folder.totalDuration.toTimeFormat(),
                picture = {
                    Icon(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(DarkGray)
                            .padding(8.dp),
                        imageVector = Icons.Rounded.Folder,
                        contentDescription = null,
                        tint = Color.White
                    )
                },
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