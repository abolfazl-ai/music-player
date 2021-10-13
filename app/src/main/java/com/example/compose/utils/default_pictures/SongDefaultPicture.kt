package com.example.compose.utils.default_pictures

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Typeface
import android.util.Size
import com.example.compose.R
import com.example.compose.local.model.Song
import com.example.compose.utils.decodeSampledBitmapFromResource

val CoverList = listOf(
    R.drawable.cover_1,
    R.drawable.cover_2,
    R.drawable.cover_3,
    R.drawable.cover_4,
    R.drawable.cover_5,
    R.drawable.cover_6,
)

data class SongAndSize(val song: Song, val size: Size)

fun SongAndSize.getDefaultCover(c: Context): Bitmap {

    val drawable = CoverList[(song.id % 6).toInt()]
    val title = if (song.title.length < 15) song.title else song.title.substring(0, 12) + "..."
    val artist = if (song.artist.length < 20) song.artist else song.artist.substring(0, 17) + "..."

    val bitmap = c.decodeSampledBitmapFromResource(drawable, size.width, size.height)

    Canvas(bitmap).apply {

        rotate(-45f, bitmap.width / 2f, bitmap.height / 2f)

        val paint = android.graphics.Paint().apply {
            isAntiAlias = true
            textSize = bitmap.width / 12f
            color = android.graphics.Color.WHITE
            typeface = Typeface.DEFAULT_BOLD
        }

        drawText(title, bitmap.width / 4.5f, bitmap.height / 1.95f, paint)

        paint.textSize = bitmap.width / 20f

        drawText(artist, bitmap.width / 3.2f, bitmap.height / 1.65f, paint)

        paint.apply {
            alpha = 200
            typeface = Typeface.DEFAULT
        }

        drawText("by", bitmap.width / 4.5f, bitmap.height / 1.65f, paint)

    }

    return bitmap
}

fun Song.getDefaultCover(c: Context): Bitmap = SongAndSize(this, Size(1000, 1000)).getDefaultCover(c)