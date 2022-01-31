package com.example.compose.ui.composables.library_screens

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.compose.ui.composables.list_items.LinearItem
import com.example.compose.utils.kotlin_extensions.toTimeFormat
import com.example.compose.viewmodel.MainViewModel

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun FoldersLibrary(modifier: Modifier = Modifier, viewModel: MainViewModel = hiltViewModel()) {

    var expandIndex by rememberSaveable { mutableStateOf(-1) }
    val selectList = remember { mutableStateListOf<Int>() }

    val folders by viewModel.repository.allFolders.collectAsState(initial = emptyList())

    val onSelect: (Int) -> Unit = remember {
        { if (selectList.contains(it)) selectList.remove(it) else selectList.add(it) }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(6.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        itemsIndexed(folders) { index, folder ->
            LinearItem(
                title = folder.title,
                subtitle = "Contains ${folder.tracksNumber} tracks",
                description = folder.totalDuration.toTimeFormat(),
                picture = { _, _ ->
                    Icon(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.tertiary)
                            .padding(8.dp),
                        imageVector = Icons.Rounded.Folder,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onTertiary
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