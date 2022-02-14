package com.example.compose.utils.image_loader

import android.content.ContentUris
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Size
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toDrawable
import coil.bitmap.BitmapPool
import coil.decode.DataSource
import coil.fetch.DrawableResult
import coil.fetch.FetchResult
import coil.fetch.Fetcher
import coil.size.PixelSize
import com.example.compose.local.model.Song
import com.example.compose.utils.default_pictures.SongAndSize
import com.example.compose.utils.default_pictures.getDefaultCover
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object CoilSongFetcher : Fetcher<Song> {

    override fun handles(data: Song) = true

    override fun key(data: Song) = data.path

    override suspend fun fetch(pool: BitmapPool, data: Song, size: coil.size.Size, options: coil.decode.Options): FetchResult {

        val actualSize = if (size is PixelSize) Size(size.width, size.height) else Size(500, 500)

        val bitmap: Bitmap = withContext(Dispatchers.IO) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val uri = Uri.parse("content://media/external/audio/media/" + data.id)
                    options.context.contentResolver.loadThumbnail(uri, actualSize, null)
                } else {
                    val uri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), data.albumId)
                    MediaStore.Images.Media.getBitmap(options.context.contentResolver, uri)
                }
            } catch (e: Exception) {
                SongAndSize(data, actualSize).getDefaultCover(options.context)
            }
        }

        return DrawableResult(
            drawable = bitmap.toDrawable(options.context.resources),
            isSampled = false,
            dataSource = DataSource.DISK
        )
    }
}