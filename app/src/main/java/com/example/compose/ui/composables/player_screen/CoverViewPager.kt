package com.example.compose.ui.composables.player_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.palette.graphics.Palette
import com.example.compose.local.model.Song
import com.example.compose.ui.composables.modifiers.crossFade
import com.example.compose.utils.util_classes.MainColors
import com.example.compose.utils.kotlin_extensions.getAccurateColor
import com.example.compose.viewmodel.MainViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@ExperimentalPagerApi
@Composable
fun CoverViewPager(
    modifier: Modifier,
    pagerState: PagerState,
    songList: List<Song>,
    onPageCreated: (index: Int, colors: MainColors) -> Unit = { _, _ -> },
    viewModel: MainViewModel = viewModel()
) = HorizontalPager(modifier = modifier, state = pagerState, count = songList.size) { page ->

/*    LaunchedEffect(key1 = viewModel.serviceController.currentIndex.value) {
        launch {
            viewModel.serviceController.currentIndex.collect {
                if (it != pagerState.currentPage)
                    pagerState.animateScrollToPage(it)
            }
        }
    }*/

    GlideImage(
        modifier = Modifier.crossFade(calculateCurrentOffsetForPage(page)),
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
                    modifier = Modifier.fillMaxSize(),
                    bitmap = success.drawable!!.toBitmap().asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                )
            }
        }
    )
}