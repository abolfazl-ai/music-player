package com.example.compose.ui.composables.list_items.folder_item

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import com.example.compose.local.model.Folder
import com.example.compose.ui.Screen
import com.example.compose.ui.composables.icons.Folder
import com.example.compose.ui.composables.list_items.Selectable

@Composable
fun FolderCard(
    folder: Folder,
    selected: Boolean = false,
    onClick: () -> Unit = {},
    onSelect: () -> Unit = {},
    elevation: Dp = 0.dp,
    height: Dp = 56.dp,
    padding: Dp = 5.dp,
    spacing: Dp = 8.dp,
    cornerRadius: Dp = 6.dp,
    expandIcon: @Composable RowScope.(modifier: Modifier) -> Unit = {}
) {

    val selectAnimator by animateFloatAsState(
        targetValue = if (selected) 1.1f else -0.1f,
        animationSpec = spring(if (selected) 0.5f else 1f, if (selected) 600f else 1500f)
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
        shape = remember { RoundedCornerShape(cornerRadius) },
        elevation = elevation,
        border = if (selectAnimator > 0f) {
            BorderStroke((2 * selectAnimator.coerceIn(-1f, 1f)).dp, MaterialTheme.colors.primary)
        } else null
    ) {

        val clipShape = remember { RoundedCornerShape(max(cornerRadius - padding, 4.dp)) }

        Row(
            modifier = Modifier.clickable(onClick = onClick),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Selectable(
                Modifier
                    .size(height)
                    .padding(padding)
                    .scale(1 - selectAnimator / 10),
                progress = selectAnimator,
                selectColor = MaterialTheme.colors.primary,
                shape = clipShape,
                onclick = onSelect
            ) {
                Icon(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    imageVector = Icons.Rounded.Folder,
                    contentDescription = null,
                    tint = Color.White
                )
            }

            Column(
                Modifier.padding(spacing, padding).weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                Text(
                    text = folder.title,
                    style = MaterialTheme.typography.subtitle1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Contains ${folder.tracksNumber} tracks",
                    style = MaterialTheme.typography.subtitle2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            expandIcon(
                modifier = Modifier
                    .size(height)
                    .padding(padding)
                    .scale(1 - selectAnimator / 10)
                    .clip(clipShape)
            )
        }
    }
}