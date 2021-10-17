package com.example.compose.ui.composables.modifiers

import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

fun Modifier.drag(
    active: Boolean,
    onDrag: (ended: Boolean, value: Offset) -> Unit
) = pointerInput(Unit) {
    coroutineScope {
        while (true) {

            val pointerId = awaitPointerEventScope { awaitFirstDown().id }
            val velocityTracker = VelocityTracker()

            awaitPointerEventScope {
                drag(pointerId) { input ->
                    if (active) launch {
                        onDrag(false, input.positionChange())
                        velocityTracker.addPosition(input.uptimeMillis, input.position)
                    }
                }
            }

            val velocity = velocityTracker.calculateVelocity()
            if (active) launch { onDrag(true, Offset(velocity.x, velocity.y)) }
        }
    }

}