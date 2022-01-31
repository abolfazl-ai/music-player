package com.example.compose.ui.composables.player_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.palette.graphics.Palette
import com.example.compose.ui.composables.modifiers.crossFade
import com.example.compose.utils.kotlin_extensions.PALETTE_TARGET_PRIMARY
import com.example.compose.utils.kotlin_extensions.PALETTE_TARGET_SECONDARY
import com.example.compose.utils.kotlin_extensions.getAccurateColor
import com.example.compose.utils.util_classes.MainColors
import com.example.compose.viewmodel.MainViewModel
import com.google.accompanist.pager.*
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@ExperimentalPagerApi
@Composable
fun CoverViewPager(
    modifier: Modifier = Modifier,
    pagerState: PagerState = rememberPagerState(),
    onPageCreated: (index: Int, colors: MainColors) -> Unit = { _, _ -> },
    viewModel: MainViewModel = hiltViewModel()
) {

    val songList = viewModel.repository.allSongs.collectAsState(emptyList()).value

    HorizontalPager(modifier = modifier, state = pagerState, count = songList.size) { page ->

        GlideImage(
            modifier = Modifier.crossFade(calculateCurrentOffsetForPage(page)),
            imageModel = songList[page],
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