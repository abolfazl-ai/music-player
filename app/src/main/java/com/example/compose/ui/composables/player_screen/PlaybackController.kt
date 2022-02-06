package com.example.compose.ui.composables.player_screen

import android.util.Log
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.compose.utils.kotlin_extensions.compIn
import com.example.compose.utils.resources.TAG
import com.example.compose.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    onRepeat: () -> Unit = {}, onPrevious: () -> Unit = {},
    onNext: () -> Unit = {}, onShuffle: () -> Unit = {},
    viewModel: MainViewModel = hiltViewModel()
) {

    Log.e(TAG, "PlaybackController")

    val sheetProgress by viewModel.stageSheetProgress.collectAsState()
    val scope = rememberCoroutineScope()
    var active by remember { mutableStateOf(true) }

    Row(Modifier.alpha(sheetProgress.compIn(0.75f, 0.9f)), verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onRepeat) {
            Icon(
                modifier = Modifier
                    .size(56.dp)
                    .padding(16.dp),
                imageVector = Icons.Rounded.Repeat,
                contentDescription = null,
            )
        }
        IconButton(onClick = {
            if (active) scope.launch {
                active = false
                onPrevious()
                delay(400)
                active = true
            }
        }) {
            Icon(
                modifier = Modifier
                    .size(56.dp)
                    .padding(12.dp),
                imageVector = Icons.Rounded.ChevronLeft,
                contentDescription = null,
            )
        }
        Spacer(modifier = Modifier.size(60.dp))
        IconButton(onClick = {
            if (active) scope.launch {
                active = false
                onNext()
                delay(400)
                active = true
            }
        }) {
            Icon(
                modifier = Modifier
                    .size(56.dp)
                    .padding(12.dp),
                imageVector = Icons.Rounded.ChevronRight,
                contentDescription = null,
            )
        }
        IconButton(onClick = onShuffle) {
            Icon(
                modifier = Modifier
                    .size(56.dp)
                    .padding(16.dp),
                imageVector = Icons.Rounded.Shuffle,
                contentDescription = null,
            )
        }
    }
}