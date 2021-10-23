package com.example.compose.ui.composables

import androidx.compose.animation.core.*
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.compose.R
import com.example.compose.ui.composables.modifiers.drag
import com.example.compose.utils.kotlin_extensions.toIntOffset
import com.example.compose.utils.util_classes.FabState
import com.example.compose.utils.util_classes.FabState.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalAnimationGraphicsApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun DraggableFab(
    modifier: Modifier = Modifier,
    fabColor: Color = MaterialTheme.colors.secondary,
    fabTint: Color = MaterialTheme.colors.onSecondary,
    playButtonColor: Color = MaterialTheme.colors.surface,
    playButtonTint: Color = MaterialTheme.colors.onSurface,
    state: FabState,
    items: List<ImageVector> = MenuItems,
    expanded: Boolean = false,
    onExpand: (expanded: Boolean) -> Unit = {},
    isPlaying: Boolean = false,
    onClick: () -> Unit = {},
) {

    val iconsAlpha = remember { Animatable(1f) }
    val offsets = remember(items) {
        ArrayList<Animatable<Offset, AnimationVector2D>>().also { list ->
            repeat(items.size + 1) {
                list.add(Animatable(Offset.Zero, Offset.VectorConverter))
            }
        }
    }

    val color = remember(state) {
        when (state) {
            Menu -> fabColor to fabTint
            is MenuToPlay -> {
                state.progress.getMidColor(fabColor, playButtonColor) to
                        state.progress.getMidColor(fabTint, playButtonTint)
            }
            Play -> playButtonColor to playButtonTint
            is PlayToQueue -> {
                state.progress.getMidColor(playButtonColor, fabColor) to
                        state.progress.getMidColor(playButtonTint, fabTint)
            }
            Queue -> fabColor to fabTint
        }
    }
    val scope = rememberCoroutineScope()

    with(LocalDensity.current) {
        LaunchedEffect(expanded, state) {
            if (state == Menu) {
                if (expanded) {
                    iconsAlpha.snapTo(0f)
                    offsets.minus(offsets[0]).reversed().forEachIndexed { index, anim ->
                        launch {
                            anim.animateTo(
                                Offset(0f, -(60 + index * 56).dp.toPx()),
                                spring(Spring.DampingRatioMediumBouncy)
                            )
                        }
                    }
                    launch { iconsAlpha.animateTo(1f) }
                } else {
                    offsets.minus(offsets[0]).forEach {
                        launch {
                            it.animateTo(Offset(0f, 0f))
                        }
                    }
                    launch {
                        iconsAlpha.animateTo(0f, tween(75))
                        delay(100)
                        iconsAlpha.snapTo(1f)
                    }
                }
            } else offsets.forEach { scope.launch { it.animateTo(Offset.Zero) } }
        }
    }

    BoxWithConstraints(modifier) {

        if (state == Menu || state is MenuToPlay)
            offsets.minus(offsets[0]).reversed().forEachIndexed { index, anim ->
                Surface(
                    modifier = Modifier
                        .offset { anim.value.toIntOffset() }
                        .padding(4.dp)
                        .size(48.dp)
                        .alpha(iconsAlpha.value),
                    onClick = {}, color = fabColor,
                    contentColor = fabTint, shape = CircleShape,
                    elevation = if (anim.value.getDistance() > 24) 4.dp else 0.dp
                ) {
                    Icon(
                        modifier = Modifier.padding(12.dp),
                        imageVector = items[items.lastIndex - index],
                        contentDescription = null
                    )
                }
            }

        Surface(
            modifier = Modifier
                .offset { offsets[0].value.toIntOffset() }
                .size(56.dp),
            shape = CircleShape, color = color.first, contentColor = color.second, elevation = 6.dp,
            onClick = {
                if (state != Menu && state !is MenuToPlay) onClick()
                if (offsets[0].value.getDistance() == 0f) onExpand(!expanded) }
        ) {

            if (state != Menu)
                Icon(
                    modifier = Modifier
                        .alpha(
                            if (state is MenuToPlay) 2 * (state.progress.coerceIn(
                                0.5f,
                                1f
                            ) - 0.5f) else 1f
                        )
                        .padding(12.dp),
                    painter = animatedVectorResource(R.drawable.play_to_pause).painterFor(atEnd = isPlaying),
                    contentDescription = "PlayButton",
                )

            if (state == Menu || state is MenuToPlay)
                Icon(
                    modifier = Modifier
                        .alpha(1 - 2 * state.progress.coerceIn(0f, 0.5f))
                        .drag(!expanded && state == Menu, offsets)
                        .padding(12.dp)
                        .rotate(
                            animateFloatAsState(
                                if (expanded) 135f else 0f,
                                spring(0.5f)
                            ).value
                        ),
                    imageVector = Icons.Rounded.Add, contentDescription = "Fab"
                )
        }
    }
}

fun Float.getMidColor(start: Color, end: Color): Color {
    return Color(
        red = start.red * (1 - this) + (end.red * this),
        green = start.green * (1 - this) + (end.green * this),
        blue = start.blue * (1 - this) + (end.blue * this)
    )
}

val MenuItems: List<ImageVector> = listOf(
    Icons.Rounded.Shuffle,
    Icons.Rounded.SortByAlpha,
    Icons.Rounded.GridView,
    Icons.Rounded.Settings,
)