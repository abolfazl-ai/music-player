package com.example.compose.ui.composables.modifiers

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.compose.utils.kotlin_extensions.compIn
import kotlinx.coroutines.launch
import kotlin.math.sqrt

fun Modifier.selectable(
    selected: Boolean,
    color: Color? = null,
    tint: Color? = null,
) = composed {

    val finalColor = color ?: MaterialTheme.colorScheme.secondary
    val finalTint = tint ?: MaterialTheme.colorScheme.onSecondary

    val progress by animateFloatAsState(
        if (selected) 1f else 0f,
        spring(if (selected) 0.5f else 1f, if (selected) 600f else 1500f)
    )

    val path = remember {
        Path().apply {
            moveTo(7f, 13f)
            lineTo(11f, 16f)
            lineTo(17f, 8f)
        }
    }

    clipToBounds()
    drawWithContent {

        drawContent()

        drawCircle(
            color = finalColor.copy(alpha = progress.coerceIn(0f, 1f)),
            radius = progress * size.width / 5,
        )

        drawCircle(
            color = finalColor.copy(alpha = progress.coerceIn(0.5f, 1f)),
            radius = sqrt(0.5f) * size.width,
            style = Stroke(width = progress * 1.47f * size.width, size.height)
        )

        withTransform({
            scale(progress)
            rotate(45 * (1 - progress))
            scale(size.minDimension / 24, Offset.Zero)
        }) {
            drawPath(
                path = path,
                color = finalTint,
                style = Stroke(
                    width = 1.5f,
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

@Composable
fun Selectable2(
    checked: Boolean,
    onCheck: (Boolean) -> Unit,
    cornerRadius: Dp = 8.dp,
    checkedColor: Color = MaterialTheme.colorScheme.secondary,
    uncheckedColor: Color = MaterialTheme.colorScheme.tertiary,
    tint: Color = MaterialTheme.colorScheme.onSecondary,
    duration: Int = 250,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    var initialized by remember { mutableStateOf(false) }

    val width = remember { Animatable(0f) }
    val color = remember { Animatable(uncheckedColor) }
    val check = remember { Animatable(0f) }
    val scale = remember { Animatable(1f) }
    LaunchedEffect(checked) {
        if (initialized || checked) {
            if (checked) {
                launch {
                    width.animateTo(1f, tween(2 * duration / 3))
                    check.animateTo(1f, tween(duration / 3))
                }
                launch {
                    scale.animateTo(0.95f, tween(2 * duration / 3))
                    scale.animateTo(1.0f, tween(duration / 3))
                }
                launch { color.animateTo(checkedColor, tween(duration / 3, duration / 3)) }
            } else {
                launch {
                    check.animateTo(0f, tween(duration / 3))
                    width.animateTo(0f, tween(2 * duration / 3))
                }
                launch { color.animateTo(checkedColor, tween(duration / 3)) }
            }
        }
        initialized = true
    }

    val path = remember { Path().apply { moveTo(7f, 13f); lineTo(11f, 16f);lineTo(17f, 8f) } }
    Box(modifier.scale(scale.value).clip(RoundedCornerShape(cornerRadius)).clickable { onCheck(!checked) }
        .drawWithContent {
            drawContent()
            if (width.value > 0f) {
                drawRoundRect(
                    color = color.value, cornerRadius = CornerRadius(cornerRadius.toPx(), cornerRadius.toPx()),
                    style = Stroke(width = width.value * (size.minDimension), miter = 4f)
                )
                withTransform({ scale(check.value.compIn(end = 0.5f)); scale(size.minDimension / 24, Offset.Zero) }) {
                    drawPath(
                        path = path, color = tint, style = Stroke(
                            width = 2f, cap = StrokeCap.Round, join = StrokeJoin.Round,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(0.1f, 5 * (1 - check.value), 20 * check.value, 20f), 1f)
                        )
                    )
                }
            }
        }, content = content)
}