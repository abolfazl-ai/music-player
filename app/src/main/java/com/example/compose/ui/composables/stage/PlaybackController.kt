package com.example.compose.ui.composables.stage

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.compose.utils.resources.StageSpacing
import com.example.compose.utils.resources.TimeLineHeight
import com.example.compose.viewmodel.MainViewModel

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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlaybackController(
    onRepeat: () -> Unit = {}, onPrev: () -> Unit = {},
    onNext: () -> Unit = {}, onShuffle: () -> Unit = {},
    viewModel: MainViewModel = hiltViewModel()
) {

    val state by viewModel.playbackControlsState.collectAsState()
    val color by animateColorAsState(state.color, tween(750))

    Spacer(
        modifier = Modifier
            .padding(bottom = StageSpacing)
            .fillMaxWidth()
            .height(TimeLineHeight)
            .alpha(state.timeLineAlpha)
            .alpha(0.5f)
            .background(color)

    )

    Row(Modifier.alpha(state.buttonsAlpha), verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onRepeat) {
            Icon(
                modifier = Modifier
                    .size(56.dp)
                    .padding(16.dp),
                imageVector = Icons.Rounded.Repeat,
                contentDescription = null, tint = color
            )
        }
        IconButton(onClick = onPrev) {
            Icon(
                modifier = Modifier
                    .size(56.dp)
                    .padding(12.dp),
                imageVector = Icons.Rounded.ChevronLeft,
                contentDescription = null, tint = color
            )
        }
        Spacer(modifier = Modifier.size(60.dp))
        IconButton(onClick = onNext) {
            Icon(
                modifier = Modifier
                    .size(56.dp)
                    .padding(12.dp),
                imageVector = Icons.Rounded.ChevronRight,
                contentDescription = null, tint = color
            )
        }
        IconButton(onClick = onShuffle) {
            Icon(
                modifier = Modifier
                    .size(56.dp)
                    .padding(16.dp),
                imageVector = Icons.Rounded.Shuffle,
                contentDescription = null, tint = color
            )
        }
    }
}