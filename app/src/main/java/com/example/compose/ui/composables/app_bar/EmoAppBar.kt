package com.example.compose.ui.composables.app_bar

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.ModeNight
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.compose.ui.composables.icons.E
import com.example.compose.ui.composables.util_composables.EmoIconButton
import com.example.compose.utils.resources.AppBarElevation
import com.example.compose.utils.resources.AppBarHeight

@Composable
fun EmoAppBar(modifier: Modifier = Modifier) = Surface(
    shadowElevation = AppBarElevation,
    color = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(AppBarHeight)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        EmoIconButton(rippleRadius = 24.dp, onClick = { /*TODO*/ }) {
            Icon(
                modifier = Modifier
                    .size(48.dp)
                    .padding(12.dp),
                imageVector = Icons.Rounded.E,
                contentDescription = "Open Drawer"
            )
        }

        Text(
            modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
            text = "Abolfazl is Awesome",
            style = MaterialTheme.typography.titleLarge
        )

        EmoIconButton(rippleRadius = 24.dp, onClick = { /*TODO*/ }) {
            Icon(
                modifier = Modifier
                    .size(48.dp)
                    .padding(12.dp),
                imageVector = Icons.Rounded.Search,
                contentDescription = "Open Drawer"
            )
        }

        EmoIconButton(rippleRadius = 24.dp, onClick = { /*TODO*/ }) {
            Icon(
                modifier = Modifier
                    .size(48.dp)
                    .padding(12.dp),
                imageVector = Icons.Rounded.ModeNight,
                contentDescription = "Open Drawer"
            )
        }
    }
}