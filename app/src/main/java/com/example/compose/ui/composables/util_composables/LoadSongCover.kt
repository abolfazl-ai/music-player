package com.example.compose.ui.composables.util_composables

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import coil.imageLoader
import coil.request.ImageRequest
import coil.size.Size
import com.example.compose.local.model.Song
import com.example.compose.utils.image_loader.CoilSongFetcher
import kotlinx.coroutines.launch


@Composable
fun LoadSongCover(
    modifier: Modifier = Modifier, size: Size,
    song: Song, memoryCacheKey: String = song.path,
    placeHolder: @Composable () -> Unit = {},
    onSuccess: suspend (Drawable) -> Unit = {}
) = Box(modifier) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val imageLoader = LocalContext.current.imageLoader

    val request = ImageRequest.Builder(context).size(size)
        .data(song).memoryCacheKey(memoryCacheKey).fetcherFactory(CoilSongFetcher.Factory).build()
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