package com.example.compose.ui.composables.list_items.album_item

import android.util.Size
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.compose.local.model.Album
import com.example.compose.ui.Screen
import com.example.compose.ui.composables.icons.Play
import com.example.compose.ui.composables.list_items.Selectable
import com.example.compose.ui.theme.DarkGray
import com.example.compose.utils.default_pictures.AlbumAndSize
import com.skydoves.landscapist.glide.GlideImage

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun AlbumItem(
    album: Album,
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

//        if (selectAnimator > 0)
        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        )

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
            ) {

                Spacer(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.25.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(DarkGray)
                )

                val den = LocalDensity.current
                val size =
                    remember { with(den) { Size(maxWidth.roundToPx(), maxHeight.roundToPx()) } }

                GlideImage(imageModel = AlbumAndSize(album, size))
            }
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {
                Column(
                    Modifier
                        .weight(1f)
                        .padding(start = 2.dp, end = 8.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = album.name.trim(),
                        style = MaterialTheme.typography.subtitle1,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = album.artist.trim(),
                        style = MaterialTheme.typography.subtitle2,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                Selectable(
                    modifier = Modifier.size(35.dp),
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