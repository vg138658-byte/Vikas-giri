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
    primary = SleekBlue500,
    onPrimary = Slate900,
    primaryContainer = SleekBlue900,
    onPrimaryContainer = SleekBlue100,
    secondary = SleekBlue600,
    onSecondary = Slate900,
    secondaryContainer = Slate800,
    onSecondaryContainer = SleekBlue50,
    tertiary = SleekIndigo800,
    onTertiary = Color.White,
    background = Slate900,
    onBackground = Slate50,
    surface = Slate800,
    onSurface = Slate50,
    surfaceVariant = Slate700,
    onSurfaceVariant = Slate200,
    outline = Slate600,
  )

private val LightColorScheme =
  lightColorScheme(
    primary = SleekBlue700,
    onPrimary = Color.White,
    primaryContainer = SleekBlue100,
    onPrimaryContainer = SleekBlue900,
    secondary = SleekBlue600,
    onSecondary = Color.White,
    secondaryContainer = SleekBlue50,
    onSecondaryContainer = SleekBlue900,
    tertiary = SleekIndigo900,
    onTertiary = Color.White,
    background = SleekBg,
    onBackground = Slate900,
    surface = Color.White,
    onSurface = Slate900,
    surfaceVariant = Slate100,
    onSurfaceVariant = Slate700,
    outline = Slate200,
  )

@Composable
fun UmeshEnterpriseTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Use our brand identity colors by default
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

// Alias to maintain compatibility with existing tests
@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) = UmeshEnterpriseTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
