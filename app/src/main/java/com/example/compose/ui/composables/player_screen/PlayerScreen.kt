package com.example.compose.ui.composables.player_screen

import androidx.collection.LruCache
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.viewmodel.MainViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState

@ExperimentalAnimationGraphicsApi
@ExperimentalPagerApi
@ExperimentalMaterialApi
@Composable
fun PlayerScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {

    val songs = viewModel.repository.getSongs().collectAsState(emptyList()).value

    val colorCache = remember { LruCache<Int, MainColors>(12) }

    val pageState = rememberPagerState()

/*    LaunchedEffect(key1 = pageState.currentPage) {
        launch { viewModel.serviceController.seekTo(pageState.currentPage, 0L) }
    }*/

    Column(
        modifier
            .fillMaxSize()
            .reveal(colorCache[pageState.currentPage]?.back ?: Color.Black, 404.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CoverViewPager(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(Color.Black),
            pagerState = pageState,
            songList = songs,
            onPageCreated = { i, c -> colorCache.put(i, c) }
        )

        Surface(
            Modifier
                .fillMaxSize()
                .padding(top = 88.dp)
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp),color = Color.White,
            elevation = 4.dp
        ) {

        }
    }
}