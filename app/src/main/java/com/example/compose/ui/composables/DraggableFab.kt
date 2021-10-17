package com.example.compose.ui.composables

import androidx.compose.animation.core.*
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
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.compose.ui.composables.modifiers.drag
import com.example.compose.utils.toIntOffset

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
    backgroundColor: Color = MaterialTheme.colors.secondary,
    contentColor: Color = MaterialTheme.colors.onPrimary,
    expanded: Boolean = false,
    onExpand: (expanded: Boolean) -> Unit = {}
) {

    val iconsAlpha = remember { Animatable(1f) }
    val offsets = remember(items) {
        ArrayList<Animatable<Offset, AnimationVector2D>>().also { list ->
            repeat(items.size + 1) {
                list.add(Animatable(Offset.Zero, Offset.VectorConverter))
            }
        }
    }

/*    with(LocalDensity.current) {
        LaunchedEffect(expanded) {
            val spec = spring<Float>(Spring.DampingRatioMediumBouncy)
            if (expanded) {
                iconsAlpha.snapTo(0f)
                offsets.reversed().forEachIndexed { index, anim ->
                    launch {
                        anim.animateTo(
                            Offset(0f, -(60 + index * 56).dp.toPx()),
                            spring(Spring.DampingRatioMediumBouncy)
                        )
                    }
                }
                launch { iconsAlpha.animateTo(1f) }
                launch { rotation.animateTo(135f, spec) }
            } else {
                offsets.forEach {
                    launch {
                        it.animateTo(Offset(0f, 0f))
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
    }*/

    BoxWithConstraints(modifier) {

        offsets.minus(offsets[0]).reversed().forEachIndexed { index, anim ->
            Surface(
                modifier = Modifier
                    .offset { anim.value.toIntOffset() }
                    .padding(4.dp)
                    .size(48.dp)
                    .alpha(iconsAlpha.value),
                onClick = {}, color = backgroundColor,
                contentColor = contentColor, shape = CircleShape,
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
                .size(56.dp)
                .rotate(
                    animateFloatAsState(
                        if (expanded) 135f else 0f,
                        spring(Spring.DampingRatioMediumBouncy)
                    ).value
                ),
            color = backgroundColor,
            contentColor = contentColor,
            elevation = 6.dp,
            shape = CircleShape, onClick = { onExpand(!expanded) }
        ) {
            Icon(
                modifier = Modifier
                    .drag(!expanded, offsets)
                    .padding(12.dp),
                imageVector = Icons.Rounded.Add,
                contentDescription = null
            )
        }
    }
}