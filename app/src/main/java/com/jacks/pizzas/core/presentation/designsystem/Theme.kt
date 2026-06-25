package com.jacks.pizzas.core.presentation.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Blue,
    background = Contemplation,
    surface = Color.White,
    onSurface = Color.Black,
    onSurfaceVariant = Color.Black.copy(0.7f)
)

@Composable
fun PizzasTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}