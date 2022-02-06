package com.example.compose.ui.composables.modifiers

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.compose.utils.resources.TAG
import kotlinx.coroutines.launch
import kotlin.math.hypot
import kotlin.math.max

fun Modifier.reveal(color: Color, y: Dp, startRadius: Dp = 0.dp, duration: Int = 750) = composed {
    Log.e(TAG, "reveal Recreated")

    val colors = remember { mutableStateListOf(color to Animatable(1f)) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(color) {
        launch {
            (color to Animatable(0f)).let {
                colors.add(it)
                scope.launch {
                    it.second.animateTo(1f, tween(duration, easing = FastOutLinearInEasing))
                }.invokeOnCompletion {
                    if (colors.size > 1 && colors[1].second.value == 1f) colors.removeFirst()
                }
            }
        }
    }

    clipToBounds()
    drawBehind {
        colors.forEach {
            drawCircle(
                color = it.first,
                radius = it.second.value * hypot(
                    center.x,
                    max((size.height - y.toPx()), y.toPx())
                ) + startRadius.toPx(),
                center = Offset(center.x, y.toPx()),
            )
        }
    }
}