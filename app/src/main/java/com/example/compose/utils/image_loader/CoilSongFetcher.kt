package com.example.compose.utils.image_loader

import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
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
import com.example.compose.local.model.Song
import com.example.compose.utils.default_pictures.SongAndSize
import com.example.compose.utils.default_pictures.getDefaultCover
import com.example.compose.utils.resources.TAG
import okio.buffer
import okio.source
import java.io.InputStream

object CoilSongFetcher : Fetcher<Song> {

    override fun handles(data: Song) = true

    override fun key(data: Song) = data.path

    override suspend fun fetch(pool: BitmapPool, data: Song, size: coil.size.Size, options: coil.decode.Options): FetchResult {

        val actualSize = if (size is PixelSize) Size(size.width, size.height) else Size(500, 500)

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
            drawable = SongAndSize(data, actualSize).getDefaultCover(options.context).toDrawable(options.context.resources),
            isSampled = false,
            dataSource = DataSource.DISK
        )
    }
}