package com.example.compose.ui.composables.player_screen

import android.util.Log
import android.util.Size
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.compose.R
import com.example.compose.local.model.Song
import com.example.compose.ui.composables.modifiers.reveal
import com.example.compose.utils.kotlin_extensions.compIn
import com.example.compose.utils.resources.*
import com.example.compose.viewmodel.MainViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@Composable
fun Stage(modifier: Modifier = Modifier, viewModel: MainViewModel = hiltViewModel()) = BoxWithConstraints(modifier.fillMaxSize()) {

    val state by viewModel.stageState.collectAsState()
    val scope = rememberCoroutineScope()

    val size = remember { Size(constraints.maxWidth / 3, constraints.maxWidth / 3) }

    Log.e(TAG, "Stage Recreated")

    if (state.initialized)
        CompositionLocalProvider(LocalContentColor provides animateColorAsState(state.color.front, tween(1000)).value) {
            Column(
                Modifier.reveal(state.color.back, remember(maxWidth) { maxWidth + TimeLineHeight + FabSize / 2 + StageSpacing }, 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                CoverViewPager(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .background(Color.Black),
                    size = size, queue = state.queue, currentIndex = state.currentIndex,
                    onPageChanged = { scope.launch { viewModel.updateCurrentSongIndex(it) } },
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
                    onPrevious = { scope.launch { viewModel.updateCurrentSongIndex(state.currentIndex - 1) } },
                    onNext = { scope.launch { viewModel.updateCurrentSongIndex(state.currentIndex + 1) } }
                )
            }
        }

    MiniStage(
        song = state.queue.getOrNull(state.currentIndex), isPlaying = state.isPlaying,
        onPrevious = { scope.launch { viewModel.updateCurrentSongIndex(state.currentIndex - 1) } },
        onPlay = { scope.launch { viewModel.preferences.updatePlayingState(!state.isPlaying) } },
        onNext = { scope.launch { viewModel.updateCurrentSongIndex(state.currentIndex + 1) } }
    )
}


@OptIn(ExperimentalAnimationGraphicsApi::class, ExperimentalMaterialApi::class)
@Composable
fun MiniStage(
    modifier: Modifier = Modifier, song: Song?, isPlaying: Boolean, onPrevious: () -> Unit = {},
    onPlay: () -> Unit = {}, onNext: () -> Unit = {}, viewModel: MainViewModel = hiltViewModel()
) {
    Log.e(TAG, "MiniStage Recreated")

    val sheetProgress by viewModel.stageSheetProgress.collectAsState()

    if (sheetProgress < 1) Surface(
        modifier
            .fillMaxSize()
            .alpha(1 - sheetProgress.compIn(0.2f, 0.3f)), color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        Box {
            Row(
                modifier = Modifier
                    .alpha(1 - sheetProgress.compIn(end = 0.05f))
                    .padding(start = 16.dp, end = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = song?.title ?: "No song is playing",
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = song?.artist ?: "Select a song to play",
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                }
                IconButton(modifier = Modifier.size(56.dp), onClick = onPrevious) {
                    Icon(
                        imageVector = Icons.Rounded.SkipPrevious,
                        contentDescription = null,
                    )
                }
                val a = AnimatedImageVector.animatedVectorResource(R.drawable.play_to_pause)
                IconButton(
                    modifier = Modifier
                        .height(56.dp)
                        .width(32.dp),
                    onClick = onPlay
                ) {
                    Icon(
                        painter = rememberAnimatedVectorPainter(a, isPlaying),
                        contentDescription = "PlayButton",
                    )
                }
                IconButton(modifier = Modifier.size(56.dp), onClick = onNext) {
                    Icon(
                        imageVector = Icons.Rounded.SkipNext,
                        contentDescription = null,
                    )
                }
            }
        }
    }
}