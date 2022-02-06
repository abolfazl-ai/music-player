package com.example.compose.ui.composables.player_screen

import android.util.Log
import android.util.Size
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import com.example.compose.local.model.Song
import com.example.compose.ui.composables.modifiers.crossFade
import com.example.compose.utils.default_pictures.SongAndSize
import com.example.compose.utils.kotlin_extensions.PALETTE_TARGET_PRIMARY
import com.example.compose.utils.kotlin_extensions.PALETTE_TARGET_SECONDARY
import com.example.compose.utils.kotlin_extensions.getAccurateColor
import com.example.compose.utils.resources.TAG
import com.example.compose.utils.util_classes.MainColors
import com.google.accompanist.pager.*
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CoverViewPager(
    modifier: Modifier = Modifier, size: Size, queue: List<Song>, currentIndex: Int,
    onPageChanged: (Int) -> Unit, onPageCreated: (Int, MainColors) -> Unit,
    pagerState: PagerState = rememberPagerState(remember { currentIndex }),
) {
    Log.e(TAG, "CoverViewPager Recreated")
    var tempIndex by remember { mutableStateOf(currentIndex) }

    LaunchedEffect(currentIndex, queue) {
        if (currentIndex != tempIndex)
            if (0 <= currentIndex && currentIndex < pagerState.pageCount) {
                tempIndex = currentIndex
                pagerState.animateScrollToPage(currentIndex)
            }
    }
    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage != tempIndex) {
            tempIndex = pagerState.currentPage
            onPageChanged(pagerState.currentPage)
        }
    }

    HorizontalPager(modifier = modifier, state = pagerState, count = queue.size, key = { queue[it].id }) { page ->

        GlideImage(
            modifier = Modifier.crossFade(calculateCurrentOffsetForPage(page)),
            imageModel = SongAndSize(queue[page], size),
            success = { success ->
                if (success.drawable != null) {
                    LaunchedEffect(success) {
                        launch {
                            Palette.Builder(success.drawable!!.toBitmap())
                                .addTarget(PALETTE_TARGET_PRIMARY)
                                .addTarget(PALETTE_TARGET_SECONDARY)
                                .generate { onPageCreated(page, it.getAccurateColor()) }
                        }
                    }

                    Image(
                        modifier = Modifier.fillMaxSize(),
                        bitmap = success.drawable!!.toBitmap().asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                    )
                }
            }
        )
    }
}