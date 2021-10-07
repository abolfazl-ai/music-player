package com.example.compose.ui.composables.player_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.palette.graphics.Palette
import com.example.compose.local.model.Song
import com.example.compose.viewmodel.MainViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@ExperimentalMaterialApi
@ExperimentalPagerApi
@Composable
fun CoverViewPager(
    modifier: Modifier,
    pagerState: PagerState,
    songList: List<Song>,
    onPageCreated: (index: Int, colors: MainColors) -> Unit = { _, _ -> },
    viewModel: MainViewModel = viewModel()
) {

    LaunchedEffect(key1 = viewModel.serviceController.currentIndex.value) {
        launch {
            viewModel.serviceController.currentIndex.collect {
                if (it != pagerState.currentPage)
                    pagerState.animateScrollToPage(it)
            }
        }
    }

    HorizontalPager(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        state = pagerState, count = pagerState.pageCount
    ) { page ->

        var width = remember { 0 }

        GlideImage(
            modifier = Modifier.fillMaxSize(),
            imageModel = songList[page],
            success = { success ->
                if (success.drawable != null) {
                    LaunchedEffect(success) {
                        launch {
                            Palette.Builder(success.drawable!!.toBitmap())
                                .resizeBitmapArea(0)
                                .clearFilters()
                                .maximumColorCount(8)
                                .generate { onPageCreated(page, it.getAccurateColor()) }
                        }
                    }

                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .onGloballyPositioned { c -> width = c.size.width }
                            .graphicsLayer {
                                val pageOffset = calculateCurrentOffsetForPage(page)
                                translationX = pageOffset.coerceIn(-1f, 1f) * .9f * width
                                alpha = 1 - 1.5f * pageOffset.absoluteValue.coerceIn(0f, 1f)
                            },
                        bitmap = success.drawable!!.toBitmap().asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                    )
                }
            }
        )
    }
}

fun Color.contrastAgainst(background: Color): Float {
    val fg = if (alpha < 1f) compositeOver(background) else this

    val fgLuminance = fg.luminance() + 0.05f
    val bgLuminance = background.luminance() + 0.05f

    return kotlin.math.max(fgLuminance, bgLuminance) / kotlin.math.min(fgLuminance, bgLuminance)
}