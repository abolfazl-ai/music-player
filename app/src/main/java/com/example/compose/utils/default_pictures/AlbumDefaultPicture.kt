package com.example.compose.utils.default_pictures

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Typeface
import android.util.Size
import com.example.compose.R
import com.example.compose.local.model.Album
import com.example.compose.utils.kotlin_extensions.decodeSampledBitmapFromResource

val AlbumCoverList = listOf(
    R.drawable.album_1,
    R.drawable.album_2,
    R.drawable.album_3,
    R.drawable.album_4,
    R.drawable.album_5,
    R.drawable.album_6,
)

data class AlbumAndSize(val album: Album, val size: Size)

fun AlbumAndSize.getDefaultCover(c: Context): Bitmap {

    val drawable = AlbumCoverList[(album.id % 6).toInt()]
    val name = if (album.name.trim().length < 23) album.name.trim() else album.name.trim()
        .substring(0, 20) + "..."
    val artist =
        if (album.artist.trim().length < 16) album.artist.trim() else album.artist.trim()
            .substring(0, 13) + "..."

    val bitmap = c.decodeSampledBitmapFromResource(drawable, size.width, size.height)

    Canvas(bitmap).apply {

        val paint = android.graphics.Paint().apply {
            isAntiAlias = true
            textSize = bitmap.width / 18f
            color = android.graphics.Color.WHITE
            typeface = Typeface.DEFAULT_BOLD
        }

        drawText(name, bitmap.width / 20f, bitmap.height / 10f, paint)

        paint.textSize = bitmap.width / 24f

        drawText(artist, bitmap.width / 8f, bitmap.height / 5.5f, paint)

        paint.apply {
            alpha = 200
            typeface = Typeface.DEFAULT
        }

        drawText("by", bitmap.width / 20f, bitmap.height / 5.5f, paint)

    }

    return bitmap
}
