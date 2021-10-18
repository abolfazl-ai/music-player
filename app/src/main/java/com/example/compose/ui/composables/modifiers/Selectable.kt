package com.example.compose.ui.composables.modifiers

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import kotlin.math.sqrt

fun Modifier.selectable(
    selected: Boolean,
    color: Color,
    tint: Color,
) = composed {

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
            color = color.copy(alpha = progress.coerceIn(0f, 1f)),
            radius = progress * size.width / 5,
        )

        drawCircle(
            color = color.copy(alpha = progress.coerceIn(0.5f, 1f)),
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
                color = tint,
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