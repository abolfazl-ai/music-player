package com.example.compose.ui.composables.list_items

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.size.Size
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
    val animator by animateFloatAsState(if (selected) 1.1f else -0.1f, spring(if (selected) 0.5f else 1f, if (selected) 600f else 1500f))
    var size by remember { mutableStateOf(Size(1, 1)) }

    Surface(
        shape = remember { RoundedCornerShape(6.dp) }, color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        border = if (animator > 0f) BorderStroke((4 * animator.coerceIn(-1f, 1f)).dp, MaterialTheme.colorScheme.secondary) else null
    ) {
        Column(
            Modifier.combinedClickable(onLongClick = onSelect, onClick = onClick).padding(padding)
                .scale(1 - animator / 25, 1 - animator / 30), horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(Modifier.padding(bottom = 6.dp).fillMaxWidth().aspectRatio(1f)
                .onGloballyPositioned { size = Size(it.size.width, it.size.height) }
                .clip(remember { RoundedCornerShape(4.dp) })
            ) {
                Spacer(Modifier.fillMaxSize().padding(0.2.dp).clip(RoundedCornerShape(4.dp)).background(MaterialTheme.colorScheme.tertiary))
                if (size.width.hashCode() > 1) picture(size)
            }

            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
                Column(Modifier.weight(1f).padding(start = 2.dp, end = 8.dp, bottom = 2.dp), Arrangement.SpaceBetween) {
                    Text(text = title, style = MaterialTheme.typography.labelLarge, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(text = subtitle, style = MaterialTheme.typography.labelSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                Icon(
                    modifier = Modifier.size(35.dp).clip(RoundedCornerShape(4.dp)).selectable(selected)
                        .background(MaterialTheme.colorScheme.surfaceVariant).clickable { if (animator > 0) onSelect() }
                        .padding(4.dp), imageVector = Icons.Filled.Play, contentDescription = "Play Item",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}