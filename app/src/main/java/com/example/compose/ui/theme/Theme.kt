package com.example.compose.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = DarkPrimaryColor,
    primaryVariant = Blue500,
    secondary = Color.Black,
    onBackground = Color.White,
    surface = DarkerGray,
    onSurface = Color.White,
    background = Color.Black
)

private val LightColorPalette = lightColors(
    primary = PrimaryColor,
    onPrimary = Color.White,
    primaryVariant = PrimaryColor,
    secondary = PrimaryColor,
    onBackground = LightGray,
    surface = Color.White,
    onSurface = LightGray,
    background = BcLight

    /* Other default colors to override
background = Color.White,
surface = Color.White,
onPrimary = Color.White,
onSecondary = Color.Black,
onBackground = Color.Black,
onSurface = Color.Black,
*/
)

@Composable
fun ComposeTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}