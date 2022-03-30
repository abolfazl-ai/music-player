package com.example.compose.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun AppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {

    val colors = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (!useDarkTheme) lightSchemeGenerator(dynamicTonalPalette(LocalContext.current))
        else darkSchemeGenerator(dynamicTonalPalette(LocalContext.current))
    } else {
        if (!useDarkTheme) lightMinimalScheme
        else darkMinimalScheme
    }

    androidx.compose.material.MaterialTheme() {
        MaterialTheme(
            colorScheme = colors,
            typography = AppTypography,
            content = content
        )
    }
}

fun lightSchemeGenerator(palette: TonalPalette): ColorScheme = lightColorScheme(
    primary = palette.primary40,
    onPrimary = palette.primary100,
    primaryContainer = palette.primary80,
    onPrimaryContainer = palette.primary10,
    inversePrimary = palette.primary80,
    secondary = palette.primary50,
    onSecondary = palette.primary100,
    secondaryContainer = palette.secondary90,
    onSecondaryContainer = palette.secondary10,
    tertiary = palette.secondary20,
    onTertiary = palette.secondary99,
    background = palette.primary95,
    onBackground = palette.primary10,
    surface = palette.neutral100,
    onSurface = palette.neutral10,
    surfaceVariant = palette.neutral95,
    onSurfaceVariant = palette.neutralVariant30,
    inverseSurface = palette.neutral20,
    inverseOnSurface = palette.neutral95,
    outline = palette.neutralVariant50,
)

fun darkSchemeGenerator(palette: TonalPalette): ColorScheme = darkColorScheme(
    primary = palette.primary20,
    onPrimary = palette.primary95,
    primaryContainer = palette.secondary10,
    onPrimaryContainer = palette.secondary90,
    secondary = palette.primary80,
    onSecondary = palette.primary20,
    tertiary = palette.secondary20,
    onTertiary = palette.secondary99,
    background = palette.neutral0,
    onBackground = palette.neutral90,
    surface = palette.neutral10,
    onSurface = palette.neutral80,
    surfaceVariant = palette.neutralVariant30,
    onSurfaceVariant = palette.neutralVariant80,
)