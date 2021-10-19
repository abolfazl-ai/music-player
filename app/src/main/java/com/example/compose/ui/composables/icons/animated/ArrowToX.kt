package com.example.compose.ui.composables.icons.animated

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import com.example.compose.utils.util_classes.Path4dVector
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun ArrowToX(
    modifier: Modifier = Modifier,
    down: Boolean,
    tint: Color = MaterialTheme.colors.onSurface
) {

    val states = remember {
        listOf(
            Path4dVector(0f, 9.5f, 12f, 14.5f),
            Path4dVector(1f, 7f, 17f, 17f),
        )
    }
    val animator = remember { Animatable(states[0], Path4dVector.VECTOR_CONVERTER) }

    LaunchedEffect(down) {
        val over = spring<Path4dVector>(if (down) Spring.DampingRatioNoBouncy else 0.4f, 400f)

        if ((down && animator.value != states[0]) || (!down && animator.value != states[1]))
            launch {
                animator.apply {
                    animateTo(if (down) states[0] else states[1], over)
                }
            }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        Path().apply {
            with(animator.value) {
                moveTo(7f * size.width / 24, v2 * size.height / 24)
                lineTo(v3 * size.width / 24, v4 * size.height / 24)
                moveTo(17f * size.width / 24, v2 * size.height / 24)
                lineTo((24 - v3) * size.width / 24, v4 * size.height / 24)
            }
        }.also {
            rotate(90 - 180 * animator.value.v1.coerceIn(-0.1f,1.1f)) {
                scale(0.5f + abs(animator.value.v1 - 0.5f).coerceIn(0f, 0.5f)) {
                    drawPath(
                        it, tint, style = Stroke(
                            1.8f * size.width / 24,
                            cap = StrokeCap.Round, join = StrokeJoin.Round
                        ), alpha = 0.8f
                    )
                }
            }
        }
    }
}