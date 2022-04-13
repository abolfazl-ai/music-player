package com.example.compose.ui.composables.library_screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.compose.ui.composables.fab.DragHandler

@Composable
fun HomeLibrary() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        var offsetX by remember { mutableStateOf(0f) }
        var offsetY by remember { mutableStateOf(0f) }


        DragHandler(onClick = { /*TODO*/ }, elevation = 10.dp, size = 56.dp, color = Color.Blue, contentColor = Color.White) {

        }

/*        Spacer(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .background(Color.Blue)
                .size(100.dp)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consumeAllChanges()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    }
                }
        )*/
    }
}