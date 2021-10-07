package com.example.compose.ui.composables

import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.hypot
import kotlin.math.roundToInt

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun DraggableFab(
    modifier: Modifier = Modifier,
    items: List<ImageVector> = listOf(
        Icons.Rounded.Shuffle,
        Icons.Rounded.SortByAlpha,
        Icons.Rounded.GridView,
        Icons.Rounded.Settings,
    ),
    expanded: Boolean = false,
    onExpand: (expanded: Boolean) -> Unit = {}
) {


    if (expanded) Spacer(modifier = Modifier
        .fillMaxSize()
        .pointerInteropFilter { onExpand(false); true })

    val scope = rememberCoroutineScope()

    val rotation = remember { Animatable(0f) }
    val iconsAlpha = remember { Animatable(1f) }
    val offset = remember { Animatable(TwoDimFloat(0f, 0f), TwoDimFloat.VECTOR_CONVERTER) }
    val offsets = remember {
        ArrayList<Animatable<TwoDimFloat, AnimationVector2D>>().also {
            items.forEach { _ ->
                it.add(Animatable(TwoDimFloat(0f, 0f), TwoDimFloat.VECTOR_CONVERTER))
            }
        }
    }

    var job: Job? = remember { null }

    BoxWithConstraints(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {

        with(LocalDensity.current) {
            LaunchedEffect(expanded) {
                val spec = spring<Float>(Spring.DampingRatioMediumBouncy)
                if (expanded) {
                    iconsAlpha.snapTo(0f)
                    offsets.reversed().forEachIndexed { index, anim ->
                        launch {
                            anim.animateTo(
                                TwoDimFloat(0f, -(60 + index * 56).dp.toPx()),
                                spring(Spring.DampingRatioMediumBouncy)
                            )
                        }
                    }
                    launch { iconsAlpha.animateTo(1f) }
                    launch { rotation.animateTo(135f, spec) }
                } else {
                    offsets.forEach {
                        launch {
                            it.animateTo(TwoDimFloat(0f, 0f))
                        }
                    }
                    launch {
                        iconsAlpha.animateTo(0f, tween(75))
                        delay(100)
                        iconsAlpha.snapTo(1f)
                    }
                    launch { rotation.animateTo(0f, spec) }
                }
            }
        }

        offsets.reversed().forEachIndexed { index, anim ->
            FloatingActionButton(
                modifier = Modifier
                    .padding(4.dp)
                    .size(48.dp)
                    .offset {
                        IntOffset(
                            anim.value.v1.roundToInt(),
                            anim.value.v2.roundToInt()
                        )
                    }
                    .alpha(iconsAlpha.value),
                onClick = {},
                backgroundColor = MaterialTheme.colors.secondary,
                contentColor = MaterialTheme.colors.onPrimary,
                elevation = FloatingActionButtonDefaults.elevation(2.dp, 4.dp)
            ) {
                Icon(
                    modifier = Modifier.padding(12.dp),
                    imageVector = items[items.lastIndex - index],
                    contentDescription = null
                )
            }
        }

        FloatingActionButton(
            modifier = Modifier
                .offset { IntOffset(offset.value.v1.roundToInt(), offset.value.v2.roundToInt()) }
                .rotate(rotation.value)
                .pointerInput(Unit) {
                    detectDragGestures(onDragEnd = {
                        scope.launch {
                            if (!expanded)
                                if (hypot(offset.value.v1, offset.value.v2) > 16.dp.toPx())
                                    job?.invokeOnCompletion {
                                        scope.launch {
                                            offset.animateTo(
                                                TwoDimFloat(0f, 0f),
                                                animationSpec = spring(0.65f, 300f)
                                            )
                                        }
                                        offsets.forEachIndexed { index, anim ->
                                            scope.launch {
                                                delay(((1 + index) * 70).toLong())
                                                anim.animateTo(
                                                    TwoDimFloat(0f, 0f),
                                                    animationSpec = spring(1f, 300f)
                                                )
                                            }
                                        }
                                    }
                                else {
                                    scope.launch { offset.animateTo(TwoDimFloat(0f, 0f)) }
                                    onExpand(true)
                                }
                        }
                    }) { change, dragAmount ->
                        change.consumeAllChanges()
                        if (!expanded) job = scope.launch {
                            offset.snapTo(
                                TwoDimFloat(
                                    offset.value.v1 + dragAmount.x,
                                    offset.value.v2 + dragAmount.y
                                )
                            )

                            offsets.forEach {
                                delay(100)
                                if (!expanded)
                                    launch {
                                        it.snapTo(
                                            TwoDimFloat(
                                                it.value.v1 + dragAmount.x,
                                                it.value.v2 + dragAmount.y
                                            )
                                        )
                                    }
                            }
                        }
                    }
                },
            onClick = { if (hypot(offset.value.v1, offset.value.v2) == 0f) onExpand(!expanded) },
            backgroundColor = MaterialTheme.colors.secondary,
            shape = CircleShape,
            contentColor = MaterialTheme.colors.onPrimary
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = null
            )
        }
    }
}

data class TwoDimFloat(val v1: Float, val v2: Float) {
    companion object {
        val VECTOR_CONVERTER: TwoWayConverter<TwoDimFloat, AnimationVector2D> =
            TwoWayConverter(
                { AnimationVector2D(it.v1, it.v2) },
                { TwoDimFloat(it.v1, it.v2) })
    }
}