package com.example.compose.ui.composables.player_screen

import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.graphics.Color
import androidx.palette.graphics.Palette
import java.util.concurrent.TimeUnit


class MainColors(val back: Color, val front: Color)

@ExperimentalMaterialApi
fun BottomSheetState.progress(): Float {
    return if (progress.from == progress.to) {
        if (progress.from == BottomSheetValue.Expanded) 1f else 0f
    } else if (progress.fraction != 1f && progress.fraction != 0f) {
        if (progress.from == BottomSheetValue.Collapsed) progress.fraction else (1 - progress.fraction)
    } else if (isCollapsed) 0f else 1f

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

enum class PlaybackAction { PLAY, PAUSE, NEXT, PREVIOUS, STOP, SHUFFLE, REPEAT }

fun Long.toTimeFormat(): String {
    (TimeUnit.MILLISECONDS).let {
        val m = it.toMinutes(this)
        val s = it.toSeconds(this) - TimeUnit.MINUTES.toSeconds(it.toMinutes(this))
        return "$m:$s" + if (s<10) "0" else ""
    }
}