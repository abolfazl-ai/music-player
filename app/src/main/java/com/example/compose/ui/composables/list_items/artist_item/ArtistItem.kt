package com.example.compose.ui.composables.list_items.artist_item

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.local.model.Artist
import com.example.compose.ui.Screen
import com.example.compose.ui.composables.icons.Artist
import com.example.compose.ui.composables.icons.Play
import com.example.compose.ui.composables.list_items.Selectable
import com.example.compose.ui.theme.DarkGray

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun ArtistItem(
    artist: Artist,
    index: Int,
    selected: Boolean = false,
    onSelect: (Int) -> Unit = {},
    onClick: () -> Unit = {}
) {
    val selectAnimator by animateFloatAsState(
        targetValue = if (selected) 1.1f else -0.1f,
        animationSpec = spring(if (selected) 0.5f else 1f, if (selected) 600f else 1500f)
    )

    Surface(
        shape = RoundedCornerShape(7.dp),
        border = if (selectAnimator > 0f) BorderStroke(
            (4 * selectAnimator.coerceIn(-1f, 1f)).dp,
            MaterialTheme.colors.primary
        ) else null,
        color = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.onSurface
    ) {
        Column(
            modifier = Modifier
                .combinedClickable(onLongClick = { onSelect(index) }, onClick = onClick)
                .padding(6.dp)
                .scale(1 - selectAnimator / 25, 1 - selectAnimator / 30),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BoxWithConstraints(
                Modifier
                    .padding(bottom = 6.dp)
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(4.dp))
                    .background(DarkGray)
                    .padding(4.dp)
            ) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    imageVector = Icons.Filled.Artist,
                    contentDescription = null,
                    tint = Color.White
                )
/*                val den = LocalDensity.current
                val size = remember { with(den) { Size(maxWidth.roundToPx(), maxHeight.roundToPx()) } }
                GlideImage(imageModel = ArtistAndSize(artist, size))*/
            }
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(
                    Modifier
                        .weight(1f)
                        .padding(start = 2.dp)
                ) {
                    Text(
                        text = artist.name,
                        style = MaterialTheme.typography.subtitle1,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = "${artist.albumsNumber} albums ${artist.tracksNumber} tracks",
                        style = MaterialTheme.typography.subtitle2,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Selectable(
                    modifier = Modifier
                        .height(35.dp)
                        .aspectRatio(1f),
                    progress = selectAnimator,
                    selectColor = MaterialTheme.colors.primary,
                    backgroundColor = MaterialTheme.colors.onBackground.copy(0.15f),
                    shape = RoundedCornerShape(4.dp),
                    onclick = { if (selectAnimator > 0) onSelect(index) }
                ) {
                    Icon(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp),
                        imageVector = Icons.Filled.Play,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Preview
@Composable
fun Preview() {
    ArtistItem(artist = Artist(0L, "Selena Gomez", 20, 10, 0L), 1)
}