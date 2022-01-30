package com.example.compose.viewmodel

import android.app.Application
import androidx.compose.material.ExperimentalMaterialApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.compose.local.dataStore
import com.example.compose.local.model.Song
import com.example.compose.local.preferences.PreferencesRepository
import com.example.compose.local.preferences.SortOrder
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
class MainViewModel(application: Application) : AndroidViewModel(application) {

    val preferences = PreferencesRepository(application.dataStore)

    val repository = Repository(application, preferences.sortOrdersFlow)

    val serviceController = ServiceController()

    var bottomSheetProgress = MutableStateFlow(0f)

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

    //Song Screen State

    private val _songScreenState = MutableStateFlow(SongScreenState())
    val songScreenState: StateFlow<SongScreenState> get() = _songScreenState
    private val _sortBy = MutableStateFlow(SortOrder.TitleASC)

    private val _expandedSongIndex = MutableStateFlow(-1)
    fun setExpandedSongIndex(expanded: Boolean, index: Int) = viewModelScope.launch {
        if (expanded) _expandedSongIndex.value = index
        else if (_expandedSongIndex.value == index) _expandedSongIndex.value = -1
        recombine()
    }

    private fun recombine() {
        viewModelScope.launch {
            combine(
                _sortBy,
                _expandedSongIndex
            ) { _isRefreshing, _isSettingsOpen ->
                SongScreenState(_isRefreshing, _isSettingsOpen)
            }.collect { _songScreenState.value = it }
        }
    }
}