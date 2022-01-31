package com.example.compose.ui.composables

import androidx.compose.animation.core.*
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.R
import com.example.compose.local.preferences.SortOrder
import com.example.compose.ui.composables.modifiers.drag
import com.example.compose.utils.kotlin_extensions.coerceAtLeast
import com.example.compose.utils.kotlin_extensions.getMidColor
import com.example.compose.utils.kotlin_extensions.toIntOffset
import com.example.compose.utils.resources.FabSize
import com.example.compose.utils.resources.MiniFabSize
import com.example.compose.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalAnimationGraphicsApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun DraggableFab(
    modifier: Modifier = Modifier,
    fabColor: Color = MaterialTheme.colorScheme.secondary,
    playButtonColor: Color = MaterialTheme.colorScheme.surface,
    transProgress: () -> Float = { 0f },
    items: List<ImageVector> = MenuItems,
    expanded: Boolean = false,
    onExpand: (expanded: Boolean) -> Unit = {},
    onDrag: (isDragging: Boolean) -> Unit = {},
    isPlaying: Boolean = false,
    viewModel: MainViewModel = viewModel(),
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

    val color = transProgress().let {
        when (it) {
            0f -> fabColor to contentColorFor(fabColor)
            1f -> playButtonColor to contentColorFor(playButtonColor)
            else -> it.getMidColor(fabColor, playButtonColor) to
                    it.getMidColor(contentColorFor(fabColor), contentColorFor(playButtonColor))
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

    BoxWithConstraints() {

        val minDistance = with(LocalDensity.current) { remember { 16.dp.roundToPx() } }

        if (transProgress() != 1f)
            offsets.minus(offsets[0]).reversed().forEachIndexed { index, anim ->
                Surface(
                    modifier = Modifier
                        .offset {
                            anim.value
                                .coerceAtLeast(minDistance)
                                .toIntOffset()
                        }
                        .padding(4.dp)
                        .size(MiniFabSize)
                        .alpha(iconsAlpha.value),
                    onClick = {
                        scope.launch {
                            viewModel.preferences.saveSongsSortOrder(SortOrder.ArtistASC)
                        }
                    }, color = fabColor,
                    contentColor = contentColorFor(fabColor), shape = CircleShape,
                    shadowElevation = if (anim.value.getDistance() > 24) 2.dp else 0.dp
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
                .offset {
                    offsets[0].value
                        .coerceAtLeast(minDistance)
                        .toIntOffset()
                }
                .size(FabSize),
            shape = CircleShape,
            color = color.first,
            contentColor = color.second, shadowElevation = (2 + 2 * (1 - transProgress())).dp,
            onClick = {
                when (transProgress()) {
                    1f -> onClick()
                    0f -> if (offsets[0].value.getDistance() < minDistance) onExpand(!expanded)
                }
            }
        ) {

            if (transProgress() != 0f) {
                val a = AnimatedImageVector.animatedVectorResource(R.drawable.play_to_pause)
                Icon(
                    modifier = Modifier
                        .alpha(2 * (transProgress().coerceIn(0.5f, 1f) - 0.5f))
                        .padding(16.dp),
                    painter = rememberAnimatedVectorPainter(a, isPlaying),
                    contentDescription = "PlayButton",
                )
            }

            if (transProgress() != 1f)
                Icon(
                    modifier = Modifier
                        .alpha(1 - 2 * transProgress().coerceIn(0f, 0.5f))
                        .drag(!expanded && transProgress() == 0f, offsets, onDrag)
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

val MenuItems: List<ImageVector> = listOf(
    Icons.Rounded.Shuffle,
    Icons.Rounded.SortByAlpha,
    Icons.Rounded.GridView,
    Icons.Rounded.Settings,
)