package com.example.compose.utils.image_loader

import android.content.ContentUris
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Size
import androidx.core.graphics.drawable.toDrawable
import coil.bitmap.BitmapPool
import coil.decode.DataSource
import coil.fetch.DrawableResult
import coil.fetch.FetchResult
import coil.fetch.Fetcher
import coil.size.PixelSize
import com.example.compose.local.model.Album
import com.example.compose.utils.default_pictures.AlbumAndSize
import com.example.compose.utils.default_pictures.getDefaultCover

object CoilAlbumFetcher : Fetcher<Album> {

    override fun handles(data: Album) = true

    override fun key(data: Album) = data.id.toString()

    override suspend fun fetch(pool: BitmapPool, data: Album, size: coil.size.Size, options: coil.decode.Options): FetchResult {

        val actualSize = if (size is PixelSize) Size(size.width, size.height) else Size(500, 500)

        val bitmap: Bitmap = try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val uri = ContentUris.withAppendedId(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, data.id)
                options.context.contentResolver.loadThumbnail(uri, actualSize, null)
            } else {
                val uri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), data.id)
                MediaStore.Images.Media.getBitmap(options.context.contentResolver, uri)
            }
        } catch (e: Exception) {
            AlbumAndSize(data, actualSize).getDefaultCover(options.context)
        }

        return DrawableResult(
            drawable = bitmap.toDrawable(options.context.resources),
            isSampled = false,
            dataSource = DataSource.DISK
        )
    }
}