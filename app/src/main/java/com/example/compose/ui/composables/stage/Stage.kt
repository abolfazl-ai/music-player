package com.example.compose.ui.composables.stage

import android.util.Log
import android.util.Size
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.compose.ui.composables.modifiers.reveal
import com.example.compose.utils.resources.FabSize
import com.example.compose.utils.resources.StageSpacing
import com.example.compose.utils.resources.TAG
import com.example.compose.utils.resources.TimeLineHeight
import com.example.compose.viewmodel.MainViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@Composable
fun Stage(modifier: Modifier = Modifier, viewModel: MainViewModel = hiltViewModel()) = BoxWithConstraints(modifier.fillMaxSize()) {

    val state by viewModel.stageState.collectAsState()
    val scope = rememberCoroutineScope()

    val size = remember { Size(constraints.maxWidth / 3, constraints.maxWidth / 3) }
    var temp by remember { mutableStateOf(state.currentIndex) }

//    Log.e(TAG, "Stage: ${state.animateColor}")

    if (state.initialized) CompositionLocalProvider(LocalContentColor provides animateColorAsState(state.color.front, tween(1000)).value) {
        Column(
            Modifier
                .fillMaxSize()
                .reveal(state.animateColor, state.color.back, maxWidth + TimeLineHeight + FabSize / 2 + StageSpacing, 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CoverViewPager(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(Color.Black),
                size = size, queue = state.queue, currentIndex = state.currentIndex,
                onPageChanged = {
                    scope.launch {
                        temp = it
                        if (it != state.currentIndex) viewModel.updateCurrentIndex(it)
                    }
                },
                onPageCreated = { page, colors -> viewModel.addColorToCache(page, colors) }
            )

            Spacer(
                modifier = Modifier
                    .padding(bottom = StageSpacing)
                    .alpha(0.5f)
                    .fillMaxWidth()
                    .height(TimeLineHeight)
                    .background(LocalContentColor.current)
            )

            PlaybackController(
                onPrev = { scope.launch { if (temp == state.currentIndex) viewModel.updateCurrentIndex(state.currentIndex - 1) } },
                onNext = { scope.launch { if (temp == state.currentIndex) viewModel.updateCurrentIndex(state.currentIndex + 1) } }
            )
        }

        MiniStage(
            queue = state.queue, index = state.currentIndex, isPlaying = state.isPlaying,
            onPrev = { scope.launch { if (temp == state.currentIndex) viewModel.updateCurrentIndex(state.currentIndex - 1) } },
            onPlay = { scope.launch { viewModel.preferences.updatePlayingState(!state.isPlaying) } },
            onNext = { scope.launch { if (temp == state.currentIndex) viewModel.updateCurrentIndex(state.currentIndex + 1) } }
        )
    }
}