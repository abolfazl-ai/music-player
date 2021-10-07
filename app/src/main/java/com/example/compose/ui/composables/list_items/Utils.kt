package com.example.compose.ui.composables.list_items

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.example.compose.ui.theme.DarkGray
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.sqrt

@Composable
fun Selectable(
    modifier: Modifier = Modifier,
    progress: Float = 0f,
    selectColor: Color = MaterialTheme.colors.primary,
    tint: Color = Color.White,
    backgroundColor: Color = DarkGray,
    shape: Shape = RectangleShape,
    onclick: () -> Unit = {},
    content: @Composable BoxScope.() -> Unit = {}
) = Box(modifier
    .clip(shape)
    .drawBehind {
        scale(0.99f) {
            drawOutline(
                shape.createOutline(
                    size, LayoutDirection.Ltr,
                    Density(density)
                ), color = backgroundColor
            )
        }
    }
    .clickable(onClick = onclick)) {

    content()

    if (progress > 0f)
        Canvas(modifier = Modifier.fillMaxSize()) {
            Path().apply {
                val fraction = size.width / 24
                moveTo(fraction * 7, fraction * 13)
                lineTo(fraction * 11, fraction * 16)
                lineTo(fraction * 17, fraction * 8)
            }.also {
                drawCircle(
                    color = selectColor.copy(alpha = progress.coerceIn(0f, 1f)),
                    radius = progress * size.width / 5,
                )
                drawCircle(
                    color = selectColor.copy(alpha = progress.coerceIn(0.5f, 1f)),
                    radius = sqrt(0.5f) * size.width,
                    style = Stroke(width = progress * 1.47f * size.width, size.height)
                )
                scale(progress.coerceIn(0.1f, 2f)) {
                    rotate(45 * (1 - progress)) {
                        drawPath(
                            path = it,
                            color = tint,
                            style = Stroke(
                                width = size.width / 16,
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round,
                                pathEffect = PathEffect.dashPathEffect(
                                    floatArrayOf(125 * progress.coerceIn(0f, 1f), 125f), 1f
                                )
                            )
                        )
                    }
                }
            }
        }
}


@Composable
fun Arrow(modifier: Modifier = Modifier, down: Boolean, tint: Color = Color.Black) {

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
                    1.5f * size.width / 24,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }
    }
}

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
            rotate(90 - 180 * animator.value.v1) {
                scale(0.5f + abs(animator.value.v1 - 0.5f).coerceIn(0f, 0.5f)) {
                    drawPath(
                        it, tint, style = Stroke(
                            1.5f * size.width / 24,
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )
                }
            }
        }
    }
}

data class Path4dVector(val v1: Float, val v2: Float, val v3: Float, val v4: Float) {
    companion object {
        val VECTOR_CONVERTER: TwoWayConverter<Path4dVector, AnimationVector4D> =
            TwoWayConverter(
                { AnimationVector4D(it.v1, it.v2, it.v3, it.v4) },
                { Path4dVector(it.v1, it.v2, it.v3, it.v4) })
    }
}
