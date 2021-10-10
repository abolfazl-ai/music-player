package com.example.compose.ui.composables.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.FloatingActionButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.compose.ui.composables.player_screen.reveal
import com.example.compose.ui.theme.*

@Composable
fun HomeScreen() {
    var color by remember { mutableStateOf(BcLight) }
    Box(modifier = Modifier
        .fillMaxSize()
        .reveal(color = color, y = 270.dp)) {
        FloatingActionButton(modifier = Modifier.align(Alignment.Center),
            onClick = { color = colors1.random() }) {}
    }
}


val colors1 = listOf(
    C01,
    C02,
    C03,
    C04,
    C05,
    C06,
    C07,
    C08,
    C09,
    C10,
    C11,
    C12,
    C13,
    C14,
    C15,
    C16
)