package com.example.compose.utils.image_loader

import android.content.ContentUris
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toDrawable
import coil.ImageLoader
import coil.decode.DataSource
import coil.decode.ImageSource
import coil.fetch.*
import coil.request.Options
import coil.size.pxOrElse
import com.example.compose.local.model.Album
import com.example.compose.utils.default_pictures.AlbumAndSize
import com.example.compose.utils.default_pictures.getDefaultCover
import com.example.compose.utils.resources.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.buffer
import okio.source
import java.io.InputStream

internal class CoilAlbumFetcher(private val data: Album, private val options: Options) : Fetcher {

    override suspend fun fetch(): FetchResult {

        val actualSize = Size(options.size.width.pxOrElse { 500 }, options.size.height.pxOrElse { 500 })
        var stream: InputStream? = null

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val uri = ContentUris.withAppendedId(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, data.id)
                val bitmap = options.context.contentResolver.loadThumbnail(uri, actualSize, null)
                return DrawableResult(
                    drawable = bitmap.toDrawable(options.context.resources),
                    isSampled = false,
                    dataSource = DataSource.DISK
                )
            } else {
                val uri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), data.id)
                stream = options.context.contentResolver.openInputStream(uri)
                stream?.let {
                    return SourceResult(
                        source = ImageSource(it.source().buffer(), options.context),
                        mimeType = options.context.contentResolver.getType(uri),
                        dataSource = DataSource.DISK
                    )
                }
            }
        } catch (e: Exception) {
            Log.i(TAG, e.message.toString())
        } finally {
            withContext(Dispatchers.IO) { stream?.close() }
        }

        return DrawableResult(
            drawable = AlbumAndSize(data, actualSize).getDefaultCover(options.context).toDrawable(options.context.resources),
            isSampled = false,
            dataSource = DataSource.DISK
        )
    }

    object Factory : Fetcher.Factory<Album> {
        override fun create(data: Album, options: Options, imageLoader: ImageLoader): Fetcher {
            return CoilAlbumFetcher(data, options)
        }
    }
}