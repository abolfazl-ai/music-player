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
import kotlin.math.hypot
import kotlin.math.max

@Composable
fun ColorReveal(
    modifier: Modifier = Modifier,
    color: Color,
    y: Dp,
    content: @Composable (BoxScope.() -> Unit)? = null
) {

    val colors = remember { mutableStateListOf(color) }
    val animators = remember { arrayListOf(Animatable(1f)) }
    if (color != colors.last()) {
        colors.add(remember { color })
        animators.add(remember { Animatable(0.05f) })
    }

    animators.forEach { animator ->
        LaunchedEffect(key1 = animator) {
            animator.animateTo(1f, animationSpec = tween(durationMillis = 700)) {
                if (animators.last().value == 1f && animators.size > 1) {
                    for (i in 0 until colors.lastIndex) {
                        animators.removeFirst()
                        colors.removeFirst()
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
                val centerY = y
                    .roundToPx()
                    .toFloat()
                colors.forEachIndexed { i, color ->
                    drawCircle(
                        color = color,
                        radius = animators[i].value * hypot(
                            center.x,
                            max((size.height - centerY), centerY)
                        ),
                        center = Offset(center.x, centerY),
                        alpha = 0.6f + 0.4f * animators[i].value
                    )
                }
            }
    ) { if (content != null) content() }
}