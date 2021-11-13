@file:JvmName("ArrowToXKt")

package com.example.compose.ui.composables.icons.animated

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import com.example.compose.utils.util_classes.Path4dVector
import kotlinx.coroutines.launch

@Composable
fun ArrowToX(
    modifier: Modifier = Modifier,
    x: Boolean,
    tint: Color = MaterialTheme.colors.onSurface
) {

    val states = remember {
        listOf(
            Path4dVector(7f, 17f, 9.5f, 14.5f),
            Path4dVector(5f, 19f, 12f, 12f),
            Path4dVector(7f, 17f, 7f, 12f),
            Path4dVector(7f, 17f, 17f, 12f),
        )
    }
    val animators = remember {
        listOf(
            Animatable(states[if (x) 3 else 0], Path4dVector.VECTOR_CONVERTER),
            Animatable(states[if (x) 2 else 0], Path4dVector.VECTOR_CONVERTER)
        )
    }

    LaunchedEffect(x) {
        launch {
            if ((x && animators[0].value != states[3]) || (!x && animators[0].value != states[0]))
                if (x) animators.forEachIndexed { i, a ->
                    launch {
                        a.animateTo(states[1], spring(stiffness = 2500f))
                        a.animateTo(states[states.lastIndex - i], spring(0.4f, 2000f))
                    }
                }
                else animators.forEach {
                    launch {
                        it.animateTo(states[1], spring(stiffness = 3000f))
                        it.animateTo(states[0], spring(0.5f, 2500f))
                    }
                }
        }
    }

    val path = remember(animators[0].value) {
        Path().apply {
            animators.forEach {
                with(it.value) {
                    moveTo(v1, v3)
                    lineTo(12f, v4)
                    lineTo(v2, v3)
                }
            }
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        scale(size.minDimension / 24f, Offset.Zero) {
            drawPath(
                path, tint, style = Stroke(
                    1.8f,
                    cap = StrokeCap.Round, join = StrokeJoin.Round
                ), alpha = 0.8f
            )
        }
    }
}