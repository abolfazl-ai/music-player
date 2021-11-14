package com.example.compose.ui.composables.list_items

import android.util.Size
import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.times
import com.example.compose.ui.composables.icons.animated.ArrowToX
import com.example.compose.ui.composables.modifiers.selectable
import com.example.compose.ui.theme.Red700
import com.example.compose.utils.resources.*

@ExperimentalFoundationApi
@Preview
@Composable
fun LinearItemPrev() {
    LinearItem(title = "There Must Be A Sweeter Place We Ca",
        subtitle = "Selena Gomez",
        description = "2:31",
        picture = { _, _ ->
            Spacer(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Red700)
            )
        })
}

@ExperimentalFoundationApi
@Composable
fun LinearItem(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    description: String = "",
    picture: @Composable BoxScope.(shape: Shape, size: Size) -> Unit,
    expanded: Boolean = false,
    itemOptions: @Composable BoxScope.() -> Unit = { ItemOptions() },
    itemOptionsHeight: Dp = IconOptionsHeight,
    selected: Boolean = false,
    onExpand: (Boolean) -> Unit = {},
    onSelect: () -> Unit = {},
    onClick: () -> Unit = {},
) {
    val expandAnimator by animateFloatAsState(
        targetValue = if (expanded) 1f else 0f,
        tween(if (expanded) 400 else 300, easing = { OvershootInterpolator(2f).getInterpolation(it) })
    )

    Box(modifier) {

        if (expandAnimator > 0)
            Layout(modifier = Modifier
                .height((LinearItemHeight + expandAnimator * itemOptionsHeight))
                .clip(RoundedCornerShape(LinearItemCornerRadius)), content = {
                Box(
                    modifier = Modifier
                        .border(
                            4.dp,
                            MaterialTheme.colorScheme.surface,
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
            title, subtitle, description, picture,
            selected, onClick, onSelect, elevation = { (4 * expandAnimator).dp }
        ) {
            ArrowToX(modifier = it
                .background(MaterialTheme.colorScheme.surfaceVariant)
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
    picture: @Composable BoxScope.(shape: Shape, size: Size) -> Unit,
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
        modifier = Modifier.height(LinearItemHeight),
        shape = remember { RoundedCornerShape(LinearItemCornerRadius) },
        shadowElevation = elevation(), border = selectAnimator.let {
            if (it > 0f) BorderStroke(
                (2.5 * it.coerceIn(0f, 1f)).dp,
                MaterialTheme.colorScheme.secondary
            )
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
                ) { picture(clipShape, size) }

                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = description,
                    style = MaterialTheme.typography.labelSmall,
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

            c.copy(
                minWidth = size.width,
                maxWidth = size.width,
                minHeight = size.width,
                maxHeight = size.height
            ).let { pictureP = m[0].measure(it);expandP = m[4].measure(it) }

            with(c.copy(minWidth = 0, minHeight = 0)) {
                val textWidth = maxWidth - 2 * size.width - 2 * LinearItemSpacing.roundToPx()
                titleP = m[1].measure(copy(maxWidth = textWidth))
                subtitleP = m[2].measure(copy(maxWidth = textWidth - 100.dp.roundToPx()))
                detailsP = m[3].measure(copy(maxWidth = 100.dp.roundToPx()))
            }

            layout(c.maxWidth, (LinearItemHeight - LinearItemPadding.times(2)).roundToPx()) {

                pictureP.place(0, 0)

                expandP.place(c.maxWidth - expandP.width, 0)

                titleP.place(
                    pictureP.width + LinearItemSpacing.roundToPx(),
                    c.maxHeight / 2 - (titleP.height + subtitleP.height) / 2 - 2.dp.roundToPx()
                )

                subtitleP.place(
                    pictureP.width + LinearItemSpacing.roundToPx(),
                    c.maxHeight / 2 + titleP.height - (titleP.height + subtitleP.height) / 2 + 2.dp.roundToPx()
                )

                detailsP.place(
                    c.maxWidth - pictureP.width - LinearItemSpacing.roundToPx() - detailsP.width,
                    c.maxHeight / 2 + 2.dp.roundToPx()
                )
            }
        }
    }
}

@Composable
fun ItemOptions() = Row(
    modifier = Modifier
        .fillMaxSize()
        .padding(8.dp),
    verticalAlignment = Alignment.Bottom,
    horizontalArrangement = Arrangement.spacedBy(4.dp)
)
{
    val modifier = Modifier.weight(1f)
    OptionItem(modifier, Icons.Rounded.PlayArrow, padding = 6.dp)
    OptionItem(modifier, Icons.Rounded.SkipNext, padding = 4.dp)
    OptionItem(modifier, Icons.Rounded.AddBox)
    OptionItem(modifier, Icons.Rounded.ExitToApp)
    OptionItem(modifier, Icons.Rounded.Info)
    OptionItem(modifier, Icons.Rounded.Share, padding = 9.dp)
    OptionItem(modifier, Icons.Rounded.Delete, tint = MaterialTheme.colorScheme.error)
}

@Composable
fun OptionItem(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    tint: Color = MaterialTheme.colorScheme.onSurface,
    padding: Dp = 8.dp
) {
    Icon(
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { }
            .padding(padding),
        imageVector = imageVector,
        contentDescription = null,
        tint = tint
    )
}

@Composable
fun LargeItemOptions() = Row(
    modifier = Modifier.padding(8.dp),
    horizontalArrangement = Arrangement.spacedBy(4.dp)
)
{
    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        LargeOptionItem(Icons.Rounded.PlayArrow, "Play", padding = 2.dp)
        LargeOptionItem(Icons.Rounded.QueueMusic, "Add to queue")
        LargeOptionItem(Icons.Rounded.ExitToApp, "Go to")
        LargeOptionItem(Icons.Rounded.Info, "Details")
    }
    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        LargeOptionItem(Icons.Rounded.SkipNext, "Play next", padding = 0.dp)
        LargeOptionItem(Icons.Rounded.AddBox, "Add to playlist")
        LargeOptionItem(Icons.Rounded.Share, "Share", padding = 5.dp)
        LargeOptionItem(Icons.Rounded.Delete, "Delete", tint = MaterialTheme.colorScheme.error)
    }
}

@Composable
fun LargeOptionItem(
    imageVector: ImageVector,
    title: String,
    tint: Color = MaterialTheme.colorScheme.onSurface,
    padding: Dp = 4.dp
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { }
            .padding(5.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Icon(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .padding(padding),
            imageVector = imageVector,
            contentDescription = null,
            tint = tint
        )
        Text(text = title, color = tint)
    }
}