package com.example.compose.utils.kotlin_extensions

import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

fun Offset.toIntOffset() = IntOffset(x.roundToInt(), y.roundToInt())

@ExperimentalMaterialApi
fun BottomSheetState.progress(): Float {
    return if (progress.from == progress.to) {
        if (progress.from == BottomSheetValue.Expanded) 1f else 0f
    } else if (progress.fraction != 1f && progress.fraction != 0f) {
        if (progress.from == BottomSheetValue.Collapsed) progress.fraction else (1 - progress.fraction)
    } else if (isCollapsed) 0f else 1f
}