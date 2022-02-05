package com.example.compose.ui.composables.util_composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color

@Composable
fun Shadow(modifier: Modifier = Modifier, alpha: Float,color: Color= Color.Black) = Spacer(
    modifier
        .fillMaxSize()
        .alpha(alpha)
        .background(color)
)