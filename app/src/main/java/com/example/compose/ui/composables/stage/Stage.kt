package com.example.compose.ui.composables.stage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.compose.ui.composables.modifiers.CircularReveal
import com.example.compose.utils.resources.FabSize
import com.example.compose.utils.resources.StageSpacing
import com.example.compose.utils.resources.TimeLineHeight
import com.example.compose.viewmodel.MainViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@Composable
fun Stage(modifier: Modifier = Modifier, viewModel: MainViewModel = hiltViewModel()) {

    val state by viewModel.stageState.collectAsState()
    val scope = rememberCoroutineScope()

    if (state.initialized) BoxWithConstraints(modifier.fillMaxSize()) {

        val stageSize = remember { Size(constraints.maxWidth.toFloat(), constraints.maxHeight.toFloat()) }
        var temp by remember { mutableStateOf(state.currentIndex) }

        Background(state.animateColor, stageSize, maxWidth + TimeLineHeight + FabSize / 2 + StageSpacing, 28.dp)

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            CoverViewPager(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(Color.Black),
                queue = state.queue, currentIndex = temp,
                onPageChanged = { scope.launch { temp = it;if (it != state.currentIndex) viewModel.updateCurrentIndex(it) } },
                onPageCreated = { page, colors -> viewModel.addColorToCache(page, colors) }
            )

            PlaybackController(
                onPrev = { scope.launch { if (temp == state.currentIndex) temp = state.currentIndex - 1 } },
                onNext = { scope.launch { if (temp == state.currentIndex) temp = state.currentIndex + 1 } }
            )
        }

        MiniStage(
            onPrev = { scope.launch { if (temp == state.currentIndex) temp = state.currentIndex - 1 } },
            onPlay = { scope.launch { viewModel.preferences.updatePlayingState(!state.isPlaying) } },
            onNext = { scope.launch { if (temp == state.currentIndex) temp = state.currentIndex + 1 } }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Background(animate: Boolean, size: Size, y: Dp, startRadius: Dp, viewModel: MainViewModel = hiltViewModel()) {
    val color by viewModel.colorFlow.collectAsState()
    CircularReveal(Modifier.fillMaxSize(), animate, color.back, size, y, startRadius)
}