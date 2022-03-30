package com.example.compose.ui.composables.stage

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.compose.ui.composables.util_composables.EmoIconButton
import com.example.compose.utils.resources.StageSpacing
import com.example.compose.utils.resources.TimeLineHeight
import com.example.compose.utils.util_classes.PlaybackAction
import com.example.compose.viewmodel.MainViewModel

@Composable
fun PlaybackController(color: Color, buttonsAlpha: Float, timelineAlpha: Float, onAction: (PlaybackAction) -> Unit) {

    val animatedColor by animateColorAsState(color, tween(750))

    CompositionLocalProvider(LocalContentColor provides animatedColor) {

        Spacer(
            modifier = Modifier
                .padding(bottom = StageSpacing)
                .fillMaxWidth()
                .height(TimeLineHeight)
                .alpha(timelineAlpha)
                .alpha(0.5f)
                .background(LocalContentColor.current)

        )

        Row(Modifier.alpha(buttonsAlpha), verticalAlignment = Alignment.CenterVertically) {
            EmoIconButton(onClick = { onAction(PlaybackAction.REPEAT) }) {
                Icon(
                    modifier = Modifier
                        .size(56.dp)
                        .padding(16.dp),
                    imageVector = Icons.Rounded.Repeat,
                    contentDescription = null
                )
            }
            EmoIconButton(onClick = { onAction(PlaybackAction.PREVIOUS) }) {
                Icon(
                    modifier = Modifier
                        .size(56.dp)
                        .padding(12.dp),
                    imageVector = Icons.Rounded.ChevronLeft,
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.size(60.dp))
            EmoIconButton(onClick = { onAction(PlaybackAction.NEXT) }) {
                Icon(
                    modifier = Modifier
                        .size(56.dp)
                        .padding(12.dp),
                    imageVector = Icons.Rounded.ChevronRight,
                    contentDescription = null
                )
            }
            EmoIconButton(onClick = { onAction(PlaybackAction.SHUFFLE) }) {
                Icon(
                    modifier = Modifier
                        .size(56.dp)
                        .padding(16.dp),
                    imageVector = Icons.Rounded.Shuffle,
                    contentDescription = null
                )
            }
        }
    }
}