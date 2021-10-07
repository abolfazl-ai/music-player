package com.example.compose.viewmodel

import androidx.compose.material.BottomSheetValue
import androidx.compose.material.DrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import com.example.compose.ui.Screen

@ExperimentalMaterialApi
data class UiState(
    val drawerState: DrawerValue = DrawerValue.Closed,
    val playerPanelState: BottomSheetValue = BottomSheetValue.Collapsed,
    val playerPanelProgress: Float = 0f,
    val homeScreens: List<Screen> = emptyList(),
    val activeScreen: Screen = Screen.Home
)
