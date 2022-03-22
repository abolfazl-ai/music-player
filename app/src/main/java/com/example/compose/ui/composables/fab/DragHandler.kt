package com.example.compose.ui.composables.fab

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SentimentSatisfied
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.compose.ui.composables.icons.E
import com.example.compose.ui.composables.icons.M
import com.example.compose.ui.composables.icons.O
import com.example.compose.utils.kotlin_extensions.coerceAtLeast
import com.example.compose.utils.kotlin_extensions.toIntOffset
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DragHandler(
    active: Boolean = true, onClick: () -> Unit, elevation: Dp, shape: Shape = CircleShape, size: Dp,
    color: Color, contentColor: Color, onDrag: (isDragging: Boolean) -> Unit = {}, icon: @Composable () -> Unit
) {

    val minDistance = with(LocalDensity.current) { remember { 16.dp.roundToPx() } }
    val mainOffset = remember { Animatable(initialValue = Offset.Zero, typeConverter = Offset.VectorConverter) }
    val tailOffsets = remember {
        listOf(
            Animatable(initialValue = Offset.Zero, typeConverter = Offset.VectorConverter) to Icons.Rounded.E,
            Animatable(initialValue = Offset.Zero, typeConverter = Offset.VectorConverter) to Icons.Rounded.M,
            Animatable(initialValue = Offset.Zero, typeConverter = Offset.VectorConverter) to Icons.Rounded.O,
            Animatable(initialValue = Offset.Zero, typeConverter = Offset.VectorConverter) to Icons.Rounded.SentimentSatisfied,
        )
    }

    val dragModifier = remember {
        Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                coroutineScope {
                    while (true) {
                        onDrag(false)

                        val pointerId = awaitPointerEventScope { awaitFirstDown().id }
                        val velocityTracker = VelocityTracker()

                        awaitPointerEventScope {
                            velocityTracker.resetTracking()
                            onDrag(true)
                            drag(pointerId) { input ->
                                launch {
                                    launch { mainOffset.snapTo(mainOffset.value + input.positionChange()) }
                                    tailOffsets.forEach {
                                        delay(100)
                                        launch { it.first.snapTo(it.first.value + input.positionChange()) }
                                    }
                                }
                                velocityTracker.addPosition(input.uptimeMillis, input.position)
                            }
                        }

                        val velocity = velocityTracker.calculateVelocity()

                        launch {
                            launch { mainOffset.animateTo(Offset.Zero, spring(0.75f, 300f), Offset(velocity.x, velocity.y)) }
                            tailOffsets.forEach {
                                delay(100)
                                launch { it.first.animateTo(Offset.Zero, spring(1f, 300f), Offset(velocity.x, velocity.y)) }
                            }
                            onDrag(false)
                        }
                    }
                }
            }
    }

    if (mainOffset.value.getDistance() > 0) tailOffsets.reversed().forEach { (offset, icon) ->
        Surface(
            modifier = Modifier
                .offset { offset.value.toIntOffset() }
                .size(size)
                .padding(4.dp),
            color = color, contentColor = contentColor, shape = shape, shadowElevation = 2.dp
        ) { Icon(modifier = Modifier.padding(10.dp), imageVector = icon, contentDescription = null) }
    }

    Surface(
        modifier = Modifier
            .offset {
                mainOffset.value
                    .coerceAtLeast(minDistance)
                    .toIntOffset()
            }
            .size(size), onClick = { if (mainOffset.value.getDistance() < minDistance) onClick() }, color = color,
        contentColor = contentColor, shape = shape, shadowElevation = elevation
    ) { Spacer(modifier = if (active) dragModifier else Modifier.fillMaxSize()); icon() }
}