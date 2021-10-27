package com.example.compose.ui.composables.list_items.linear_item

import android.util.Size
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.compose.ui.composables.icons.animated.ArrowToX2

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun LinearItem(
    modifier: Modifier = Modifier,
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
) = Box(modifier) {

    val cardHeight = remember { 56.dp }

    AnimatedVisibility(
        modifier = Modifier.padding(top = cardHeight / 4),
        visible = expanded,
        enter = expandVertically(spring(0.55f, 600f)),
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
/*        Icon(modifier = it
            .background(MaterialTheme.colors.onSurface.copy(0.05f))
            .clickable { onExpand(!expanded) }
            .padding(8.dp)
            .rotate(animateFloatAsState(if (expanded) 180f else 0f, spring(0.55f, 500f)).value),
            imageVector = Icons.Rounded.ExpandMore,
            contentDescription = "Expand")*/

        ArrowToX2(modifier = it
            .background(MaterialTheme.colors.onSurface.copy(0.05f))
            .clickable { onExpand(!expanded) }
            .padding(8.dp), x = expanded)
    }
}