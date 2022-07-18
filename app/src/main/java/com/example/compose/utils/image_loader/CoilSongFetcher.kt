package com.example.compose.utils.image_loader

import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.util.Log
import android.util.Size
import androidx.core.graphics.drawable.toDrawable
import coil.ImageLoader
import coil.decode.DataSource
import coil.decode.ImageSource
import coil.fetch.DrawableResult
import coil.fetch.FetchResult
import coil.fetch.Fetcher
import coil.fetch.SourceResult
import coil.request.Options
import com.example.compose.local.model.Song
import com.example.compose.utils.default_pictures.SongAndSize
import com.example.compose.utils.default_pictures.getDefaultCover
import com.example.compose.utils.resources.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.buffer
import okio.source
import java.io.InputStream

class CoilSongFetcher(val data: Song, val options: Options) : Fetcher {
    override suspend fun fetch(): FetchResult {

        val actualSize = options.size.run { if (width.hashCode() == 0) Size(width.hashCode(), height.hashCode()) else Size(500, 500) }
        var stream: InputStream? = null

        try {
            if (SDK_INT >= Build.VERSION_CODES.Q) {
                val uri = Uri.parse("content://media/external/audio/media/" + data.id)
                val bitmap = options.context.contentResolver.loadThumbnail(uri, actualSize, null)
                return DrawableResult(
                    drawable = bitmap.toDrawable(options.context.resources),
                    isSampled = false,
                    dataSource = DataSource.DISK
                )
            } else {
                val uri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), data.albumId)
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
            drawable = SongAndSize(data, actualSize).getDefaultCover(options.context).toDrawable(options.context.resources),
            isSampled = false,
            dataSource = DataSource.DISK
        )
    }

    object Factory : Fetcher.Factory<Song> {
        override fun create(data: Song, options: Options, imageLoader: ImageLoader) = CoilSongFetcher(data, options)
    }
}