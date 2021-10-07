package com.example.compose.ui.composables.screens

import android.util.Size
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.example.compose.local.model.Album
import com.example.compose.utils.default_pictures.AlbumAndSize
import com.example.compose.utils.default_pictures.getDefaultCover

val HomeScreen = @Composable { modifier: Modifier ->
    Box(modifier.fillMaxSize()) {
/*        val a = remember {
            AlbumAndSize(Album(0, "My heart wants what it wants", 0, "Selena Gomez and The Scene", 6, 2012, 0L), Size(500, 500))
        }
        Image(modifier = Modifier.fillMaxWidth().aspectRatio(1f),
            bitmap = a.getDefaultCover(LocalContext.current).asImageBitmap(),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )*/
    }
}