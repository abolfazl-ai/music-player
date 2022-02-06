package com.example.compose.utils.image_loader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.os.Build
import androidx.annotation.RequiresApi
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.signature.ObjectKey
import com.example.compose.local.model.Song
import com.example.compose.utils.default_pictures.getDefaultCover

class EmbeddedSongGlideLoader(val context: Context) : ModelLoader<Song, Bitmap> {

    override fun buildLoadData(
        model: Song,
        width: Int,
        height: Int,
        options: Options
    ): ModelLoader.LoadData<Bitmap> =
        ModelLoader.LoadData(
            ObjectKey(model.path),
            EmbeddedSongFetcher(context, model)
        )

    override fun handles(model: Song): Boolean = true

}

class EmbeddedSongGlideLoaderFactory(val context: Context) : ModelLoaderFactory<Song, Bitmap> {
    override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<Song, Bitmap> =
        EmbeddedSongGlideLoader(context)

    override fun teardown() {}
}

class EmbeddedSongFetcher(val context: Context, val model: Song) :
    DataFetcher<Bitmap> {

    private val metadataRetriever = MediaMetadataRetriever()

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in Bitmap>) {
        metadataRetriever.setDataSource(model.path)
        val bitmap: Bitmap =
            metadataRetriever.embeddedPicture?.run {
                BitmapFactory.decodeByteArray(this, 0, size)
            } ?: model.getDefaultCover(context)

        callback.onDataReady(bitmap)
    }

    override fun getDataClass(): Class<Bitmap> = Bitmap::class.java

    override fun getDataSource(): DataSource = DataSource.LOCAL

    override fun cleanup() = metadataRetriever.release()

    override fun cancel() = metadataRetriever.release()

}
