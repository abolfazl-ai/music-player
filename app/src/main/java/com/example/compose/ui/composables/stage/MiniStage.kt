package com.example.compose.ui.composables.stage

import androidx.compose.animation.*
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.compose.R
import com.example.compose.local.model.Song
import com.example.compose.ui.composables.util_composables.EmoIconButton
import com.example.compose.utils.util_classes.PlaybackAction
import com.example.compose.viewmodel.MainViewModel

@OptIn(ExperimentalAnimationGraphicsApi::class, ExperimentalAnimationApi::class)
@Composable
fun MiniStage(song: Song, index: Int, isPlaying: Boolean, alpha: Float, onAction: (PlaybackAction) -> Unit = {}) {

    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onPrimaryContainer) {
        Box(Modifier.alpha(alpha)) {
            Row(Modifier.padding(start = 16.dp, end = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                AnimatedContent(modifier = Modifier.weight(1f), targetState = index,
                    transitionSpec = {
                        (if (targetState > initialState) slideInHorizontally { width -> width / 4 } + fadeIn()
                                with slideOutHorizontally { width -> -width / 4 } + fadeOut()
                        else slideInHorizontally { width -> -width / 4 } + fadeIn() with slideOutHorizontally { width -> width / 4 } + fadeOut()
                                ).using(SizeTransform(clip = false))
                    }
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(song.title, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(song.artist, style = MaterialTheme.typography.labelSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }

                EmoIconButton(modifier = Modifier.size(56.dp), onClick = { onAction(PlaybackAction.PREVIOUS) }) {
                    Icon(imageVector = Icons.Rounded.SkipPrevious, contentDescription = "Previous")
                }
                EmoIconButton(modifier = Modifier.size(56.dp, 32.dp), onClick = { onAction(PlaybackAction.PREVIOUS) }) {
                    val a = AnimatedImageVector.animatedVectorResource(R.drawable.play_to_pause)
                    Icon(painter = rememberAnimatedVectorPainter(a, isPlaying), contentDescription = "Play")
                }
                EmoIconButton(modifier = Modifier.size(56.dp), onClick = { onAction(PlaybackAction.PREVIOUS) }) {
                    Icon(imageVector = Icons.Rounded.SkipNext, contentDescription = "Next")
                }
            }
        }
    }
}