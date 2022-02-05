package com.example.compose.viewmodel

import android.util.Log
import androidx.collection.LruCache
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compose.local.model.Song
import com.example.compose.local.preferences.AppPreferences
import com.example.compose.utils.util_classes.DefaultMainColors
import com.example.compose.utils.util_classes.MainColors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FabState(
    val isFabExpanded: Boolean = false,
    val isFabDragging: Boolean = false,
    val isPlaying: Boolean = false,
    val fabTransProgress: Float = 0f,
)

data class MiniPlayerState(
    val isPlaying: Boolean = false,
    val song: Song? = null,
    val sheetProgress: Float = 0f
)

data class PlayScreenState(
    val song: Song? = null,
    val color: MainColors = DefaultMainColors,
    val sheetProgress: Float = 0f
)

data class MainScaffoldState(
    val isFabExpanded: Boolean = false,
    val isFabDragging: Boolean = false,
    val progress: Float = 0f,
    val transProgress: Float = 0f,
    val showBottomNav: Boolean = true
)

@OptIn(InternalCoroutinesApi::class)
@ExperimentalMaterialApi
@HiltViewModel
class MainViewModel @Inject constructor(
    val repository: Repository,
    val preferences: AppPreferences,
    val serviceController: ServiceController,
) : ViewModel() {

    val TAG = "ViewModel"

    //Playback related

    val queue: Flow<List<Song>> = repository.allSongs
    private val _currentSongIndex = MutableStateFlow(0)
    val currentSongIndex: StateFlow<Int> get() = _currentSongIndex

    fun updateCurrentSongIndex(index: Int) {
        _currentSongIndex.value = index
    }

    //UI related

    private val colorCache = LruCache<Int, MainColors>(100)

    private val isFabExpanded = MutableStateFlow(false)
    private val isFabDragging = MutableStateFlow(false)

    private val mainSheetProgress = MutableStateFlow(0f)
    private val mainSheetTransProgress = MutableStateFlow(0f)


    fun addColorToCache(index: Int, color: MainColors) = colorCache.put(index, color)

    fun setFabState(
        isExpanded: Boolean = isFabExpanded.value,
        isDragging: Boolean = isFabDragging.value,
    ) {
        isFabExpanded.value = isExpanded
        isFabDragging.value = isDragging
    }

    fun setSheetState(transProgress: Float, mainProgress: Float) {
        mainSheetTransProgress.value = transProgress
        mainSheetProgress.value = mainProgress
    }


    private val _fabState = MutableStateFlow(FabState())
    val fabState: StateFlow<FabState> get() = _fabState

    private val _playerScreenState = MutableStateFlow(PlayScreenState())
    val playerScreenState: StateFlow<PlayScreenState> get() = _playerScreenState

    private val _miniPlayerState = MutableStateFlow(MiniPlayerState())
    val miniPlayerState: StateFlow<MiniPlayerState> get() = _miniPlayerState

    private val _mainScaffoldState = MutableStateFlow(MainScaffoldState())
    val mainScaffoldState: StateFlow<MainScaffoldState> get() = _mainScaffoldState


    init {
        viewModelScope.launch {
            combine(isFabExpanded, isFabDragging, preferences.playStateFlow, mainSheetTransProgress)
            { expand, drag, playState, progress -> FabState(expand, drag, playState, progress) }
                .catch { throwable -> throwable.message?.let { Log.i(TAG, it) } }
                .collect { _fabState.value = it }
        }

        viewModelScope.launch {
            combine(currentSongIndex, queue, mainSheetProgress)
            { i, queue, progress ->
                PlayScreenState(queue[i], colorCache[i] ?: DefaultMainColors, progress)
            }.catch { throwable -> throwable.message?.let { Log.i(TAG, it) } }.collect {
                _playerScreenState.value = it
            }
        }

        viewModelScope.launch {
            combine(preferences.playStateFlow, currentSongIndex, queue, mainSheetProgress)
            { playState, i, q, progress -> MiniPlayerState(playState, q[i], progress) }
                .catch { throwable -> throwable.message?.let { Log.i(TAG, it) } }
                .collect { _miniPlayerState.value = it }
        }

        viewModelScope.launch {
            combine(
                isFabExpanded, isFabDragging, mainSheetProgress,
                mainSheetTransProgress, preferences.playStateFlow
            ) { expand, drag, p, t, play -> MainScaffoldState(expand, drag, p, t, play) }
                .catch { throwable -> throwable.message?.let { Log.i(TAG, it) } }
                .collect { _mainScaffoldState.value = it }
        }
    }
}