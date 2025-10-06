package com.managr.app.core.design.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Glass morphism style variants
 */
enum class GlassStyle {
    LIGHT,      // Light frosted glass
    MEDIUM,     // Medium frosted glass
    HEAVY,      // Heavy frosted glass
    SUBTLE      // Subtle glass effect
}

/**
 * Glass morphism modifier for creating frosted glass effect
 * 
 * @param style Glass style variant
 * @param color Optional custom color, defaults to surface with transparency
 * @param shadowElevation Elevation for shadow effect
 * @param shape Shape of the glass surface
 * @param borderWidth Width of the glass border
 */
fun Modifier.glassmorphism(
    style: GlassStyle = GlassStyle.MEDIUM,
    color: Color? = null,
    shadowElevation: Dp = 8.dp,
    shape: Shape = RoundedCornerShape(20.dp),
    borderWidth: Dp = 1.dp
): Modifier = composed {
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurface = MaterialTheme.colorScheme.onSurface
    
    val (alpha, blurRadius, borderAlpha) = when (style) {
        GlassStyle.LIGHT -> Triple(0.5f, 8.dp, 0.1f)
        GlassStyle.MEDIUM -> Triple(0.7f, 12.dp, 0.15f)
        GlassStyle.HEAVY -> Triple(0.85f, 16.dp, 0.2f)
        GlassStyle.SUBTLE -> Triple(0.3f, 4.dp, 0.05f)
    }
    
    val glassColor = color ?: surfaceColor.copy(alpha = alpha)
    val borderColor = onSurface.copy(alpha = borderAlpha)
    
    this
        .shadow(
            elevation = shadowElevation,
            shape = shape,
            ambientColor = Color.Black.copy(alpha = 0.1f),
            spotColor = Color.Black.copy(alpha = 0.1f)
        )
        .background(
            color = glassColor,
            shape = shape
        )
        .border(
            width = borderWidth,
            color = borderColor,
            shape = shape
        )
        .clip(shape)
}

/**
 * Glass morphism with gradient background
 */
fun Modifier.glassmorphismGradient(
    gradient: Brush,
    style: GlassStyle = GlassStyle.MEDIUM,
    shadowElevation: Dp = 8.dp,
    shape: Shape = RoundedCornerShape(20.dp),
    borderWidth: Dp = 1.dp
): Modifier = composed {
    val onSurface = MaterialTheme.colorScheme.onSurface
    
    val (alpha, blurRadius, borderAlpha) = when (style) {
        GlassStyle.LIGHT -> Triple(0.5f, 8.dp, 0.1f)
        GlassStyle.MEDIUM -> Triple(0.7f, 12.dp, 0.15f)
        GlassStyle.HEAVY -> Triple(0.85f, 16.dp, 0.2f)
        GlassStyle.SUBTLE -> Triple(0.3f, 4.dp, 0.05f)
    }
    
    val borderColor = onSurface.copy(alpha = borderAlpha)
    
    this
        .shadow(
            elevation = shadowElevation,
            shape = shape,
            ambientColor = Color.Black.copy(alpha = 0.1f),
            spotColor = Color.Black.copy(alpha = 0.1f)
        )
        .background(
            brush = gradient,
            shape = shape,
            alpha = alpha
        )
        .border(
            width = borderWidth,
            color = borderColor,
            shape = shape
        )
        .clip(shape)
}

