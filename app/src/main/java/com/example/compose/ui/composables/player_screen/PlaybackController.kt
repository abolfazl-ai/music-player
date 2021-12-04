package com.example.compose.ui.composables.player_screen

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.R
import com.example.compose.utils.resources.PlayerScreenSpacing
import com.example.compose.utils.resources.ProgressBarHeight
import com.example.compose.utils.resources.TAG
import com.example.compose.utils.util_classes.PlaybackAction.*
import com.example.compose.viewmodel.MainViewModel

@ExperimentalMaterialApi
@ExperimentalAnimationGraphicsApi
@Composable
fun PlaybackController(
    alpha: () -> Float,
    contentColor: Color,
) = CompositionLocalProvider(
    LocalContentColor provides animateColorAsState(contentColor, tween(1000)).value,
    LocalContentAlpha provides 1f
) {
    Column(Modifier.alpha(alpha()), horizontalAlignment = Alignment.CenterHorizontally) {

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(ProgressBarHeight)
                .background(LocalContentColor.current)
        )

        Row(
            Modifier.padding(top = PlayerScreenSpacing),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconButton(onClick = { }) {
                androidx.compose.material3.Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Rounded.Repeat,
                    contentDescription = null,
                )
            }
            IconButton(onClick = { }) {
                androidx.compose.material3.Icon(
                    modifier = Modifier.size(44.dp),
                    imageVector = Icons.Rounded.ChevronLeft,
                    contentDescription = null,
                )
            }
            Spacer(modifier = Modifier.size(56.dp))
            IconButton(onClick = { }) {
                androidx.compose.material3.Icon(
                    modifier = Modifier.size(44.dp),
                    imageVector = Icons.Rounded.ChevronRight,
                    contentDescription = null,
                )
            }
            IconButton(onClick = { }) {
                androidx.compose.material3.Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Rounded.Shuffle,
                    contentDescription = null,
                )
            }
        }
    }
}
