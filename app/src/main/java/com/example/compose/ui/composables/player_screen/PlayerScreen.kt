package com.example.compose.ui.composables.player_screen

import android.util.Log
import androidx.collection.LruCache
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.ui.composables.modifiers.reveal
import com.example.compose.utils.kotlin_extensions.compIn
import com.example.compose.utils.resources.PlayerSheetPeekHeight
import com.example.compose.utils.resources.TAG
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

    val colorCache by remember { mutableStateOf(LruCache<Int, MainColors>(20)) }

    val pageState = rememberPagerState()

/*    LaunchedEffect(key1 = pageState.currentPage) {
        launch { viewModel.serviceController.seekTo(pageState.currentPage, 0L) }
    }*/

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
    }
    progress().let {
        if (it < 1f) Surface(Modifier.alpha(1 - it.compIn(0.3f, 0.5f))) {
            MiniPlayer(Modifier.alpha(1 - it.compIn(end = 0.1f)))
        }
    }
}

@Preview
@Composable
fun MiniPlayer(modifier: Modifier = Modifier) = Box(modifier) {
    Row(
        modifier = Modifier.padding(start = 16.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Column(
            Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = "The heart wants what it wants and i",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                maxLines = 1, overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Selena Gomez",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                maxLines = 1, overflow = TextOverflow.Ellipsis
            )
        }

        Box(
            modifier = Modifier
                .requiredHeight(PlayerSheetPeekHeight)
                .requiredWidth(PlayerSheetPeekHeight * 2.5f),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier
                    .requiredSize(70.dp)
                    .offset((-44).dp)
                    .clip(CircleShape)
                    .clickable { }
                    .padding(20.dp),
                imageVector = Icons.Rounded.SkipPrevious,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Icon(
                modifier = Modifier
                    .requiredSize(70.dp)
                    .offset(44.dp)
                    .clip(CircleShape)
                    .clickable { }
                    .padding(20.dp),
                imageVector = Icons.Rounded.SkipNext,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Icon(
                modifier = Modifier
                    .requiredSize(70.dp)
                    .clip(CircleShape)
                    .clickable { }
                    .padding(20.dp),
                imageVector = Icons.Rounded.PlayArrow,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}