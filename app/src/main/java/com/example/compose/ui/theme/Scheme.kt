package com.example.compose.ui.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val lightMinimalScheme = lightColorScheme(
    primary = Color(0xFFFDFBFF),
    onPrimary = Color(0xFF2A3042),
    primaryContainer = Color(0xFFFFFFFF),
    onPrimaryContainer = Color(0xFF2E2E2E),
    secondary = Color(0xFF0061E9),
    onSecondary = Color(0xFFFFFFFF),
    tertiary = Color(0xFF2E3038),
    onTertiary = Color(0xFFFFFFFF),
    background = Color(0xffE4EBF5),
    onBackground = Color(0xFF2A3042),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF404658),
    surfaceVariant = Color(0xFFF2F0F5),
    onSurfaceVariant = Color(0xFF2A3042),
)

val darkMinimalScheme = lightColorScheme(
    primary = Color(0xFF2A3042),
    onPrimary = Color(0xFFF2F0F5),
    primaryContainer = Color(0xFF151B2C),
    onPrimaryContainer = Color(0xFFEDF0FF),
    secondary = Color(0xFFB1C5FF),
    onSecondary = Color(0xFF002A77),
    tertiary = Color(0xFF2E3038),
    onTertiary = Color(0xFFFFFFFF),
    background = Color(0xFF000000),
    onBackground = Color(0xFFFFFFFF),
    surface = Color(0xFF191B23),
    onSurface = Color(0xFFF0F0FB),
    surfaceVariant = Color(0xFF2E3038),
    onSurfaceVariant = Color(0xFFF0F0FB),
)