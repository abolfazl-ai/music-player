package com.example.compose.ui.composables.list_items.linear_item

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.compose.ui.theme.Red700

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
    OptionItem(modifier = modifier, imageVector = Icons.Default.Settings)
    OptionItem(modifier = modifier, imageVector = Icons.Default.DateRange)
    OptionItem(modifier = modifier, imageVector = Icons.Default.Favorite)
    OptionItem(modifier = modifier, imageVector = Icons.Default.Edit)
    OptionItem(modifier = modifier, imageVector = Icons.Default.Star)
    OptionItem(modifier = modifier, imageVector = Icons.Default.Info)
    OptionItem(modifier = modifier, imageVector = Icons.Default.Delete)
}

@Composable
fun OptionItem(modifier: Modifier = Modifier, imageVector: ImageVector) {
    Icon(
        imageVector,
        contentDescription = null,
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colors.surface)
            .clickable { }
            .padding(8.dp),
        tint = MaterialTheme.colors.onSurface
    )
}

@Composable
fun LargeItemOptions() = Row(
    modifier = Modifier
        .fillMaxSize()
        .padding(8.dp),
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
        LargeOptionItem(Icons.Rounded.Delete, "Delete", tint = Red700)
    }
}

@Composable
fun LargeOptionItem(
    imageVector: ImageVector,
    title: String,
    tint: Color = MaterialTheme.colors.onSurface,
    padding: Dp = 4.dp
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colors.surface)
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
        Text(text = title,color = tint)
    }
}