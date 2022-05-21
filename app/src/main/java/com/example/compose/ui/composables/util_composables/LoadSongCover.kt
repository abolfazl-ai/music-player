package com.example.compose.ui.composables.util_composables

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import coil.compose.LocalImageLoader
import coil.request.ImageRequest
import coil.size.PixelSize
import com.example.compose.local.model.Song
import com.example.compose.utils.image_loader.CoilSongFetcher
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch


@Composable
fun LoadSongCover(
    modifier: Modifier = Modifier, size: PixelSize,
    song: Song, memoryCacheKey: String = song.path,
    placeHolder: @Composable () -> Unit = {},
    onSuccess: suspend (Drawable) -> Unit = {}
) = Box(modifier) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val imageLoader = LocalImageLoader.current

    val request = ImageRequest.Builder(context).size(size).data(song).memoryCacheKey(memoryCacheKey).fetcher(CoilSongFetcher).build()
    var drawable by remember(request) { mutableStateOf<Drawable?>(null) }

    LaunchedEffect(request) {
        launch { imageLoader.enqueue(request.newBuilder(context).target { scope.launch { onSuccess(it) }; drawable = it }.build()) }
    }

    if (drawable == null) placeHolder.invoke()
    drawable?.let {
        Image(
            modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop,
            bitmap = it.toBitmap().asImageBitmap(), contentDescription = song.title
        )
    }
}