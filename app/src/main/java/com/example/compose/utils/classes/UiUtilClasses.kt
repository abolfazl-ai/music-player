package com.example.compose.utils.classes

import androidx.compose.ui.graphics.Color

data class MainColors(val back: Color, val front: Color)

enum class PlaybackAction { PLAY, PAUSE, NEXT, PREVIOUS, STOP, SHUFFLE, REPEAT }