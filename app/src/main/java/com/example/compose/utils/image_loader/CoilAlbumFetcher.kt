package com.example.compose.utils.image_loader

import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import androidx.core.graphics.drawable.toDrawable
import coil.bitmap.BitmapPool
import coil.decode.DataSource
import coil.fetch.DrawableResult
import coil.fetch.FetchResult
import coil.fetch.Fetcher
import coil.fetch.SourceResult
import coil.size.PixelSize
import com.example.compose.local.model.Album
import com.example.compose.utils.default_pictures.AlbumAndSize
import com.example.compose.utils.default_pictures.getDefaultCover
import com.example.compose.utils.resources.TAG
import okio.buffer
import okio.source
import java.io.InputStream

object CoilAlbumFetcher : Fetcher<Album> {

    override fun handles(data: Album) = true

    override fun key(data: Album) = data.id.toString()

    override suspend fun fetch(pool: BitmapPool, data: Album, size: coil.size.Size, options: coil.decode.Options): FetchResult {

        val actualSize = if (size is PixelSize) Size(size.width, size.height) else Size(500, 500)

        try {
            if (false) {
                val uri = ContentUris.withAppendedId(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, data.id)
                val bitmap = options.context.contentResolver.loadThumbnail(uri, actualSize, null)
                return DrawableResult(
                    drawable = bitmap.toDrawable(options.context.resources),
                    isSampled = false,
                    dataSource = DataSource.DISK
                )
            } else {
                val uri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), data.id)
                val stream: InputStream? = options.context.contentResolver.openInputStream(uri)
                stream?.let {
                    return SourceResult(
                        source = it.source().buffer(),
                        mimeType = options.context.contentResolver.getType(uri),
                        dataSource = DataSource.DISK
                    )
                }
                stream?.close()
            }
        } catch (e: Exception) {
            Log.i(TAG, e.message.toString())
        }

        return DrawableResult(
            drawable = AlbumAndSize(data, actualSize).getDefaultCover(options.context).toDrawable(options.context.resources),
            isSampled = false,
            dataSource = DataSource.DISK
        )
    }
}