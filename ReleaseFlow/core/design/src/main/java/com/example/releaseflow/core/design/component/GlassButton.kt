package com.example.releaseflow.core.design.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.releaseflow.core.design.animation.smoothScale

/**
 * Glass morphism button component
 * Beautiful frosted glass button with smooth press animation
 * 
 * @param onClick Click handler
 * @param modifier Modifier to be applied to the button
 * @param enabled Whether the button is enabled
 * @param style Glass style variant
 * @param shape Shape of the button
 * @param contentPadding Padding for the content
 * @param icon Optional leading icon
 * @param content Content of the button (usually text)
 */
@Composable
fun GlassButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    style: GlassStyle = GlassStyle.MEDIUM,
    shape: Shape = RoundedCornerShape(16.dp),
    contentPadding: PaddingValues = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
    icon: ImageVector? = null,
    content: @Composable RowScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .clickable(
                enabled = enabled,
                role = Role.Button,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .smoothScale(enabled = enabled)
            .glassmorphism(
                style = style,
                shape = shape,
                shadowElevation = if (enabled) 4.dp else 2.dp
            )
            .padding(contentPadding),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            content = {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = if (enabled) LocalContentColor.current else LocalContentColor.current.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                content()
            }
        )
    }
}

/**
 * Glass button with text
 */
@Composable
fun GlassButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    style: GlassStyle = GlassStyle.MEDIUM,
    shape: Shape = RoundedCornerShape(16.dp),
    icon: ImageVector? = null,
    textStyle: TextStyle = MaterialTheme.typography.labelLarge
) {
    GlassButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        style = style,
        shape = shape,
        icon = icon
    ) {
        Text(
            text = text,
            style = textStyle,
            color = if (enabled) LocalContentColor.current else LocalContentColor.current.copy(alpha = 0.5f)
        )
    }
}

/**
 * Primary glass button with color
 */
@Composable
fun PrimaryGlassButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    
    Box(
        modifier = modifier
            .clickable(
                enabled = enabled,
                role = Role.Button,
                onClick = onClick
            )
            .smoothScale(enabled = enabled)
            .glassmorphism(
                style = GlassStyle.MEDIUM,
                color = primaryColor.copy(alpha = 0.8f),
                shape = RoundedCornerShape(16.dp),
                shadowElevation = if (enabled) 8.dp else 4.dp
            )
            .padding(horizontal = 24.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                color = Color.White
            )
        }
    }
}
