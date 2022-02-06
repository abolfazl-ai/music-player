package com.example.compose.viewmodel

import android.util.Log
import androidx.collection.LruCache
import androidx.compose.material.ExperimentalMaterialApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compose.local.model.Song
import com.example.compose.local.preferences.AppPreferences
import com.example.compose.utils.kotlin_extensions.compIn
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

data class StageState(
    val initialized: Boolean = false,
    val isPlaying: Boolean = false,
    val currentIndex: Int = 0,
    val queue: List<Song> = emptyList(),
    val color: MainColors = DefaultMainColors
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

    private val queue = repository.allSongs
    private val currentSongIndex = preferences.currentIndexFlow

    suspend fun updateCurrentSongIndex(index: Int) = preferences.updateCurrentIndex(index)


    //UI related

    private val colorCache = LruCache<Int, MainColors>(100)

    private val isFabExpanded = MutableStateFlow(false)
    private val isFabDragging = MutableStateFlow(false)

    val stageSheetProgress = MutableStateFlow(0f)
    val stageTransProgress = MutableStateFlow(0f)


    fun addColorToCache(index: Int, color: MainColors) = colorCache.put(index, color)

    fun setFabState(
        isExpanded: Boolean = isFabExpanded.value,
        isDragging: Boolean = isFabDragging.value,
    ) {
        isFabExpanded.value = isExpanded
        isFabDragging.value = isDragging
    }

    fun setSheetState(transProgress: Float, mainProgress: Float) {
        stageTransProgress.value = transProgress
        stageSheetProgress.value = mainProgress
    }


    private val _fabState = MutableStateFlow(FabState())
    val fabState: StateFlow<FabState> get() = _fabState

    private val _stageState = MutableStateFlow(StageState())
    val stageState: StateFlow<StageState> get() = _stageState

    private val _mainScaffoldState = MutableStateFlow(MainScaffoldState())
    val mainScaffoldState: StateFlow<MainScaffoldState> get() = _mainScaffoldState


    init {
        viewModelScope.launch {
            combine(isFabExpanded, isFabDragging, preferences.playStateFlow, stageTransProgress)
            { expand, drag, playState, progress -> FabState(expand, drag, playState, progress) }
                .catch { it.message?.let { m -> Log.i(TAG, m) } }
                .collect { _fabState.value = it }
        }

        viewModelScope.launch {
            combine(preferences.playStateFlow, currentSongIndex, queue)
            { playState, i, queue -> StageState(true, playState, i, queue, colorCache[i] ?: DefaultMainColors) }
                .catch { it.message?.let { m -> Log.i(TAG, m) } }.collect { _stageState.value = it }
        }

        viewModelScope.launch {
            combine(
                isFabExpanded, isFabDragging, stageSheetProgress,
                stageTransProgress, preferences.playStateFlow
            ) { expand, drag, progress, trans, play -> MainScaffoldState(expand, drag, progress, trans, play) }
                .catch { it.message?.let { m -> Log.i(TAG, m) } }.collect { _mainScaffoldState.value = it }
        }
    }
}