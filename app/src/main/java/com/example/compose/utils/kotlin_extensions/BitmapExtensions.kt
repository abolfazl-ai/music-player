package com.example.compose.utils.kotlin_extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

fun Context.decodeSampledBitmapFromResource(resId: Int, reqWidth: Int, reqHeight: Int): Bitmap {
    return BitmapFactory.Options().run {
        inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, resId, this)
        inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)
        inJustDecodeBounds = false
        inMutable = true
        BitmapFactory.decodeResource(resources, resId, this)
    }
}

fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    val (height: Int, width: Int) = options.run { outHeight to outWidth }
    var inSampleSize = 1
    if (height > reqHeight || width > reqWidth) do {
        inSampleSize *= 2
    } while (width / inSampleSize >= reqWidth)
    return inSampleSize
}