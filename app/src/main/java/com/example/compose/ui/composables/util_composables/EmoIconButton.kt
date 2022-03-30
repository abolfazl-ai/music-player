package com.example.compose.ui.composables.util_composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun EmoIconButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
    rippleRadius: Dp = 40.dp,
    content: @Composable BoxScope.() -> Unit
) = Box(
    modifier
        .clickable(
            onClick = onClick,
            enabled = enabled,
            role = Role.Button,
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(bounded = false, radius = rippleRadius)
        ).alpha(if (enabled) 1f else 0.8f),
    contentAlignment = Alignment.Center,
    content = content
)