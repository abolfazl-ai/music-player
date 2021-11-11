package com.example.compose.ui.composables.list_items

import android.util.Size
import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.times
import com.example.compose.ui.composables.icons.animated.ArrowToX
import com.example.compose.ui.composables.list_items.linear_item.ItemOptions
import com.example.compose.ui.composables.modifiers.selectable
import com.example.compose.utils.resources.*

@ExperimentalFoundationApi
@Composable
fun LinearItem2(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    description: String = "",
    picture: @Composable BoxScope.(shape:Shape,size: Size) -> Unit,
    expanded: Boolean = false,
    itemOptions: @Composable BoxScope.() -> Unit = { ItemOptions() },
    itemOptionsHeight: Dp = IconOptionsHeight,
    selected: Boolean = false,
    onExpand: (Boolean) -> Unit = {},
    onSelect: () -> Unit = {},
    onClick: () -> Unit = {},
) {
    val animator by animateFloatAsState(
        targetValue = if (expanded) 1f else 0f,
        tween(easing = { OvershootInterpolator(3f).getInterpolation(it) })
    )

    Box(modifier) {

        if (animator > 0)
            Layout(modifier = Modifier
                .height((LinearItemHeight + animator * itemOptionsHeight))
                .clip(RoundedCornerShape(LinearItemCornerRadius)), content = {
                Box(
                    modifier = Modifier
                        .border(
                            4.dp,
                            MaterialTheme.colors.surface,
                            RoundedCornerShape(6.dp)
                        )
                        .padding(top = LinearItemHeight),
                    contentAlignment = Alignment.BottomCenter, content = itemOptions
                )
            }) { m, c ->
                val placeables = m.map {
                    it.measure(
                        c.copy(
                            minHeight = 0,
                            maxHeight = (itemOptionsHeight + LinearItemHeight).roundToPx()
                        )
                    )
                }
                layout(c.maxWidth, c.maxHeight) {
                    placeables.forEach { it.place(0, c.maxHeight - it.height) }
                }
            }

        LinearItemCard(
            title,
            subtitle,
            description,
            picture,
            selected,
            onClick,
            onSelect,
            elevation = { (2 * animator).dp }
        ) {
            ArrowToX(modifier = it
                .background(MaterialTheme.colors.onSurface.copy(0.05f))
                .clickable { onExpand(!expanded) }
                .padding(8.dp), x = expanded)
        }
    }
}

@ExperimentalFoundationApi
@Composable
internal fun LinearItemCard(
    title: String,
    subtitle: String,
    description: String = "",
    picture: @Composable BoxScope.(shape:Shape,size: Size) -> Unit,
    selected: Boolean = false,
    onClick: () -> Unit = {},
    onSelect: () -> Unit = {},
    elevation: () -> Dp = { 0.dp },
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
        Modifier.height(LinearItemHeight),
        shape = remember { RoundedCornerShape(LinearItemCornerRadius) },
        elevation = elevation(), border = selectAnimator.let {
            if (it > 0f) BorderStroke((2.5 * it.coerceIn(0f, 1f)).dp, MaterialTheme.colors.primary)
            else null
        }
    ) {
        Layout(
            modifier = Modifier
                .combinedClickable(onLongClick = onSelect, onClick = onClick)
                .padding(LinearItemPadding), content = {

                Box(
                    Modifier
                        .scale(1 - selectAnimator / 15)
                        .clip(clipShape)
                        .selectable(selected)
                        .clickable(onClick = onSelect)
                ) { picture(clipShape,size) }

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
                subtitleY = maxHeight / 2 + LinearItemSpacing.div(3.5f).roundToPx()
                subtitleP = m[2].measure(copy(maxWidth = textWidth - 100.dp.roundToPx()))
                detailsP = m[3].measure(copy(maxWidth = 100.dp.roundToPx()))
            }

            layout(c.maxWidth, (LinearItemHeight - LinearItemPadding.times(2)).roundToPx()) {

                pictureP.place(0, 0)

                expandP.place(c.maxWidth - expandP.width, 0)

                titleP.place(
                    pictureP.width + LinearItemSpacing.roundToPx(),
                    c.maxHeight / 2 - titleP[FirstBaseline] - LinearItemSpacing.div(3.5f)
                        .roundToPx()
                )

                subtitleP.place(pictureP.width + LinearItemSpacing.roundToPx(), subtitleY)

                detailsP.place(
                    c.maxWidth - pictureP.width - LinearItemSpacing.roundToPx() - detailsP.width,
                    subtitleY
                )
            }
        }
    }
}