package com.example.compose.ui.composables.fab

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.R
import com.example.compose.ui.composables.fab.EmoFabMode.*
import com.example.compose.utils.kotlin_extensions.getMidColor
import com.example.compose.utils.resources.FabElevation
import com.example.compose.utils.resources.FabSize
import com.example.compose.utils.resources.MenuFabSize
import com.example.compose.utils.resources.MenuFabSpacing
import com.example.compose.viewmodel.MainViewModel
import kotlinx.coroutines.launch

sealed class EmoFabMode(val progress: Float) {
    object Playback : EmoFabMode(1f)
    class Menu2Playback(progress: Float) : EmoFabMode(progress)
    object Menu : EmoFabMode(0f)
    class Menu2Play(progress: Float) : EmoFabMode(progress)
    object Play : EmoFabMode(1f)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EmoFab(viewModel: MainViewModel = viewModel()) {

    val state by viewModel.fabState.collectAsState()
    val scope = rememberCoroutineScope()
    val color = MaterialTheme.colorScheme.run { getColor(secondary, onSecondary, surface, onSurface, state.fabMode) }

    EmoFab(
        draggable = state.fabMode is Menu && !state.isMenuOpen,
        onDrag = { scope.launch { viewModel.setFabState(isDragging = it) } },
        showMenu = state.fabMode is Menu,
        isMenuOpen = state.isMenuOpen, menuItems = DummyMenuItems, onMenuClicked = {},
        color = color.first, contentColor = color.second,
        onClick = {
            scope.launch {
                when (state.fabMode) {
                    is Menu -> viewModel.setFabState(isMenuOpen = !state.isMenuOpen)
                    is Playback -> viewModel.preferences.updatePlayingState(!state.isPlaying)
                    is Play -> Unit
                }
            }
        }
    ) { state.run { GetIcon(fabMode, isPlaying, isMenuOpen) } }
}

internal fun getColor(mColor: Color, mOnColor: Color, pColor: Color, pOnColor: Color, mode: EmoFabMode) = when (mode) {
    is Playback -> pColor to pOnColor
    is Menu2Playback -> mode.progress.getMidColor(mColor, pColor) to mode.progress.getMidColor(mOnColor, pOnColor)
    else -> mColor to mOnColor
}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
internal fun GetIcon(mode: EmoFabMode, isPlaying: Boolean, isMenuOpen: Boolean) {

    if (mode is Playback || mode is Menu2Playback) {
        val a = AnimatedImageVector.animatedVectorResource(R.drawable.play_to_pause)
        Icon(
            modifier = Modifier
                .alpha(2 * (mode.progress.coerceIn(0.5f, 1f) - 0.5f))
                .padding(16.dp),
            painter = rememberAnimatedVectorPainter(a, isPlaying), contentDescription = "PlayButton",
        )
    }

    if (mode is Menu || mode is Menu2Playback)
        Icon(
            modifier = Modifier
                .alpha(1 - 2 * mode.progress.coerceIn(0f, 0.5f))
                .padding(12.dp)
                .rotate(animateFloatAsState(if (isMenuOpen) 135f else 0f, spring(0.5f)).value),
            imageVector = Icons.Rounded.Add, contentDescription = "Fab"
        )
}

@Composable
internal fun EmoFab(
    draggable: Boolean, showMenu: Boolean, isMenuOpen: Boolean, onDrag: (isDragging: Boolean) -> Unit,
    color: Color, contentColor: Color, size: Dp = FabSize, shape: Shape = CircleShape, elevation: Dp = FabElevation,
    menuItems: List<MenuItem>, onMenuClicked: (id: Int) -> Unit, menuSize: Dp = MenuFabSize, menuSpacing: Dp = MenuFabSpacing,
    onClick: () -> Unit, icon: @Composable () -> Unit
) = Box {
    if (showMenu) MenuHandler(isMenuOpen, menuItems, onMenuClicked, shape, color, contentColor, menuSize, menuSpacing)
    DragHandler(draggable, onClick, elevation, shape, size, color, contentColor, onDrag, icon)
}