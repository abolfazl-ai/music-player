package com.example.compose.ui.composables.list_items

import android.util.Size
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.compose.ui.composables.icons.Play
import com.example.compose.ui.composables.modifiers.selectable

@ExperimentalFoundationApi
@Composable
fun GridItem(
    title: String,
    subtitle: String,
    picture: @Composable BoxScope.(size: Size) -> Unit = {},
    padding: Dp = 5.dp,
    selected: Boolean = false,
    onSelect: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    val selectAnimator by animateFloatAsState(
        targetValue = if (selected) 1.1f else -0.1f,
        animationSpec = spring(if (selected) 0.5f else 1f, if (selected) 600f else 1500f)
    )

    Surface(
        shape = remember { RoundedCornerShape(6.dp) },
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        border = if (selectAnimator > 0f) BorderStroke(
            (4 * selectAnimator.coerceIn(-1f, 1f)).dp,
            MaterialTheme.colorScheme.secondary
        ) else null,
    ) {
        Column(
            modifier = Modifier
                .combinedClickable(onLongClick = onSelect, onClick = onClick)
                .padding(padding)
                .scale(1 - selectAnimator / 25, 1 - selectAnimator / 30),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BoxWithConstraints(
                Modifier
                    .padding(bottom = 6.dp)
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(remember { RoundedCornerShape(4.dp) }),
            ) {

                Spacer(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.25.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.tertiary)
                )

                val size = with(LocalDensity.current) {
                    remember {
                        (maxWidth - padding.times(2)).roundToPx().let { Size(it, it) }
                    }
                }

                picture(size)
            }
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {
                Column(
                    Modifier
                        .weight(1f)
                        .padding(start = 2.dp, end = 8.dp, bottom = 2.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
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
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                Icon(
                    modifier = Modifier
                        .size(35.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .selectable(selected)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { if (selectAnimator > 0) onSelect() }
                        .padding(4.dp),
                    imageVector = Icons.Filled.Play,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}