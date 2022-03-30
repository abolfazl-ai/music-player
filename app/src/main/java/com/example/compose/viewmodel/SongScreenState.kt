package com.example.compose.viewmodel

import androidx.compose.material.ExperimentalMaterialApi
import com.example.compose.local.preferences.SortOrder

@ExperimentalMaterialApi
data class SongScreenState(
    val sortBy: SortOrder = SortOrder.TitleASC,
    val expandedIndex: Int = -1,
)
