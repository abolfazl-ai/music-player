package com.example.compose.ui.composables.stage

import androidx.compose.animation.*
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.compose.R
import com.example.compose.viewmodel.MainViewModel

@OptIn(ExperimentalAnimationGraphicsApi::class, ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun MiniStage(onPrev: () -> Unit = {}, onPlay: () -> Unit = {}, onNext: () -> Unit = {}, viewModel: MainViewModel = hiltViewModel()) {
    val state by viewModel.miniStageState.collectAsState()

    if (state.show) Surface(
        Modifier.fillMaxSize().alpha(state.alpha), color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        Box {
            Row(Modifier.alpha(state.contentAlpha).padding(start = 16.dp, end = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                AnimatedContent(modifier = Modifier.weight(1f), targetState = state.currentIndex,
                    transitionSpec = {
                        (if (targetState > initialState) slideInHorizontally { width -> width / 4 } + fadeIn()
                                with slideOutHorizontally { width -> -width / 4 } + fadeOut()
                        else slideInHorizontally { width -> -width / 4 } + fadeIn() with slideOutHorizontally { width -> width / 4 } + fadeOut()
                                ).using(SizeTransform(clip = false))
                    }
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = state.queue.getOrNull(it)?.title ?: "No song is playing",
                            style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = state.queue.getOrNull(it)?.artist ?: "Select a song to play",
                            style = MaterialTheme.typography.labelSmall, maxLines = 1, overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                IconButton(modifier = Modifier.size(56.dp), onClick = onPrev) {
                    Icon(imageVector = Icons.Rounded.SkipPrevious, contentDescription = "Previous")
                }
                IconButton(modifier = Modifier.size(56.dp, 32.dp), onClick = onPlay) {
                    val a = AnimatedImageVector.animatedVectorResource(R.drawable.play_to_pause)
                    Icon(painter = rememberAnimatedVectorPainter(a, state.isPlaying), contentDescription = "Play")
                }
                IconButton(modifier = Modifier.size(56.dp), onClick = onNext) {
                    Icon(imageVector = Icons.Rounded.SkipNext, contentDescription = "Next")
                }
            }
        }
    }
}