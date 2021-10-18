package com.example.compose.utils.kotlin_extensions

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.luminance
import androidx.palette.graphics.Palette
import com.example.compose.utils.util_classes.MainColors

fun Color.contrastAgainst(background: Color): Float {
    val fg = if (alpha < 1f) compositeOver(background) else this

    val fgLuminance = fg.luminance() + 0.05f
    val bgLuminance = background.luminance() + 0.05f

    return kotlin.math.max(fgLuminance, bgLuminance) / kotlin.math.min(fgLuminance, bgLuminance)
}

fun Palette?.getAccurateColor(): MainColors {
    if (this != null)
        if (vibrantSwatch != null) {
            return MainColors(
                Color(vibrantSwatch!!.rgb),
                Color(vibrantSwatch!!.titleTextColor)
            )
        } else {
            swatches.sortedByDescending { swatch -> swatch.population }
                .firstOrNull { swatch ->
                    Color(swatch.rgb).contrastAgainst(
                        Color.White
                    ) >= 1.4f
                }
                ?.let { swatch ->
                    return MainColors(
                        Color(swatch.rgb),
                        Color(swatch.titleTextColor)
                    )
                }
        }
    return MainColors(Color.Black, Color.White)
}