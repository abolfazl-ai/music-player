package com.example.compose.ui.composables.library_screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun HomeLibrary() {

    Column() {
        Button(modifier = Modifier.clip(CircleShape).height(100.dp).width(100.dp), onClick = { /*TODO*/ }) {
            Text(text = "Hadi")
        }
        Button(modifier = Modifier.height(50.dp).width(150.dp), onClick = { /*TODO*/ }) {
            Text(text = "Abolfazl")
        }
    }
}