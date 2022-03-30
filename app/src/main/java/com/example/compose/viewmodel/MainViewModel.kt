package com.example.compose.viewmodel

import android.util.Log
import androidx.collection.LruCache
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compose.local.model.Song
import com.example.compose.local.preferences.AppPreferences
import com.example.compose.ui.Page
import com.example.compose.ui.composables.fab.EmoFabMode
import com.example.compose.utils.kotlin_extensions.compIn
import com.example.compose.utils.util_classes.DefaultMainColors
import com.example.compose.utils.util_classes.MainColors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FabState(
    val isMenuOpen: Boolean = false,
    val isDragging: Boolean = false,
    val isPlaying: Boolean = false,
    val fabMode: EmoFabMode = EmoFabMode.Menu,
)

data class StageState(
    val initialized: Boolean = false,
    val isPlaying: Boolean = false,
    val index: Int = 0,
    val queue: List<Song> = emptyList(),
    val color: MainColors = DefaultMainColors,
    val animateColor: Boolean = false,
    val stageAlpha: Float = 0f,
    val timelineAlpha: Float = 0f,
    val buttonsAlpha: Float = 0f,
    val miniStageAlpha: Float = 1f,
)

data class MainScaffoldState(
    val isFabExpanded: Boolean = false,
    val isFabDragging: Boolean = false,
    val progress: Float = 0f,
    val transProgress: Float = 0f,
    val showBottomNav: Boolean = true
)

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

    private val stageSheetProgress = MutableStateFlow(0f)
    private val stageTransProgress = MutableStateFlow(0f)


    fun addColorToCache(index: Int, color: MainColors) {
        if (colorCache[index] == null) colorCache.put(index, color)
    }

    fun setFabState(isMenuOpen: Boolean = isFabExpanded.value, isDragging: Boolean = isFabDragging.value) {
        isFabExpanded.value = isMenuOpen
        isFabDragging.value = isDragging
    }

    fun setSheetState(transProgress: Float, mainProgress: Float) {
        stageTransProgress.value = transProgress
        stageSheetProgress.value = mainProgress
    }

    fun setCurrentPage(page: Page) {
        if (_currentPage != page) _currentPage.value = page
    }


    private val _currentPage = MutableStateFlow<Page>(Page.Libraries.HomePage)
    val currentPage: StateFlow<Page> get() = _currentPage

    private val _fabState = MutableStateFlow(FabState())
    val fabState: StateFlow<FabState> get() = _fabState

    private val _stageState = MutableStateFlow(StageState())
    val stageState: StateFlow<StageState> get() = _stageState

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
            { expand, drag, playState, progress ->
                val fabMode = when (progress) {
                    0f -> EmoFabMode.Menu
                    1f -> EmoFabMode.Playback
                    else -> EmoFabMode.Menu2Playback(progress)
                }
                FabState(expand, drag, playState, fabMode)
            }.catch { it.message?.let { m -> Log.i(TAG, m) } }.collect { _fabState.value = it }
        }

        viewModelScope.launch {
            combine(preferences.playStateFlow, currentSongIndex, queue, colorFlow, stageSheetProgress)
            { playState, i, queue, color, progress ->
                StageState(
                    initialized = true,
                    isPlaying = playState,
                    index = i,
                    queue = queue,
                    color = color,
                    animateColor = progress != 0f,
                    stageAlpha = progress.compIn(0.05f, 0.2f),
                    timelineAlpha = progress.compIn(0.75f, 0.8f),
                    buttonsAlpha = progress.compIn(0.8f, 0.9f),
                    miniStageAlpha = 1 - progress.compIn(end = 0.05f),
                )
            }.catch { it.message?.let { m -> Log.i(TAG, m) } }.collect { _stageState.value = it }
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