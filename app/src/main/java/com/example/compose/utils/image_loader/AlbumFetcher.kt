package com.example.compose.utils.image_loader

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.signature.ObjectKey
import com.example.compose.utils.default_pictures.AlbumAndSize
import com.example.compose.utils.default_pictures.getDefaultCover
import com.example.compose.utils.resources.TAG

class AlbumGlideLoader(val context: Context) : ModelLoader<AlbumAndSize, Bitmap> {

    override fun buildLoadData(
        model: AlbumAndSize,
        width: Int,
        height: Int,
        options: Options
    ): ModelLoader.LoadData<Bitmap> =
        ModelLoader.LoadData(
            ObjectKey(model.album.id),
            AlbumFetcher(context, model)
        )

    override fun handles(model: AlbumAndSize): Boolean = true

}

class AlbumGlideLoaderFactory(val context: Context) : ModelLoaderFactory<AlbumAndSize, Bitmap> {
    override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<AlbumAndSize, Bitmap> =
        AlbumGlideLoader(context)

    override fun teardown() {}
}

class AlbumFetcher(val context: Context, val model: AlbumAndSize) :
    DataFetcher<Bitmap> {

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in Bitmap>) {
        val bitmap: Bitmap = try {
            val uri = ContentUris.withAppendedId(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                model.album.id
            )
            context.contentResolver.loadThumbnail(uri, model.size, null)
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            model.getDefaultCover(context)
        }
        callback.onDataReady(bitmap)
    }

    override fun getDataClass(): Class<Bitmap> = Bitmap::class.java

    override fun getDataSource(): DataSource = DataSource.LOCAL

    override fun cleanup() {}

    override fun cancel() {}

}
