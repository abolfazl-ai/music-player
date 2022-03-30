package com.example.compose.ui.composables.stage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.compose.ui.composables.modifiers.reveal
import com.example.compose.utils.resources.FabSize
import com.example.compose.utils.resources.StageSpacing
import com.example.compose.utils.resources.TimeLineHeight
import com.example.compose.utils.util_classes.PlaybackAction
import com.example.compose.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun Stage(modifier: Modifier = Modifier, viewModel: MainViewModel = hiltViewModel()) {

    val state by viewModel.stageState.collectAsState()
    val scope = rememberCoroutineScope()

    if (state.initialized) BoxWithConstraints(modifier.fillMaxSize()) {

        val stageSize = remember { Size(constraints.maxWidth.toFloat(), constraints.maxHeight.toFloat()) }
        val revealY = remember { maxWidth + TimeLineHeight + FabSize / 2 + StageSpacing }
        var temp by remember { mutableStateOf(state.index) }

        Column(
            Modifier
                .fillMaxSize()
                .alpha(state.stageAlpha)
                .reveal(state.animateColor, state.color.back, stageSize, revealY, 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CoverViewPager(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(Color.Black),
                queue = state.queue, currentIndex = temp,
                onPageChanged = { scope.launch { temp = it;if (it != state.index) viewModel.updateCurrentIndex(it) } },
                onPageCreated = { page, colors -> viewModel.addColorToCache(page, colors) }
            )

            PlaybackController(state.color.front, timelineAlpha = state.timelineAlpha, buttonsAlpha = state.buttonsAlpha) { action ->
                scope.launch {
                    when (action) {
                        PlaybackAction.PREVIOUS -> if (temp == state.index) temp = state.index - 1
                        PlaybackAction.NEXT -> if (temp == state.index) temp = state.index + 1
                    }
                }
            }
        }

        MiniStage(song = state.queue[temp], index = temp, isPlaying = state.isPlaying, alpha = state.miniStageAlpha) { action ->
            scope.launch {
                when (action) {
                    PlaybackAction.PREVIOUS -> if (temp == state.index) temp = state.index - 1
                    PlaybackAction.PLAY -> viewModel.preferences.updatePlayingState(!state.isPlaying)
                    PlaybackAction.NEXT -> if (temp == state.index) temp = state.index + 1
                }
            }
        }
    }
}