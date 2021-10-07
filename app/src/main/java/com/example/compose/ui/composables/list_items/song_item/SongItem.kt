package com.example.compose.ui.composables.list_items.song_item

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.compose.local.model.Song
import com.example.compose.ui.composables.list_items.ArrowToX

@ExperimentalAnimationApi
@Composable
fun SongItem(
    song: Song,
    expanded: Boolean = false,
    itemOptions: @Composable BoxScope.() -> Unit = { LargeItemOptions() },
    selected: Boolean = false,
    onExpand: (Boolean) -> Unit = {},
    onSelect: () -> Unit = {},
    onClick: () -> Unit = {},
) = Box {

    val cardHeight = remember { 56.dp }

    AnimatedVisibility(
        modifier = Modifier.padding(top = cardHeight / 4),
        visible = expanded,
        enter = expandVertically(animationSpec = spring(0.65f, 400f)),
        exit = shrinkVertically()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(4.dp, MaterialTheme.colors.surface, RoundedCornerShape(10.dp))
                .padding(top = cardHeight * 3 / 4)
        ) { itemOptions.invoke(this) }
    }

    SongCard(
        song, selected, onClick, onSelect,
        animateDpAsState(targetValue = (if (expanded) 5 else 0).dp).value, cardHeight
    ) {
        ArrowToX(
            modifier = it
                .background(MaterialTheme.colors.onBackground.copy(0.05f))
                .clickable { onExpand(!expanded) }
                .padding(8.dp),
            down = !expanded
        )
    }
}
