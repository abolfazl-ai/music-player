package com.example.compose.utils.kotlin_extensions

import androidx.compose.ui.graphics.Color
import androidx.palette.graphics.Palette
import androidx.palette.graphics.Target
import com.example.compose.utils.util_classes.DefaultMainColors
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
            return MainColors(Color(it.rgb), Color(it.bodyTextColor))
        }
        getSwatchForTarget(PALETTE_TARGET_SECONDARY)?.let {
            return MainColors(Color(it.rgb), Color(it.bodyTextColor))
        }
        swatches.maxByOrNull { swatch -> swatch.population }?.let {
            return MainColors(Color(it.rgb), Color(it.bodyTextColor))
        }

    }
    return DefaultMainColors
}

fun Float.getMidColor(start: Color, end: Color): Color {
    return Color(
        red = (start.red * (1 - this) + (end.red * this)).coerceIn(0f, 1f),
        green = (start.green * (1 - this) + (end.green * this)).coerceIn(0f, 1f),
        blue = (start.blue * (1 - this) + (end.blue * this)).coerceIn(0f, 1f)
    )
}