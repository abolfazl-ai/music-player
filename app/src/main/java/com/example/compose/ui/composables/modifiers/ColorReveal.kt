package com.example.compose.ui.composables.modifiers

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.spring
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.hypot
import kotlin.math.max

fun Modifier.reveal(animate: Boolean, color: Color, size: Size, y: Dp, startRadius: Dp = 0.dp, duration: Int = 750) = composed {

    val scope = rememberCoroutineScope()

    var main: Color by remember { mutableStateOf(color) }
    val colors = remember { mutableStateListOf<Color>() }
    val animators = remember { mutableStateMapOf<Int, Float>() }

    with(LocalDensity.current) {
        val maxRadius = remember { hypot(size.width / 2f, max((size.height - y.toPx()), y.toPx())) }
        LaunchedEffect(color) {
            if (color != colors.lastOrNull()) scope.launch {
                if (animate) {
                    colors.add(color)
                    val index = colors.lastIndex
                    animate(startRadius.toPx(), maxRadius, animationSpec = spring(stiffness = 25f)) { v, _ ->
                        animators[index] = v
                    }
                }
                main = color
                if (animators[colors.lastIndex] == maxRadius) colors.clear(); animators.clear()
            }
        }
    }

    clipToBounds().drawBehind {
        drawRect(main)
        colors.forEachIndexed { i, color -> drawCircle(color, animators[i] ?: startRadius.toPx(), Offset(center.x, y.toPx())) }
    }
}

@Composable
fun CircularReveal(
    modifier: Modifier = Modifier, animate: Boolean, color: Color,
    size: Size, y: Dp, startRadius: Dp = 0.dp, duration: Int = 700
) = with(LocalDensity.current) {

    val scope = rememberCoroutineScope()
    val maxRadius = remember { hypot(size.width / 2f, max((size.height - y.toPx()), y.toPx())) }

    var main: Color by remember { mutableStateOf(color) }
    val colors = remember { mutableStateListOf<Color>() }
    val animators = remember { mutableStateMapOf<Int, Float>() }

    LaunchedEffect(color) {
        delay(50)
        if (color != colors.lastOrNull()) scope.launch {
            if (animate) {
                colors.add(color)
                val index = colors.lastIndex
                animate(startRadius.toPx(), maxRadius, animationSpec = spring(stiffness = 30f)) { v, _ -> animators[index] = v }
            }
            main = color
            if (animators[colors.lastIndex] == maxRadius) colors.clear(); animators.clear()
        }
    }

    Canvas(modifier) {
        drawRect(main)
        colors.forEachIndexed { i, color -> drawCircle(color, animators[i] ?: startRadius.toPx(), Offset(center.x, y.toPx())) }
    }
}