package com.example.compose.ui.composables

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.compose.ui.theme.Blue700
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.*

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun DraggableFab(
    modifier: Modifier = Modifier,
    items: List<ImageVector> = listOf(
        Icons.Rounded.Home,
        Icons.Rounded.GridView,
        Icons.Rounded.TableView,
        Icons.Rounded.Delete,
        Icons.Rounded.Settings,
    )
) {

    var isInitialized by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var isExpanding by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    val backSize = remember { Animatable(56.dp, Dp.VectorConverter) }
    val rotation = remember { Animatable(0f) }
    val alpha = animateFloatAsState(if (expanded) 0.7f else 0f)
    val offset = remember { Animatable(TwoDimFloat(0f, 0f), TwoDimFloat.VECTOR_CONVERTER) }
    val offsets = remember {
        ArrayList<Animatable<TwoDimFloat, AnimationVector2D>>().also {
            items.forEach { _ ->
                it.add(Animatable(TwoDimFloat(0f, 0f), TwoDimFloat.VECTOR_CONVERTER))
            }
        }
    }

    var job: Job? = remember { null }

    if (isExpanding)
        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .pointerInteropFilter { expanded = false; true }
                .alpha(alpha.value)
                .background(MaterialTheme.colors.background)
        )

    //just testing

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomEnd
    ) {

        with(LocalDensity.current) {
            LaunchedEffect(expanded) {
                if (isInitialized) {
                    if (expanded) {
                        isExpanding = true
                        launch {
                            offset.animateTo(
                                TwoDimFloat((-maxWidth / 2 + 28.dp).toPx(), 0f),
                                animationSpec = spring(0.65f)
                            )
                        }
                        offsets.forEach {
                            launch {
                                it.animateTo(
                                    TwoDimFloat((-maxWidth / 2 + 25.dp).toPx(), 0f),
                                    animationSpec = spring(0.65f)
                                )
                            }
                        }
                        delay(200)
                        launch { backSize.animateTo(200.dp, animationSpec = spring(0.4f, 1000f)) }
                        launch { rotation.animateTo(135f, animationSpec = spring(0.4f, 1000f)) }
                        launch {
                            val angle = PI / items.lastIndex
                            offsets.forEachIndexed { index, animatable ->
                                launch {
                                    animatable.animateTo(
                                        TwoDimFloat(
                                            animatable.value.v1 - (64 * cos(index * angle)).dp.toPx(),
                                            animatable.value.v2 - (64 * sin(index * angle)).dp.toPx()
                                        )
                                    )
                                }
                            }
                        }
                    } else {
                        offsets.forEach {
                            launch {
                                it.animateTo(
                                    TwoDimFloat((-maxWidth / 2 + 25.dp).toPx(), 0f),
                                    animationSpec = spring(0.65f)
                                )
                            }
                        }
                        launch { backSize.animateTo(56.dp) }
                        launch { rotation.animateTo(0f) }
                        delay(250)
                        launch {
                            offset.animateTo(
                                TwoDimFloat(0f, 0f),
                                animationSpec = spring(0.65f)
                            )
                        }
                        offsets.forEach {
                            launch {
                                it.animateTo(
                                    TwoDimFloat(0f, 0f),
                                    animationSpec = spring(0.65f)
                                )
                            }
                        }
                        isExpanding = false
                    }
                } else isInitialized = true
            }
        }

        Surface(modifier = Modifier
            .offset {
                IntOffset(
                    offset.value.v1.roundToInt() + ((backSize.value - 56.dp) / 2).roundToPx(),
                    offset.value.v2.roundToInt() + ((backSize.value - 56.dp) / 2).roundToPx()
                )
            }
            .size(backSize.value),
            elevation = 4.dp,
            shape = CircleShape,
            border = BorderStroke(3.dp, Blue700)) {}

        offsets.reversed().forEachIndexed { index, anim ->
            Surface(
                modifier = Modifier
                    .offset { IntOffset(anim.value.v1.roundToInt(), anim.value.v2.roundToInt()) }
                    .size(if (isExpanding) 50.dp else 54.dp),
                onClick = {}, shape = CircleShape,
                color = if (isExpanding) Color.Transparent else Blue700,
                contentColor = if (isExpanding) Blue700 else Color.White
            ) {
                Icon(
                    modifier = Modifier.padding(14.dp),
                    imageVector = items[items.lastIndex - index],
                    contentDescription = null
                )
            }
        }

        Surface(
            modifier = Modifier
                .offset { IntOffset(offset.value.v1.roundToInt(), offset.value.v2.roundToInt()) }
                .size(56.dp)
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
                                else expanded = true
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
            onClick = { if (offset.value.v2 == 0f) expanded = !expanded },
            color = Blue700,
            shape = CircleShape,
            contentColor = Color.White
        ) {
            Icon(
                modifier = Modifier.padding(12.dp),
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