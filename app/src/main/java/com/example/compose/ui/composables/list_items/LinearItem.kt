package com.example.compose.ui.composables.list_items

import android.util.Size
import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import com.example.compose.ui.composables.icons.animated.ArrowToX
import com.example.compose.ui.composables.list_items.linear_item.ItemOptions
import com.example.compose.ui.composables.modifiers.selectable
import com.example.compose.utils.resources.LinearItemCornerRadius
import com.example.compose.utils.resources.LinearItemHeight
import com.example.compose.utils.resources.LinearItemPadding
import com.example.compose.utils.resources.LinearItemSpacing

@ExperimentalFoundationApi
@Composable
fun LinearItem2(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    description: String = "",
    picture: @Composable (modifier: Modifier, size: Size) -> Unit,
    expanded: Boolean = false,
    itemOptions: @Composable BoxScope.() -> Unit = { ItemOptions() },
    selected: Boolean = false,
    onExpand: (Boolean) -> Unit = {},
    onSelect: () -> Unit = {},
    onClick: () -> Unit = {},
) {
    val expandAnimator by animateIntAsState(
        targetValue = if (expanded) 100 else 0,
        tween(easing = { OvershootInterpolator(3f).getInterpolation(it) })
    )

    Layout(modifier = modifier.clipToBounds(), content = {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(4.dp, MaterialTheme.colors.surface, RoundedCornerShape(6.dp))
                .padding(top = 16.dp),
            content = itemOptions
        )

        LinearItemCard(
            title, subtitle, description, picture, selected, onClick, onSelect,
            animateDpAsState(targetValue = (if (expanded) 2 else 0).dp).value
        ) {
            ArrowToX(modifier = it
                .background(MaterialTheme.colors.onSurface.copy(0.05f))
                .clickable { onExpand(!expanded) }
                .padding(8.dp), x = expanded)
        }

    }) { m, c ->

        val (optionsPlaceable, cardPlaceable) = m.map { it.measure(c) }

        val height = cardPlaceable.height + (optionsPlaceable.height - 16.dp.roundToPx()) *
                expandAnimator.coerceAtLeast(0) / 100

        layout(c.maxWidth, height) {
            if (expandAnimator > 0) optionsPlaceable.place(0, height - optionsPlaceable.height)
            cardPlaceable.place(0, 0)
        }

    }
}

@ExperimentalFoundationApi
@Composable
internal fun LinearItemCard(
    title: String,
    subtitle: String,
    description: String = "",
    picture: @Composable (modifier: Modifier, size: Size) -> Unit,
    selected: Boolean = false,
    onClick: () -> Unit = {},
    onSelect: () -> Unit = {},
    elevation: Dp = 0.dp,
    expandIcon: @Composable (modifier: Modifier) -> Unit = {}
) {

    val selectAnimator by animateFloatAsState(if (selected) 1.1f else -0.1f, spring(0.5f))
    val clipShape =
        remember { RoundedCornerShape(max(LinearItemCornerRadius - LinearItemPadding, 4.dp)) }
    val size = with(LocalDensity.current) {
        remember {
            (LinearItemHeight - LinearItemPadding.times(2)).roundToPx().let { Size(it, it) }
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(LinearItemHeight),
        shape = remember { RoundedCornerShape(LinearItemCornerRadius) },
        elevation = elevation, border = selectAnimator.let {
            if (it > 0f) BorderStroke((2.5 * it.coerceIn(0f, 1f)).dp, MaterialTheme.colors.primary)
            else null
        }
    ) {
        Layout(
            modifier = Modifier
                .combinedClickable(onLongClick = onSelect, onClick = onClick)
                .padding(LinearItemPadding), content = {

                picture(
                    Modifier
                        .scale(1 - selectAnimator / 15)
                        .clip(clipShape)
                        .background(MaterialTheme.colors.onSurface)
                        .selectable(selected)
                        .clickable(onClick = onSelect), size
                )

                Text(
                    text = title,
                    style = MaterialTheme.typography.subtitle1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.subtitle2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = description,
                    style = MaterialTheme.typography.body1,
                )

                expandIcon(
                    modifier = Modifier
                        .scale(1 - selectAnimator / 15)
                        .clip(clipShape)
                )

            }) { m, c ->

            val pictureP: Placeable
            val expandP: Placeable
            val titleP: Placeable
            val subtitleP: Placeable
            val detailsP: Placeable

            val subtitleY: Int

            c.copy(
                minWidth = size.width,
                maxWidth = size.width,
                minHeight = size.width,
                maxHeight = size.height
            ).let { pictureP = m[0].measure(it);expandP = m[4].measure(it) }

            with(c.copy(minWidth = 0)) {
                val textWidth = maxWidth - 2 * size.width - 2 * LinearItemSpacing.roundToPx()
                titleP = m[1].measure(copy(maxWidth = textWidth))
                subtitleY = maxHeight / 2 + LinearItemSpacing.div(3).roundToPx()
                subtitleP = m[2].measure(copy(maxWidth = textWidth - 100.dp.roundToPx()))
                detailsP = m[3].measure(copy(maxWidth = 100.dp.roundToPx()))
            }

            layout(c.maxWidth, c.maxHeight) {
                pictureP.place(0, 0)

                expandP.place(c.maxWidth - expandP.width, 0)

                titleP.place(
                    pictureP.width + LinearItemSpacing.roundToPx(),
                    c.maxHeight / 2 - titleP[FirstBaseline] - LinearItemSpacing.div(3).roundToPx()
                )

                subtitleP.place(pictureP.width + LinearItemSpacing.roundToPx(), subtitleY)

                detailsP.place(
                    c.maxWidth - expandP.width - LinearItemSpacing.roundToPx() - detailsP.width,
                    subtitleY + subtitleP.height - detailsP.height
                )
            }
        }
    }
}