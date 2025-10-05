package com.example.releaseflow.core.design.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.releaseflow.core.design.animation.smoothScale

/**
 * Glass morphism card component
 * Beautiful frosted glass effect card with smooth interactions
 * 
 * @param modifier Modifier to be applied to the card
 * @param onClick Optional click handler
 * @param style Glass style variant
 * @param shape Shape of the card
 * @param shadowElevation Shadow elevation
 * @param contentPadding Padding for the content
 * @param enabled Whether the card is enabled for interaction
 * @param content Content of the card
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    style: GlassStyle = GlassStyle.MEDIUM,
    shape: Shape = RoundedCornerShape(20.dp),
    shadowElevation: Dp = 8.dp,
    contentPadding: Dp = 16.dp,
    enabled: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    val clickableModifier = if (onClick != null && enabled) {
        Modifier
            .clickable(
                enabled = enabled,
                role = Role.Button,
                onClick = onClick
            )
            .smoothScale(enabled = enabled)
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .then(clickableModifier)
            .glassmorphism(
                style = style,
                shadowElevation = shadowElevation,
                shape = shape
            )
    ) {
        Column(
            modifier = Modifier.padding(contentPadding),
            content = content
        )
    }
}

/**
 * Glass card with custom color
 */
@Composable
fun GlassCard(
    color: Color,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    style: GlassStyle = GlassStyle.MEDIUM,
    shape: Shape = RoundedCornerShape(20.dp),
    shadowElevation: Dp = 8.dp,
    contentPadding: Dp = 16.dp,
    enabled: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    val clickableModifier = if (onClick != null && enabled) {
        Modifier
            .clickable(
                enabled = enabled,
                role = Role.Button,
                onClick = onClick
            )
            .smoothScale(enabled = enabled)
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .then(clickableModifier)
            .glassmorphism(
                style = style,
                color = color,
                shadowElevation = shadowElevation,
                shape = shape
            )
    ) {
        Column(
            modifier = Modifier.padding(contentPadding),
            content = content
        )
    }
}
