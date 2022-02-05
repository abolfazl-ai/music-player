package com.example.compose.ui.composables.player_screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalContentAlpha
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
import com.example.compose.ui.composables.modifiers.reveal
import com.example.compose.utils.kotlin_extensions.compIn
import com.example.compose.utils.resources.*
import com.example.compose.viewmodel.MainViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@ExperimentalAnimationGraphicsApi
@ExperimentalPagerApi
@Composable
fun PlayerScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) = BoxWithConstraints(modifier.fillMaxSize()) {

    val state by viewModel.playerScreenState.collectAsState()

    val pageState = rememberPagerState()

    CompositionLocalProvider(
        LocalContentColor provides animateColorAsState(state.color.front, tween(1000)).value,
        LocalContentAlpha provides 1f
    ) {
        Column(
            Modifier.reveal(
                state.color.back,
                maxWidth + TimeLineHeight + FabSize / 2 + StageSpacing, startRadius = 28.dp
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CoverViewPager(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(Color.Black),
            )

            Spacer(
                modifier = Modifier
                    .padding(bottom = StageSpacing)
                    .alpha(0.5f)
                    .fillMaxWidth()
                    .height(TimeLineHeight)
                    .background(LocalContentColor.current)
            )

            PlaybackController(alpha = state.sheetProgress.compIn(0.75f, 0.9f))
        }
    }

    MiniPlayer()
}


@OptIn(ExperimentalMaterialApi::class)
@ExperimentalAnimationGraphicsApi
@Composable
fun MiniPlayer(modifier: Modifier = Modifier, viewModel: MainViewModel = hiltViewModel()) {

    val state by viewModel.miniPlayerState.collectAsState()
    val scope = rememberCoroutineScope()

    if (state.sheetProgress < 1) Surface(
        modifier
            .fillMaxSize()
            .alpha(1 - state.sheetProgress.compIn(0.2f, 0.3f)),
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        Box {
            Row(
                modifier = Modifier
                    .alpha(1 - state.sheetProgress.compIn(end = 0.05f))
                    .padding(start = 16.dp, end = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = state.song?.title ?: "No song is playing",
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = state.song?.artist ?: "Select a song to play",
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                }
                IconButton(modifier = Modifier.size(56.dp), onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Rounded.SkipPrevious,
                        contentDescription = null,
                    )
                }
                val a = AnimatedImageVector.animatedVectorResource(R.drawable.play_to_pause)
                IconButton(modifier = Modifier
                    .height(56.dp)
                    .width(32.dp),
                    onClick = {
                        scope.launch {
                            viewModel.preferences.updatePlayingState(!state.isPlaying)
                        }
                    }) {
                    Icon(
                        painter = rememberAnimatedVectorPainter(a, state.isPlaying),
                        contentDescription = "PlayButton",
                    )
                }
                IconButton(modifier = Modifier.size(56.dp), onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Rounded.SkipNext,
                        contentDescription = null,
                    )
                }
            }
        }
    }
}