package com.example.compose.utils.image_loader

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.example.compose.utils.default_pictures.AlbumAndSize
import com.example.compose.utils.default_pictures.SongAndSize

@GlideModule
class GlideModule : AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.prepend(
            SongAndSize::class.java,
            Bitmap::class.java,
            SongGlideLoaderFactory(context)
        )
        registry.prepend(
            ArtistAndSize::class.java,
            Bitmap::class.java,
            ArtistGlideLoaderFactory(context)
        )
        registry.prepend(
            AlbumAndSize::class.java,
            Bitmap::class.java,
            AlbumGlideLoaderFactory(context)
        )
    }
}