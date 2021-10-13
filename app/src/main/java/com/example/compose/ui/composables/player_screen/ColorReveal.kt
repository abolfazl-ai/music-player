package com.example.compose.ui.composables.player_screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.launch
import kotlin.math.hypot
import kotlin.math.max

fun Modifier.reveal(color: Color, y: Dp) = composed {

    val colors = remember { mutableStateListOf(color to Animatable(1f)) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(color) {
        launch {
            (color to Animatable(0f)).let {
                colors.add(it)
                scope.launch {
                    it.second.animateTo(1f, tween(1000))
                }.invokeOnCompletion {
                    if (colors.size > 1 && colors[1].second.value == 1f) colors.removeFirst()
                }
            }
        }
    }

    this.then(clip(RectangleShape).drawBehind {
        colors.forEach {
            drawCircle(
                color = it.first,
                radius = it.second.value * hypot(
                    center.x,
                    max((size.height - y.toPx()), y.toPx())
                ),
                center = Offset(center.x, y.toPx()),
            )
        }
    })
}