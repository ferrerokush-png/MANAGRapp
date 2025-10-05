package com.example.releaseflow.core.design.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Light color scheme with music industry vibes
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6366F1), // Indigo
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0E7FF),
    onPrimaryContainer = Color(0xFF1E1B4B),
    
    secondary = Color(0xFF8B5CF6), // Purple
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF3E8FF),
    onSecondaryContainer = Color(0xFF4C1D95),
    
    tertiary = Color(0xFFEC4899), // Pink
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFCE7F3),
    onTertiaryContainer = Color(0xFF831843),
    
    background = Color(0xFFFAFAFA),
    onBackground = Color(0xFF1A1A1A),
    
    surface = Color.White,
    onSurface = Color(0xFF1A1A1A),
    surfaceVariant = Color(0xFFF5F5F5),
    onSurfaceVariant = Color(0xFF666666),
    surfaceTint = Color(0xFF6366F1),
    
    inverseSurface = Color(0xFF2D2D2D),
    inverseOnSurface = Color(0xFFF5F5F5),
    inversePrimary = Color(0xFF818CF8),
    
    error = Color(0xFFEF4444),
    onError = Color.White,
    errorContainer = Color(0xFFFEE2E2),
    onErrorContainer = Color(0xFF7F1D1D),
    
    outline = Color(0xFFD1D5DB),
    outlineVariant = Color(0xFFE5E7EB),
    scrim = Color.Black.copy(alpha = 0.32f)
)

// Dark color scheme with music industry vibes
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF818CF8), // Light Indigo
    onPrimary = Color(0xFF1E1B4B),
    primaryContainer = Color(0xFF3730A3),
    onPrimaryContainer = Color(0xFFE0E7FF),
    
    secondary = Color(0xFFA78BFA), // Light Purple
    onSecondary = Color(0xFF4C1D95),
    secondaryContainer = Color(0xFF6D28D9),
    onSecondaryContainer = Color(0xFFF3E8FF),
    
    tertiary = Color(0xFFF472B6), // Light Pink
    onTertiary = Color(0xFF831843),
    tertiaryContainer = Color(0xFF9F1239),
    onTertiaryContainer = Color(0xFFFCE7F3),
    
    background = Color(0xFF0F0F0F),
    onBackground = Color(0xFFE5E5E5),
    
    surface = Color(0xFF1A1A1A),
    onSurface = Color(0xFFE5E5E5),
    surfaceVariant = Color(0xFF2A2A2A),
    onSurfaceVariant = Color(0xFFB0B0B0),
    surfaceTint = Color(0xFF818CF8),
    
    inverseSurface = Color(0xFFE5E5E5),
    inverseOnSurface = Color(0xFF1A1A1A),
    inversePrimary = Color(0xFF6366F1),
    
    error = Color(0xFFF87171),
    onError = Color(0xFF7F1D1D),
    errorContainer = Color(0xFF991B1B),
    onErrorContainer = Color(0xFFFEE2E2),
    
    outline = Color(0xFF4B5563),
    outlineVariant = Color(0xFF374151),
    scrim = Color.Black.copy(alpha = 0.5f)
)

/**
 * Local composition for reduced motion preference
 */
val LocalReducedMotion = staticCompositionLocalOf { false }

/**
 * Release Flow theme with Material 3 design system
 * Supports light/dark mode, dynamic colors (Android 12+), and glass morphism effects
 * 
 * @param darkTheme Whether to use dark theme
 * @param dynamicColor Whether to use dynamic colors from system (Android 12+)
 * @param reducedMotion Whether to reduce animations for accessibility
 * @param content The composable content
 */
@Composable
fun ReleaseFlowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    reducedMotion: Boolean = false,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    
    val colorScheme = when {
        // Dynamic colors available on Android 12+
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        // Fallback to custom color schemes
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    CompositionLocalProvider(
        LocalReducedMotion provides reducedMotion
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}

