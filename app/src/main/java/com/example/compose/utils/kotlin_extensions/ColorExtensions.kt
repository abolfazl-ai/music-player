package com.example.compose.utils.kotlin_extensions

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.luminance
import androidx.palette.graphics.Palette
import androidx.palette.graphics.Target
import com.example.compose.utils.util_classes.MainColors

val PALETTE_TARGET_PRIMARY = Target.Builder()
    .setSaturationWeight(3f)
    .setPopulationWeight(2f)
    .setLightnessWeight(1f)
    .setTargetSaturation(1f)
    .setTargetLightness(0.5f)
    .setMinimumLightness(0.15f)
    .setMaximumLightness(0.85f)
    .build()

val PALETTE_TARGET_SECONDARY = Target.Builder()
    .setPopulationWeight(3f)
    .setSaturationWeight(2f)
    .setLightnessWeight(1f)
    .setTargetSaturation(1f)
    .setTargetLightness(0.5f)
    .build()


fun Palette?.getAccurateColor(): MainColors {
    this?.run {
        getSwatchForTarget(PALETTE_TARGET_PRIMARY)?.let {
            return MainColors(Color(it.rgb), Color(it.titleTextColor))
        }
        getSwatchForTarget(PALETTE_TARGET_SECONDARY)?.let {
            return MainColors(Color(it.rgb), Color(it.titleTextColor))
        }
        swatches.maxByOrNull { swatch -> swatch.population }?.let {
            return MainColors(Color(it.rgb), Color(it.titleTextColor))
        }

    }
    return MainColors(Color.Black, Color.White)
}

fun Float.getMidColor(start: Color, end: Color): Color {
    return Color(
        red = start.red * (1 - this) + (end.red * this),
        green = start.green * (1 - this) + (end.green * this),
        blue = start.blue * (1 - this) + (end.blue * this)
    )
}