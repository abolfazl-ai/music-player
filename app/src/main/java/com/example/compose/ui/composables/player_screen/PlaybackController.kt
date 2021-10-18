package com.example.compose.ui.composables.player_screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.R
import com.example.compose.utils.util_classes.PlaybackAction.*
import com.example.compose.viewmodel.MainViewModel

@ExperimentalMaterialApi
@ExperimentalAnimationGraphicsApi
@Composable
fun PlaybackController(
    modifier: Modifier,
    contentColor: Color,
    viewModel: MainViewModel = viewModel()
) = Column(
    modifier
        .fillMaxWidth()
        .height(116.dp),
    horizontalAlignment = Alignment.CenterHorizontally
) {

    val color = animateColorAsState(targetValue = contentColor)

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
            .background(color.value.copy(alpha = 0.2f))
    )

    Box(Modifier.padding(0.dp, 8.dp), contentAlignment = Alignment.Center) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .clickable { }
                    .padding(28.dp),
                imageVector = Icons.Outlined.Favorite,
                contentDescription = null,
                tint = color.value
            )
            Spacer(modifier = Modifier.width(160.dp))
            Icon(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .clickable { viewModel.serviceController.playbackController(SHUFFLE) }
                    .padding(28.dp),
                imageVector = Icons.Outlined.Star,
                contentDescription = null,
                tint = color.value
            )
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .clickable { viewModel.serviceController.playbackController(PREVIOUS) }
                    .padding(16.dp),
                imageVector = Icons.Outlined.KeyboardArrowLeft,
                contentDescription = null,
                tint = color.value
            )
            Spacer(modifier = Modifier.width(48.dp))
            Icon(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .clickable { viewModel.serviceController.playbackController(NEXT) }
                    .padding(16.dp),
                imageVector = Icons.Outlined.KeyboardArrowRight,
                contentDescription = null,
                tint = color.value
            )
        }

        val v = animatedVectorResource(R.drawable.play_to_pause)
        var atEnd by remember { mutableStateOf(false) }

        FloatingActionButton(
            modifier = Modifier
                .size(56.dp),
            onClick = { viewModel.serviceController.playbackController(PLAY); atEnd = !atEnd }
        ) {
            Icon(
                painter = v.painterFor(atEnd = atEnd),
                contentDescription = null,
                tint = Color(0xFF424242)
            )
        }

    }
}