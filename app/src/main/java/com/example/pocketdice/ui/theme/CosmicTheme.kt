package com.example.pocketdice.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CosmicColorScheme = darkColorScheme(
    primary = Color(0xFF1E1E3F),
    secondary = Color(0xFF7B61FF),
    tertiary = Color(0xFF3EBFA3),
    background = Color(0xFF0F0F1A),
    surface = Color(0xFF1A1A2E),
    onPrimary = Color(0xFFDADADA),
    onSecondary = Color.Black,
    onBackground = Color(0xFFDADADA),
    onSurface = Color(0xFFDADADA)
)

@Composable
fun CosmicTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CosmicColorScheme,
        typography = Typography(), // You can customize this later
        content = content
    )
}
