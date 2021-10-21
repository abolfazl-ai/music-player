package com.example.compose.ui.composables.modifiers

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun Modifier.drag(active: Boolean, animators: List<Animatable<Offset, AnimationVector2D>>) =
    if (active) pointerInput(Unit) {
        coroutineScope {
            while (true) {

                val pointerId = awaitPointerEventScope { awaitFirstDown().id }
                val velocityTracker = VelocityTracker()

                awaitPointerEventScope {
                    drag(pointerId) { input ->
                        launch {
                            animators.forEach {
                                launch { it.snapTo(it.value + input.positionChange()) }
                                delay(100)
                            }
                            velocityTracker.addPosition(input.uptimeMillis, input.position)
                        }
                    }
                }

                val velocity = velocityTracker.calculateVelocity()
                 launch {
                    animators.forEachIndexed { i, anim ->
                        launch {
                            anim.animateTo(
                                Offset.Zero,
                                spring(if (i == 0) 0.75f else 1f, 300f),
                                Offset(2*velocity.x, 2*velocity.y)
                            )
                        }
                        delay(100)
                    }
                }
            }
        }
    } else this