package com.example.releaseflow.personal.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF6366F1),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF4F46E5),
    secondary = Color(0xFFEC4899),
    secondaryContainer = Color(0xFFDB2777),
    background = Color(0xFF0F172A),
    surface = Color(0xFF1E293B),
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF334155),
    error = Color(0xFFEF4444),
    outline = Color(0xFF475569)
)

@Immutable
data class RFShapesTokens(
    val smallRadius: Float = 8f,
    val mediumRadius: Float = 12f,
    val largeRadius: Float = 16f,
)

val LocalRFShapesTokens = staticCompositionLocalOf { RFShapesTokens() }

@Composable
fun ReleaseFlowTheme(
    reduceMotion: Boolean = false,
    content: @Composable () -> Unit
) {
    val shapeTokens = RFShapesTokens()
    val shapes = Shapes(
        small = androidx.compose.foundation.shape.RoundedCornerShape(shapeTokens.smallRadius.dp),
        medium = androidx.compose.foundation.shape.RoundedCornerShape(shapeTokens.mediumRadius.dp),
        large = androidx.compose.foundation.shape.RoundedCornerShape(shapeTokens.largeRadius.dp),
    )

    RFMotionProviderWithReducedMotion(reduceMotion = reduceMotion) {
        CompositionLocalProvider(LocalRFShapesTokens provides shapeTokens) {
            MaterialTheme(
                colorScheme = DarkColorScheme,
                shapes = shapes,
                typography = MaterialTheme.typography,
                content = content
            )
        }
    }
}