package com.example.compose.viewmodel

import androidx.compose.material.ExperimentalMaterialApi
import com.example.compose.local.model.Song

@ExperimentalMaterialApi
data class SongScreenState(
    val isRefreshing: Boolean = false,
    val isSettingsOpen: Boolean = false,
    val sortBy: Song.Sort = Song.Sort.TitleASC,
    val expandedIndex: Int = -1,
)
