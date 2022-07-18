package com.example.compose.ui.composables.stage

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.size.Size
import com.example.compose.local.model.Song
import com.example.compose.ui.composables.modifiers.crossFade
import com.example.compose.ui.composables.util_composables.LoadSongCover
import com.example.compose.utils.kotlin_extensions.PALETTE_TARGET_PRIMARY
import com.example.compose.utils.kotlin_extensions.PALETTE_TARGET_SECONDARY
import com.example.compose.utils.kotlin_extensions.getAccurateColor
import com.example.compose.utils.util_classes.MainColors
import com.google.accompanist.pager.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CoverViewPager(
    modifier: Modifier = Modifier, queue: List<Song>, currentIndex: Int, onPageChanged: (Int) -> Unit,
    onPageCreated: (Int, MainColors) -> Unit,
) = BoxWithConstraints(modifier) {

    val pagerState: PagerState = rememberPagerState(remember { currentIndex })
    val scope = rememberCoroutineScope()

    LaunchedEffect(currentIndex) {
        scope.launch {
            if (currentIndex != pagerState.currentPage)
                if (0 <= currentIndex && currentIndex < pagerState.pageCount)
                    pagerState.animateScrollToPage(currentIndex)
        }
    }

//    LaunchedEffect(pagerState) { snapshotFlow { pagerState.currentPage }.collect { onPageChanged(it) } }

    pagerState.run { LaunchedEffect(currentPageOffset) { if (currentPageOffset < 0.05f) onPageChanged(currentPage) } }

    HorizontalPager(modifier = Modifier.fillMaxSize(), state = pagerState, count = queue.size, key = { queue[it].id }) { page ->
        LoadSongCover(
            modifier = Modifier.crossFade(calculateCurrentOffsetForPage(page)), song = queue[page],
            size = Size(constraints.maxWidth, constraints.minHeight), memoryCacheKey = queue[page].path + " Stage"
        ) {
            withContext(Dispatchers.Default) {
                Palette.Builder(it.toBitmap())
                    .addTarget(PALETTE_TARGET_PRIMARY)
                    .addTarget(PALETTE_TARGET_SECONDARY)
                    .generate { onPageCreated(page, it.getAccurateColor()) }
            }
        }
    }
}
