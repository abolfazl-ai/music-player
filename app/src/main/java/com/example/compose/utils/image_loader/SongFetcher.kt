package com.example.compose.utils.image_loader

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
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
import com.example.compose.utils.default_pictures.SongAndSize
import com.example.compose.utils.default_pictures.getDefaultCover
import com.example.compose.utils.resources.TAG

class SongGlideLoader(val context: Context) : ModelLoader<SongAndSize, Bitmap> {

    override fun buildLoadData(
        model: SongAndSize,
        width: Int,
        height: Int,
        options: Options
    ): ModelLoader.LoadData<Bitmap> =
        ModelLoader.LoadData(
            ObjectKey(model.song.path + model.size.width),
            SongFetcher(context, model)
        )

    override fun handles(model: SongAndSize): Boolean = model.size.width > 0

}

class SongGlideLoaderFactory(val context: Context) : ModelLoaderFactory<SongAndSize, Bitmap> {
    override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<SongAndSize, Bitmap> =
        SongGlideLoader(context)

    override fun teardown() {}
}

class SongFetcher(val context: Context, val model: SongAndSize) :
    DataFetcher<Bitmap> {

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in Bitmap>) {
        Log.e(TAG, "Glide fetch: ${model.song.title}")
        val bitmap: Bitmap = try {
            val uri = Uri.parse("content://media/external/audio/media/" + model.song.id)
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