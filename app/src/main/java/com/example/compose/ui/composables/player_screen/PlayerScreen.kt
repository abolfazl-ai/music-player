package com.example.compose.ui.composables.player_screen

import androidx.collection.LruCache
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.R
import com.example.compose.ui.composables.modifiers.reveal
import com.example.compose.utils.kotlin_extensions.compIn
import com.example.compose.utils.resources.PlayerScreenSpacing
import com.example.compose.utils.util_classes.MainColors
import com.example.compose.viewmodel.MainViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState

@ExperimentalMaterialApi
@ExperimentalAnimationGraphicsApi
@ExperimentalPagerApi
@Composable
fun PlayerScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel(),
    progress: () -> Float
) {

    val songs = viewModel.repository.getSongs().collectAsState(emptyList()).value

    val colorCache = remember { LruCache<Int, MainColors>(20) }

    val pageState = rememberPagerState()

    Column(
        modifier
            .fillMaxSize()
            .reveal(colorCache[pageState.currentPage]?.back ?: Color.Black, 404.dp, 750),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CoverViewPager(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(Color.Black),
            pagerState = pageState,
            songList = songs,
            onPageCreated = { i, c -> if (colorCache[i] == null) colorCache.put(i, c) }
        )

        CompositionLocalProvider(
            LocalContentColor provides animateColorAsState(
                colorCache[pageState.currentPage]?.front ?: Color.White
            ).value, LocalContentAlpha provides 1f
        ) {
            Row(
                Modifier
                    .alpha(progress().compIn(0.75f, 0.9f))
                    .padding(top = PlayerScreenSpacing),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = Icons.Rounded.Repeat,
                        contentDescription = null,
                    )
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        modifier = Modifier.size(44.dp),
                        imageVector = Icons.Rounded.ChevronLeft,
                        contentDescription = null,
                    )
                }
                Spacer(modifier = Modifier.size(56.dp))
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        modifier = Modifier.size(44.dp),
                        imageVector = Icons.Rounded.ChevronRight,
                        contentDescription = null,
                    )
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = Icons.Rounded.Shuffle,
                        contentDescription = null,
                    )
                }
            }
        }
    }

    progress().let {
        if (it < 1f) Surface(
            Modifier.alpha(1 - it.compIn(0.3f, 0.4f)),
            color = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ) {
            MiniPlayer(Modifier.alpha(1 - it.compIn(end = 0.1f)))
        }
    }
}

@ExperimentalAnimationGraphicsApi
@Preview
@Composable
fun MiniPlayer(modifier: Modifier = Modifier) = Box(modifier) {
    var isPlaying by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.padding(start = 16.dp, end = 4.dp),
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
        IconButton(modifier = Modifier
            .height(56.dp)
            .width(32.dp), onClick = { isPlaying = !isPlaying }) {
            Icon(
                painter = animatedVectorResource(R.drawable.play_to_pause).painterFor(atEnd = isPlaying),
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