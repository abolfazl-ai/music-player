package com.example.compose.ui.composables.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.FloatingActionButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.compose.ui.composables.player_screen.ColorReveal
import com.example.compose.ui.theme.*

@Composable
fun HomeScreen() {
    var color by remember { mutableStateOf(BcLight) }
    ColorReveal(modifier = Modifier.fillMaxSize(), color = color, y = 270.dp) {
        FloatingActionButton(modifier = Modifier.align(Alignment.Center),
            onClick = { color = colors1.random() }) {}
    }
}


val colors1 = listOf(
    Blue700,
    Red700,
    Green700,
    Purple700,
    Yellow700,
)