package com.example.compose.utils.image_loader

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import android.util.Size
import androidx.annotation.RequiresApi
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.signature.ObjectKey
import com.example.compose.local.model.Artist


data class ArtistAndSize(val artist: Artist, val size: Size)

class ArtistGlideLoader(val context: Context) : ModelLoader<ArtistAndSize, Bitmap> {

    override fun buildLoadData(
        model: ArtistAndSize,
        width: Int,
        height: Int,
        options: Options
    ): ModelLoader.LoadData<Bitmap> =
        ModelLoader.LoadData(
            ObjectKey(model.artist.id),
            ArtistFetcher(context, model)
        )

    override fun handles(model: ArtistAndSize): Boolean = true

}

class ArtistGlideLoaderFactory(val context: Context) : ModelLoaderFactory<ArtistAndSize, Bitmap> {
    override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<ArtistAndSize, Bitmap> =
        ArtistGlideLoader(context)

    override fun teardown() {}
}

class ArtistFetcher(val context: Context, val model: ArtistAndSize) :
    DataFetcher<Bitmap> {

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in Bitmap>) {
        try {
            val uri = ContentUris.withAppendedId(
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                model.artist.id
            )
            val bitmap: Bitmap = context.contentResolver.loadThumbnail(uri, model.size, null)
            callback.onDataReady(bitmap)
        } catch (e: Exception) {
            callback.onLoadFailed(e)
        }
    }

    override fun getDataClass(): Class<Bitmap> = Bitmap::class.java

    override fun getDataSource(): DataSource = DataSource.LOCAL

    override fun cleanup() {}

    override fun cancel() {}

}