package com.example.organizadorestudo.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Paleta de cores para o modo escuro
private val DarkColorScheme = darkColorScheme(
    primary = BluePrimary,
    secondary = PurpleGrey80,
    background = Color(0xFF1C1B1F),
    surface = Color(0xFF2C2A31),
    onPrimary = WhiteSurface,
    onBackground = Color(0xFFE6E1E5),
    onSurface = Color(0xFFE6E1E5),
    surfaceVariant = GreenComplete,
    primaryContainer = BluePrimary
)

// Paleta de cores para o modo claro
private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    secondary = PurpleGrey40,
    background = GreyBackground,
    surface = WhiteSurface,
    onPrimary = WhiteSurface,
    onBackground = DarkBlueText,
    onSurface = DarkBlueText,
    surfaceVariant = GreenComplete,
    primaryContainer = BluePrimary
)

@Composable
fun OrganizadorEstudoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme // Corrigido para modo claro/escuro
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Agora deve resolver
        content = content
    )
}
