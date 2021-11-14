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
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.MaterialTheme
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
import com.example.compose.utils.resources.FabSize
import com.example.compose.utils.resources.MiniFabSize
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalAnimationGraphicsApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun DraggableFab(
    modifier: Modifier = Modifier,
    fabColor: Color = MaterialTheme.colorScheme.secondary,
    fabTint: Color = MaterialTheme.colorScheme.onSecondary,
    playButtonColor: Color = MaterialTheme.colorScheme.surface,
    playButtonTint: Color = MaterialTheme.colorScheme.onSurface,
    transProgress: () -> Float = { 0f },
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

    val color = remember(transProgress()) {
        transProgress().let {
            when (it) {
                0f -> fabColor to fabTint
                1f -> playButtonColor to playButtonTint
                else -> it.getMidColor(fabColor, playButtonColor) to
                        it.getMidColor(fabTint, playButtonTint)
            }
        }
    }
    val scope = rememberCoroutineScope()

    with(LocalDensity.current) {
        LaunchedEffect(expanded, transProgress()) {
            if (transProgress() == 0f) {
                if (expanded) {
                    iconsAlpha.snapTo(0f)
                    offsets.minus(offsets[0]).reversed().forEachIndexed { index, anim ->
                        launch {
                            anim.animateTo(
                                Offset(0f, -(FabSize.times(index) + 60.dp).toPx()),
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

        if (transProgress() != 1f)
            offsets.minus(offsets[0]).reversed().forEachIndexed { index, anim ->
                Surface(
                    modifier = Modifier
                        .offset { anim.value.toIntOffset() }
                        .padding(4.dp)
                        .size(MiniFabSize)
                        .alpha(iconsAlpha.value),
                    onClick = {}, color = fabColor,
                    contentColor = fabTint, shape = CircleShape,
                    elevation = if (anim.value.getDistance() > 24) 4.dp else 0.dp
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
                .offset { offsets[0].value.toIntOffset() }
                .size(FabSize),
            shape = CircleShape, color = color.first, contentColor = color.second, elevation = 6.dp,
            onClick = {
                if (transProgress() == 1f) onClick()
                else if (transProgress() == 0f && offsets[0].value.getDistance() == 0f) onExpand(!expanded)
            }
        ) {

            if (transProgress() != 0f)
                Icon(
                    modifier = Modifier
                        .alpha(2 * (transProgress().coerceIn(0.5f, 1f) - 0.5f))
                        .padding(12.dp),
                    painter = animatedVectorResource(R.drawable.play_to_pause).painterFor(atEnd = isPlaying),
                    contentDescription = "PlayButton",
                )

            if (transProgress() != 1f)
                Icon(
                    modifier = Modifier
                        .alpha(1 - 2 * transProgress().coerceIn(0f, 0.5f))
                        .drag(!expanded && transProgress() == 0f, offsets)
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