package com.example.compose.utils.util_classes

import androidx.annotation.FloatRange

enum class PlaybackAction { PLAY, PAUSE, NEXT, PREVIOUS, STOP, SHUFFLE, REPEAT }

sealed class FabState(@FloatRange(from = 0.0, to = 1.0) val progress: Float) {
    object Menu : FabState(0f)
    class MenuToPlay(progress: Float) : FabState(progress)
    object Play : FabState(1f)
    class PlayToQueue(progress: Float) : FabState(progress)
    object Queue : FabState(0f)
}