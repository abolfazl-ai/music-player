package com.example.compose.ui.composables.icons.animated

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.compose.utils.util_classes.Path4dVector
import kotlinx.coroutines.launch

@Composable
fun Arrow(modifier: Modifier = Modifier, down: Boolean, tint: Color = MaterialTheme.colors.onSurface) {

    val states = remember {
        listOf(
            Path4dVector(17f, 7f, 9.5f, 14.5f),
            Path4dVector(16f, 8f, 14f, 20f),
            Path4dVector(16f, 8f, 10f, 4f),
            Path4dVector(17f, 7f, 14.5f, 9.5f),
        )
    }
    val animator = remember { Animatable(states[0], Path4dVector.VECTOR_CONVERTER) }

    LaunchedEffect(down) {
        val loSi = tween<Path4dVector>(durationMillis = 50, easing = LinearOutSlowInEasing)
        val foSi = tween<Path4dVector>(100, 50, LinearOutSlowInEasing)
        val over = spring<Path4dVector>(Spring.DampingRatioHighBouncy)

        if ((down && animator.value != states[0]) || (!down && animator.value != states[3]))
            launch {
                animator.apply {
                    animateTo(if (down) states[2] else states[1], animationSpec = loSi)
                    animateTo(if (down) states[1] else states[2], animationSpec = foSi)
                    animateTo(if (down) states[0] else states[3], animationSpec = over)
                }
            }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        Path().apply {
            with(animator.value) {
                moveTo(v1 * size.width / 24, v3 * size.height / 24)
                lineTo(size.width / 2f, v4 * size.height / 24)
                lineTo(v2 * size.width / 24, v3 * size.height / 24)
            }
        }.also {
            drawPath(
                it, tint, style = Stroke(
                    1.8f * size.width / 24,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }
    }
}