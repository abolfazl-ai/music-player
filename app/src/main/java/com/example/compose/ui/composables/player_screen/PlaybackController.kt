package com.example.compose.ui.composables.player_screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.utils.resources.PlayerScreenSpacing
import com.example.compose.utils.resources.ProgressBarHeight

@Composable
fun IconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable BoxScope.() -> Unit
) = Box(
    modifier
        .clickable(
            onClick = onClick,
            enabled = enabled,
            role = Role.Button,
            interactionSource = interactionSource,
            indication = rememberRipple(bounded = false, radius = 32.dp)
        ),
    contentAlignment = Alignment.Center,
    content = content
)

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationGraphicsApi::class)
@Preview
@Composable
fun controls() {
    PlaybackController { 1f }
}

@ExperimentalMaterialApi
@ExperimentalAnimationGraphicsApi
@Composable
fun PlaybackController(alpha: () -> Float) = Row(
    Modifier.alpha(alpha()), verticalAlignment = Alignment.CenterVertically
) {
    IconButton(onClick = { }) {
        Icon(
            modifier = Modifier
                .size(56.dp)
                .padding(16.dp),
            imageVector = Icons.Rounded.Repeat,
            contentDescription = null,
        )
    }
    IconButton(onClick = { }) {
        Icon(
            modifier = Modifier
                .size(56.dp)
                .padding(12.dp),
            imageVector = Icons.Rounded.ChevronLeft,
            contentDescription = null,
        )
    }
    Spacer(modifier = Modifier.size(60.dp))
    IconButton(onClick = { }) {
        Icon(
            modifier = Modifier
                .size(56.dp)
                .padding(12.dp),
            imageVector = Icons.Rounded.ChevronRight,
            contentDescription = null,
        )
    }
    IconButton(onClick = { }) {
        Icon(
            modifier = Modifier
                .size(56.dp)
                .padding(16.dp),
            imageVector = Icons.Rounded.Shuffle,
            contentDescription = null,
        )
    }
}