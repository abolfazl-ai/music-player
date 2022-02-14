package com.example.compose.viewmodel

import android.util.Log
import androidx.collection.LruCache
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compose.local.model.Song
import com.example.compose.local.preferences.AppPreferences
import com.example.compose.ui.composables.layouts.SheetValue
import com.example.compose.utils.kotlin_extensions.compIn
import com.example.compose.utils.util_classes.DefaultMainColors
import com.example.compose.utils.util_classes.MainColors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
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
    val animateColor: Boolean = false,
)

data class MiniStageState(
    val show: Boolean = false,
    val alpha: Float = 1f,
    val contentAlpha: Float = 1f,
    val isPlaying: Boolean = false,
    val currentIndex: Int = 0,
    val queue: List<Song> = emptyList(),
    val sheetProgress: Float = 0f,
)

data class MainScaffoldState(
    val isFabExpanded: Boolean = false,
    val isFabDragging: Boolean = false,
    val progress: Float = 0f,
    val transProgress: Float = 0f,
    val showBottomNav: Boolean = true
)

data class PlaybackControlsState(
    val show: Boolean = false,
    val timeLineAlpha: Float = 0f,
    val buttonsAlpha: Float = 0f,
    val color: Color = DefaultMainColors.front
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

    suspend fun updateCurrentIndex(index: Int) = preferences.updateCurrentIndex(index)


    //UI related

    val colorFlow = MutableStateFlow(DefaultMainColors)
    private val colorCache = LruCache<Int, MainColors>(100)

    private val isFabExpanded = MutableStateFlow(false)
    private val isFabDragging = MutableStateFlow(false)

    private val stageSheetValue = MutableStateFlow(SheetValue.Collapsed)
    private val stageSheetProgress = MutableStateFlow(0f)
    private val stageTransProgress = MutableStateFlow(0f)


    fun addColorToCache(index: Int, color: MainColors) = colorCache.put(index, color)

    fun setFabState(
        isExpanded: Boolean = isFabExpanded.value,
        isDragging: Boolean = isFabDragging.value,
    ) {
        isFabExpanded.value = isExpanded
        isFabDragging.value = isDragging
    }

    fun setSheetState(state: SheetValue, transProgress: Float, mainProgress: Float) {
        if (stageSheetValue.value != state) stageSheetValue.value = state
        stageTransProgress.value = transProgress
        stageSheetProgress.value = mainProgress
    }


    private val _fabState = MutableStateFlow(FabState())
    val fabState: StateFlow<FabState> get() = _fabState

    private val _stageState = MutableStateFlow(StageState())
    val stageState: StateFlow<StageState> get() = _stageState

    private val _miniStageState = MutableStateFlow(MiniStageState())
    val miniStageState: StateFlow<MiniStageState> get() = _miniStageState

    private val _playbackControlsState = MutableStateFlow(PlaybackControlsState())
    val playbackControlsState: StateFlow<PlaybackControlsState> get() = _playbackControlsState

    private val _mainScaffoldState = MutableStateFlow(MainScaffoldState())
    val mainScaffoldState: StateFlow<MainScaffoldState> get() = _mainScaffoldState


    init {
        viewModelScope.launch {
            currentSongIndex.collect { index ->
                colorCache[index]?.let { colorFlow.value = it }
                var i = 0
                do {
                    i++
                    delay(200)
                    colorCache[index]?.let { colorFlow.value = it }
                } while (colorCache[index] == null && i < 10)
            }
        }

        viewModelScope.launch {
            combine(isFabExpanded, isFabDragging, preferences.playStateFlow, stageTransProgress)
            { expand, drag, playState, progress -> FabState(expand, drag, playState, progress) }
                .catch { it.message?.let { m -> Log.i(TAG, m) } }
                .collect { _fabState.value = it }
        }

        viewModelScope.launch {
            combine(preferences.playStateFlow, currentSongIndex, queue, stageSheetValue)
            { playState, i, queue, value ->
                StageState(true, playState, i, queue, value != SheetValue.Collapsed)
            }.catch { it.message?.let { m -> Log.i(TAG, m) } }.collect { _stageState.value = it }
        }

        viewModelScope.launch {
            combine(preferences.playStateFlow, currentSongIndex, queue, stageSheetProgress)
            { playState, i, queue, progress ->
                MiniStageState(
                    show = progress < 1, alpha = (1 - progress.compIn(0.05f, 0.15f)),
                    contentAlpha = 1 - progress.compIn(end = 0.05f), playState, i, queue, progress
                )
            }.catch { it.message?.let { m -> Log.i(TAG, m) } }.collect { _miniStageState.value = it }
        }

        viewModelScope.launch {
            combine(stageSheetProgress, colorFlow)
            { p, color -> PlaybackControlsState(p > 0f, p.compIn(0.75f, 0.8f), p.compIn(0.8f, 0.9f), color.front) }
                .catch { it.message?.let { m -> Log.i(TAG, m) } }.collect { _playbackControlsState.value = it }
        }

        viewModelScope.launch {
            combine(
                isFabExpanded, isFabDragging, stageSheetProgress,
                stageTransProgress, preferences.playStateFlow
            ) { expand, drag, progress, trans, play -> MainScaffoldState(expand, drag, progress, trans, !play) }
                .catch { it.message?.let { m -> Log.i(TAG, m) } }.collect { _mainScaffoldState.value = it }
        }
    }
}