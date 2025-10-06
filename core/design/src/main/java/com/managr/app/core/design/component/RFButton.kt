package com.managr.app.core.design.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.managr.app.core.design.animation.smoothScale

/**
 * MANAGR button variants
 */
enum class RFButtonStyle {
    PRIMARY,    // Filled button with primary color
    SECONDARY,  // Outlined button
    TEXT        // Text-only button
}

/**
 * MANAGR Button - Primary UI button component
 *
 * @param text Button text
 * @param onClick Click handler
 * @param modifier Modifier
 * @param style Button style variant
 * @param enabled Whether button is enabled
 * @param icon Optional leading icon
 * @param loading Whether button is in loading state
 */
@Composable
fun RFButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: RFButtonStyle = RFButtonStyle.PRIMARY,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    loading: Boolean = false
) {
    when (style) {
        RFButtonStyle.PRIMARY -> {
            Button(
                onClick = onClick,
                modifier = modifier
                    .smoothScale(enabled = enabled && !loading)
                    .heightIn(min = 48.dp),
                enabled = enabled && !loading,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                ButtonContent(text, icon, loading)
            }
        }
        
        RFButtonStyle.SECONDARY -> {
            OutlinedButton(
                onClick = onClick,
                modifier = modifier
                    .smoothScale(enabled = enabled && !loading)
                    .heightIn(min = 48.dp),
                enabled = enabled && !loading,
                shape = RoundedCornerShape(16.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    width = 2.dp
                )
            ) {
                ButtonContent(text, icon, loading)
            }
        }
        
        RFButtonStyle.TEXT -> {
            TextButton(
                onClick = onClick,
                modifier = modifier
                    .smoothScale(enabled = enabled && !loading)
                    .heightIn(min = 48.dp),
                enabled = enabled && !loading,
                shape = RoundedCornerShape(16.dp)
            ) {
                ButtonContent(text, icon, loading)
            }
        }
    }
}

@Composable
private fun RowScope.ButtonContent(
    text: String,
    icon: ImageVector?,
    loading: Boolean
) {
    if (loading) {
        CircularProgressIndicator(
            modifier = Modifier.size(20.dp),
            strokeWidth = 2.dp
        )
        Spacer(modifier = Modifier.width(8.dp))
    } else if (icon != null) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
    }
    
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge
    )
}

/**
 * Primary button shorthand
 */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    loading: Boolean = false
) {
    RFButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        style = RFButtonStyle.PRIMARY,
        enabled = enabled,
        icon = icon,
        loading = loading
    )
}

/**
 * Secondary button shorthand
 */
@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    loading: Boolean = false
) {
    RFButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        style = RFButtonStyle.SECONDARY,
        enabled = enabled,
        icon = icon,
        loading = loading
    )
}

/**
 * Text button shorthand
 */
@Composable
fun RFTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null
) {
    RFButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        style = RFButtonStyle.TEXT,
        enabled = enabled,
        icon = icon,
        loading = false
    )
}
