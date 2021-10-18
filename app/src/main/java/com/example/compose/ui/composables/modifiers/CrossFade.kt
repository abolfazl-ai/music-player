package com.example.compose.ui.composables.modifiers

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import kotlin.math.absoluteValue

fun Modifier.crossFade(pageOffset: Float) = composed {
    var width by remember { mutableStateOf(0) }
    onGloballyPositioned {
        width = it.size.width
    }.graphicsLayer {
        translationX = pageOffset.coerceIn(-1f, 1f) * .9f * width
        alpha = 1 - 1.5f * pageOffset.absoluteValue.coerceIn(0f, 1f)
    }
}
