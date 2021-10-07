package com.example.compose.ui.composables.player_screen

import android.util.Log
import androidx.collection.LruCache
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.viewmodel.MainViewModel
import com.example.compose.ui.theme.Purple500
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@ExperimentalAnimationGraphicsApi
@ExperimentalPagerApi
@ExperimentalMaterialApi
@Composable
fun PlayerScreen(
    modifier: Modifier = Modifier,
    progress: Float,
    viewModel: MainViewModel = viewModel()
) {

    val songs = viewModel.serviceController.playlist.collectAsState().value

    var isInit by remember { mutableStateOf(false) }

    val colorCache = remember { LruCache<Int, MainColors>(12) }

    val pageState = rememberPagerState()

    LaunchedEffect(key1 = pageState.currentPage) {
        launch { viewModel.serviceController.seekTo(pageState.currentPage, 0L) }
    }

    Box(
        modifier
            .fillMaxSize()
            .alpha(4 * (progress.coerceIn(0.25f, 0.5f) - 0.25f))
            .background(Color.Black)
    ) {

        if (!isInit || colorCache.size() > 0) {
            CoverViewPager(
                modifier = Modifier.fillMaxWidth(),
                pagerState = pageState,
                songList = songs,
                onPageCreated = { i, c ->
                    colorCache.put(i, c)
                    Log.e("Abolfazl", "PlayerScreen: $i , ${c.back}")
                    if (!isInit) isInit = colorCache[pageState.currentPage] != null
                }
            )

            if (isInit)
                Column(Modifier.fillMaxSize()) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(2 - progress)
                    )

                    ColorReveal(
                        modifier = Modifier.fillMaxSize(),
                        color = colorCache[pageState.currentPage]?.back ?: Purple500,
                        y = 68.dp
                    ) {
                        PlaybackController(
                            Modifier.alpha(4 * (progress.coerceIn(0.5f, 0.75f) - 0.5f)),
                            colorCache[pageState.currentPage]?.front ?: Color.White
                        )
                    }
                }
        }

        if (progress < 1f)
            Spacer(
                modifier = modifier
                    .fillMaxSize()
                    .clickable(
                        indication = null,
                        onClick = {},
                        interactionSource = remember { MutableInteractionSource() })
            )
    }
}