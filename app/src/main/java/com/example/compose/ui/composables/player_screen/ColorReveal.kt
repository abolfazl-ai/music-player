package com.example.compose.ui.composables.player_screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.launch
import kotlin.math.hypot
import kotlin.math.max

@Composable
fun ColorReveal(
    modifier: Modifier = Modifier,
    color: Color,
    y: Dp,
    content: @Composable BoxScope.() -> Unit = {}
) {

    val colors = remember { mutableStateListOf(color to Animatable(1f)) }

    LaunchedEffect(color) {

        launch {

            colors.add(color to Animatable(0f))

            colors.forEach { c ->
                if (!c.second.isRunning) launch {
                    c.second.animateTo(1f, tween(500), initialVelocity = 0f)
                }
            }

        }.invokeOnCompletion {
            launch {
                with(colors) {
                    while (size > 1) {
                        if (first().second.value == 1f) removeFirst()
                    }
                }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RectangleShape)
            .drawBehind {
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
            },
        content = content
    )
}