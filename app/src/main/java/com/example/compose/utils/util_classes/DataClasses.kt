package com.example.compose.utils.util_classes

import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.ui.graphics.Color

data class MainColors(val back: Color, val front: Color)
val DefaultMainColors = MainColors(back = Color(0xFF243144), front = Color(0xBEFFFFFF))

data class Path4dVector(val v1: Float, val v2: Float, val v3: Float, val v4: Float) {
    companion object {
        val VECTOR_CONVERTER: TwoWayConverter<Path4dVector, AnimationVector4D> =
            TwoWayConverter(
                { AnimationVector4D(it.v1, it.v2, it.v3, it.v4) },
                { Path4dVector(it.v1, it.v2, it.v3, it.v4) })
    }
}