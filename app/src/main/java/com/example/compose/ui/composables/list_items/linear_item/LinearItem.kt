package com.example.compose.ui.composables.list_items.linear_item

import android.util.Size
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.example.compose.ui.composables.list_items.ArrowToX

@ExperimentalAnimationApi
@Composable
fun LinearItem(
    title: String,
    subtitle: String,
    description: String = "",
    picture: @Composable BoxScope.(size: Size) -> Unit = {},
    expanded: Boolean = false,
    itemOptions: @Composable BoxScope.() -> Unit = { ItemOptions() },
    selected: Boolean = false,
    onExpand: (Boolean) -> Unit = {},
    onSelect: () -> Unit = {},
    onClick: () -> Unit = {},
) = Box {

    val cardHeight = remember { 56.dp }

    AnimatedVisibility(
        modifier = Modifier.padding(top = cardHeight / 4),
        visible = expanded,
        enter = expandVertically(animationSpec = spring(0.65f, 600f)),
        exit = shrinkVertically()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(4.dp, MaterialTheme.colors.surface, RoundedCornerShape(6.dp))
                .padding(top = cardHeight * 3 / 4),
            content = itemOptions
        )
    }

    LinearItemCard(
        title, subtitle, description, picture, selected, onClick, onSelect,
        animateDpAsState(targetValue = (if (expanded) 2 else 0).dp).value, cardHeight
    ) {
        Icon(modifier = it
            .background(MaterialTheme.colors.onSurface.copy(0.05f))
            .clickable { onExpand(!expanded) }
            .padding(8.dp)
            .rotate(animateFloatAsState(if (expanded) 180f else 0f,spring(0.65f, 400f)).value),
            imageVector = Icons.Rounded.ExpandMore,
            contentDescription = "Expand")
/*        ArrowToX(
            modifier = it
                .background(MaterialTheme.colors.onSurface.copy(0.04f))
                .clickable { onExpand(!expanded) }
                .padding(8.dp),
            down = !expanded
        )*/
    }
}