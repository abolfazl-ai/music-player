package com.example.compose.ui.composables.fab

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons.Rounded
import androidx.compose.material.icons.rounded.SentimentSatisfied
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import com.example.compose.ui.composables.icons.E
import com.example.compose.ui.composables.icons.M
import com.example.compose.ui.composables.icons.O
import com.example.compose.utils.kotlin_extensions.toIntOffset
import com.example.compose.utils.util_classes.MultipleEventsCutter
import com.example.compose.utils.util_classes.get
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DragHandler(
    active: Boolean = true, onClick: () -> Unit, elevation: Dp, shape: Shape = CircleShape, size: Dp,
    color: Color, contentColor: Color, icon: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()

    val mainOffset = remember { Animatable(Offset.Zero, Offset.VectorConverter) }
    val tailOffsets = remember { FabTailIcons.map { Animatable(Offset.Zero, Offset.VectorConverter) to it } }

    var isDragging by remember { mutableStateOf(false) }
    val velocityTracker = remember { VelocityTracker() }
    LaunchedEffect(isDragging, active) {
        val spring = SpringSpec<Offset>(1f, 300f)
        if (!isDragging || !active) {
            scope.launch {
                val velocity = velocityTracker.calculateVelocity()
                velocityTracker.resetTracking()
                launch { mainOffset.animateTo(Offset.Zero, spring(0.75f, 300f), velocity.offset) }
                tailOffsets.forEach { delay(100); launch { it.first.animateTo(Offset.Zero, spring, velocity.offset) } }
            }
        }
    }

    val dragModifier = remember {
        Modifier.pointerInput(Unit) {
            detectDragGestures({ isDragging = true }, { isDragging = false }) { change, dragAmount ->
                change.consumeAllChanges()
                velocityTracker.addPosition(change.uptimeMillis, change.position)
                scope.launch {
                    mainOffset.snapTo(mainOffset.value + dragAmount)
                    tailOffsets.forEach { delay(100); launch { it.first.snapTo(it.first.value + dragAmount) } }
                }
            }
        }
    }

    tailOffsets.reversed().forEach { (offset, icon) ->
        if (offset.value.getDistance() > 0f || isDragging) Icon(
            modifier = Modifier.offset { offset.value.toIntOffset() }.size(size).padding(4.dp).clip(shape).background(color)
                .padding(10.dp), imageVector = icon, contentDescription = icon.name, tint = contentColor
        )
    }

    val multipleEventsCutter = remember { MultipleEventsCutter.get() }
    Surface(
        modifier = (if (active) dragModifier else Modifier).offset { mainOffset.value.toIntOffset() }.size(size),
        color = color, contentColor = contentColor, shape = shape, shadowElevation = elevation,
        onClick = { multipleEventsCutter.processEvent { onClick() } }, content = icon
    )
}

val FabTailIcons = listOf(Rounded.E, Rounded.M, Rounded.O, Rounded.SentimentSatisfied)

val Velocity.offset: Offset get() = Offset(x, y)