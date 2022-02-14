package com.example.compose.ui.composables.modifiers

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.hypot
import kotlin.math.max

fun Modifier.reveal(animate: Boolean, color: Color, size: Size, y: Dp, startRadius: Dp = 0.dp, duration: Int = 750) = composed {

    val colors = remember { mutableStateListOf(color to Animatable(1f)) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(color) {
        if (color != colors.last().first) launch {
            if (animate) (color to Animatable(0f)).let {
                colors.add(it)
                scope.launch {
                    it.second.animateTo(1f, tween(duration, easing = FastOutLinearInEasing))
                    if (colors.size > 1 && colors[1].second.value == 1f) colors.removeFirst()
                }
            } else {
                colors.add(color to Animatable(1f))
                if (colors.size > 1 && colors[1].second.value == 1f) colors.removeFirst()
            }
        }
    }

    val maxRadius = with(LocalDensity.current) { remember { hypot(size.width / 2f, max((size.height - y.toPx()), y.toPx())) } }

    clipToBounds().drawBehind {
        drawRect(colors.first().first)
        colors.forEach {
            drawCircle(
                color = it.first, center = Offset(center.x, y.toPx()),
                radius = it.second.value * maxRadius + startRadius.toPx(),
            )
        }
    }
}

@Composable
fun CircularReveal(
    modifier: Modifier = Modifier, animate: Boolean, color: Color,
    size: Size, y: Dp, startRadius: Dp = 0.dp, duration: Int = 600
) = with(LocalDensity.current) {

    val scope = rememberCoroutineScope()
    val maxRadius = remember { hypot(size.width / 2f, max((size.height - y.toPx()), y.toPx())) }

    var main: Color by remember { mutableStateOf(color) }
    val colors = remember { mutableStateListOf<Color>() }
    val animators = remember { mutableStateMapOf<Int, Float>() }

    LaunchedEffect(color) {
        if (color != colors.lastOrNull()) scope.launch {
            if (animate) {
                colors.add(color)
                colors.lastIndex.let {
                    animate(
                        startRadius.toPx(), maxRadius,
                        animationSpec = tween(duration, easing = CubicBezierEasing(0.5f, 0f, 1f, 1f))
                    ) { value, _ ->
                        animators[it] = value
                    }
                }
            }
            main = color
            if (animators[colors.lastIndex] == maxRadius) colors.clear(); animators.clear()
        }
    }

    Canvas(modifier) {
        drawRect(main)
        colors.forEachIndexed { index, color ->
            drawCircle(color = color, radius = animators[index] ?: startRadius.toPx(), center = Offset(center.x, y.toPx()))
        }
    }

}