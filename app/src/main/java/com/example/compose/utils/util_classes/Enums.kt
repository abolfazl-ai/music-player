package com.example.compose.utils.util_classes

import androidx.annotation.FloatRange

enum class PlaybackAction { PLAY, PAUSE, NEXT, PREVIOUS, STOP, SHUFFLE, REPEAT }

sealed class FabState(@FloatRange(from = 0.0, to = 1.0) val progress: Float) {
    object Menu : FabState(0f)
    class MenuToPlayBack(progress: Float) : FabState(progress)
    object PlayBack : FabState(1f)
}