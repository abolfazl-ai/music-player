package com.example.compose.viewmodel

import android.app.Application
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.DrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.compose.local.model.Song
import com.example.compose.ui.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
class MainViewModel(application: Application) : AndroidViewModel(application) {

    val repository = Repository(application)

    val serviceController = ServiceController()


    private val drawerState = MutableStateFlow(DrawerValue.Closed)
    private val playerPanelState = MutableStateFlow(BottomSheetValue.Collapsed)
    private val playerPanelProgress = MutableStateFlow(0f)
    private val homeScreens = MutableStateFlow(
        listOf(
            Screen.Home,
            Screen.Songs,
            Screen.Folders,
            Screen.Artists,
            Screen.Albums
        )
    )
    private val activeScreen = MutableStateFlow(Screen.Home)

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> get() = _uiState


    //Song Screen State

    private val _songScreenState = MutableStateFlow(SongScreenState())
    val songScreenState: StateFlow<SongScreenState> get() = _songScreenState

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing
    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            recombine()

            repository.scan()

            delay(2000)
            _isRefreshing.value = false
            recombine()
        }
    }

    private val _isSettingsOpen = MutableStateFlow(false)

    private val _sortBy = MutableStateFlow(Song.Sort.TitleASC)

    private val _expandedIndex = MutableStateFlow(-1)
    fun setExpandedIndex(expanded: Boolean, index: Int) {
        if (expanded) _expandedIndex.value = index
        else if (_expandedIndex.value == index) _expandedIndex.value = -1

        recombine()
    }

    private fun recombine() {
        viewModelScope.launch {
            combine(
                _isRefreshing,
                _isSettingsOpen,
                _sortBy,
                _expandedIndex
            ) { _isRefreshing, _isSettingsOpen, _sortBy, _expandedIndex ->
                SongScreenState(_isRefreshing, _isSettingsOpen, _sortBy, _expandedIndex)
            }.collect { _songScreenState.value = it }
        }
    }

    init {
        viewModelScope.launch {
            combine(
                drawerState,
                playerPanelState,
                playerPanelProgress,
                homeScreens,
                activeScreen
            ) { drawerState,
                playerPanelState,
                playerPanelProgress,
                homeScreens,
                activeScreen ->
                UiState(
                    drawerState,
                    playerPanelState,
                    playerPanelProgress,
                    homeScreens,
                    activeScreen
                )
            }.collect {
                _uiState.value = it
            }
        }
    }
}