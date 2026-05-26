package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = IndigoPrimary,
    secondary = IndigoAccent,
    background = PureBlack,
    surface = Color(0xFF111115),
    onPrimary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
  )

private val LightColorScheme =
  darkColorScheme( // Forces dark theme layout for premium aesthetic under all settings
    primary = IndigoPrimary,
    secondary = IndigoAccent,
    background = PureBlack,
    surface = Color(0xFF111115),
    onPrimary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true, // Force dark mode as specified by prompt
  dynamicColor: Boolean = false, // Disable dynamic content wallpaper match to keep pristine contrast
  content: @Composable () -> Unit,
) {
  val colorScheme = DarkColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
