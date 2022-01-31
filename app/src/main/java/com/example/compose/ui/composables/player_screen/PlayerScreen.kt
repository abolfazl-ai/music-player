package com.example.compose.ui.composables.player_screen

import androidx.collection.LruCache
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
import com.example.compose.R
import com.example.compose.ui.composables.modifiers.reveal
import com.example.compose.utils.kotlin_extensions.compIn
import com.example.compose.utils.resources.*
import com.example.compose.utils.util_classes.MainColors
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState

@ExperimentalMaterialApi
@ExperimentalAnimationGraphicsApi
@ExperimentalPagerApi
@Composable
fun PlayerScreen(
    modifier: Modifier = Modifier,
    progress: () -> Float
) = BoxWithConstraints(modifier.fillMaxSize()) {

    val colorCache = remember { LruCache<Int, MainColors>(20) }
    val pageState = rememberPagerState()

    CompositionLocalProvider(
        LocalContentColor provides animateColorAsState(
            colorCache[pageState.currentPage]?.front ?: Color.White,
            tween(1000)
        ).value,
        LocalContentAlpha provides 1f
    ) {
        Column(
            Modifier
                .reveal(
                    colorCache[pageState.currentPage]?.back ?: Color.Black,
                    maxWidth + ProgressBarHeight + FabSize / 2 + PlayerScreenSpacing//, 750
//                    , startRadius = 24.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CoverViewPager(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(Color.Black),
                pagerState = pageState,
                onPageCreated = remember {
                    { i, c ->
                        if (colorCache[i] == null) colorCache.put(
                            i,
                            c
                        )
                    }
                }
            )

            Spacer(
                modifier = Modifier
                    .padding(bottom = PlayerScreenSpacing)
                    .alpha(0.5f)
                    .fillMaxWidth()
                    .height(ProgressBarHeight)
                    .background(LocalContentColor.current)
            )

            PlaybackController(alpha = { progress().compIn(0.75f, 0.9f) })
        }
    }

    MiniPlayer(progress = progress())
}


@ExperimentalAnimationGraphicsApi
@Composable
fun MiniPlayer(modifier: Modifier = Modifier, progress: Float) {
    if (progress < 1) Surface(
        modifier
            .fillMaxSize()
            .alpha(1 - progress.compIn(0.2f, 0.3f)),
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        var isPlaying by remember { mutableStateOf(false) }
        Box {
            Row(
                modifier = Modifier
                    .alpha(1 - progress.compIn(end = 0.05f))
                    .padding(start = 16.dp, end = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "The heart wants what it wants and i",
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Selena Gomez",
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
                    .width(32.dp), onClick = { isPlaying = !isPlaying }) {
                    Icon(
                        painter = rememberAnimatedVectorPainter(a, isPlaying),
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