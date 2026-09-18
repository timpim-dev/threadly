package com.threadly.felixx.dev.ui

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.threadly.felixx.dev.data.AppSettingsEntity

@Composable
fun ThreadlyTheme(
    settings: AppSettingsEntity?,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val isSystemDark = isSystemInDarkTheme()
    
    val useDarkTheme = when (settings?.theme) {
        "light" -> false
        "dark" -> true
        else -> isSystemDark
    }

    val colorScheme = if (settings?.dynamicColors != false && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (useDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    } else {
        val seed = Color(settings?.seedColor ?: 0xFF465D91)
        if (useDarkTheme) darkColorScheme(primary = seed) else lightColorScheme(primary = seed)
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !useDarkTheme
        }
    }

    val typography = Typography() // Can expand later

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}
