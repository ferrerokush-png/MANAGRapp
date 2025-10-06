package com.managr.app.core.design.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Snackbar types with different visual styles
 */
enum class SnackbarType {
    SUCCESS,
    ERROR,
    WARNING,
    INFO,
    LOADING
}

/**
 * Animated snackbar with different types and animations
 */
@Composable
fun AnimatedSnackbar(
    message: String,
    type: SnackbarType = SnackbarType.INFO,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    visible: Boolean = true,
    modifier: Modifier = Modifier,
    onDismiss: (() -> Unit)? = null
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            ),
            initialOffsetY = { it }
        ) + fadeIn(
            animationSpec = tween(300)
        ),
        exit = slideOutVertically(
            animationSpec = tween(300)
        ) + fadeOut(
            animationSpec = tween(300)
        ),
        modifier = modifier
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(
                containerColor = getSnackbarColor(type)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Icon
                Icon(
                    imageVector = getSnackbarIcon(type),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = getSnackbarIconColor(type)
                )
                
                // Message
                Text(
                    text = message,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyMedium,
                    color = getSnackbarTextColor(type),
                    fontWeight = FontWeight.Medium
                )
                
                // Action button
                if (actionText != null && onActionClick != null) {
                    TextButton(
                        onClick = onActionClick,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = getSnackbarTextColor(type)
                        )
                    ) {
                        Text(actionText)
                    }
                }
                
                // Dismiss button
                if (onDismiss != null) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = "Dismiss",
                            modifier = Modifier.size(16.dp),
                            tint = getSnackbarTextColor(type)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Success snackbar
 */
@Composable
fun SuccessSnackbar(
    message: String,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    visible: Boolean = true,
    modifier: Modifier = Modifier,
    onDismiss: (() -> Unit)? = null
) {
    AnimatedSnackbar(
        message = message,
        type = SnackbarType.SUCCESS,
        actionText = actionText,
        onActionClick = onActionClick,
        visible = visible,
        modifier = modifier,
        onDismiss = onDismiss
    )
}

/**
 * Error snackbar
 */
@Composable
fun ErrorSnackbar(
    message: String,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    visible: Boolean = true,
    modifier: Modifier = Modifier,
    onDismiss: (() -> Unit)? = null
) {
    AnimatedSnackbar(
        message = message,
        type = SnackbarType.ERROR,
        actionText = actionText,
        onActionClick = onActionClick,
        visible = visible,
        modifier = modifier,
        onDismiss = onDismiss
    )
}

/**
 * Warning snackbar
 */
@Composable
fun WarningSnackbar(
    message: String,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    visible: Boolean = true,
    modifier: Modifier = Modifier,
    onDismiss: (() -> Unit)? = null
) {
    AnimatedSnackbar(
        message = message,
        type = SnackbarType.WARNING,
        actionText = actionText,
        onActionClick = onActionClick,
        visible = visible,
        modifier = modifier,
        onDismiss = onDismiss
    )
}

/**
 * Info snackbar
 */
@Composable
fun InfoSnackbar(
    message: String,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    visible: Boolean = true,
    modifier: Modifier = Modifier,
    onDismiss: (() -> Unit)? = null
) {
    AnimatedSnackbar(
        message = message,
        type = SnackbarType.INFO,
        actionText = actionText,
        onActionClick = onActionClick,
        visible = visible,
        modifier = modifier,
        onDismiss = onDismiss
    )
}

/**
 * Loading snackbar
 */
@Composable
fun LoadingSnackbar(
    message: String,
    visible: Boolean = true,
    modifier: Modifier = Modifier,
    onDismiss: (() -> Unit)? = null
) {
    AnimatedSnackbar(
        message = message,
        type = SnackbarType.LOADING,
        visible = visible,
        modifier = modifier,
        onDismiss = onDismiss
    )
}

/**
 * Get snackbar color based on type
 */
@Composable
private fun getSnackbarColor(type: SnackbarType): Color {
    return when (type) {
        SnackbarType.SUCCESS -> MaterialTheme.colorScheme.primaryContainer
        SnackbarType.ERROR -> MaterialTheme.colorScheme.errorContainer
        SnackbarType.WARNING -> MaterialTheme.colorScheme.tertiaryContainer
        SnackbarType.INFO -> MaterialTheme.colorScheme.secondaryContainer
        SnackbarType.LOADING -> MaterialTheme.colorScheme.surfaceVariant
    }
}

/**
 * Get snackbar icon color based on type
 */
@Composable
private fun getSnackbarIconColor(type: SnackbarType): Color {
    return when (type) {
        SnackbarType.SUCCESS -> MaterialTheme.colorScheme.onPrimaryContainer
        SnackbarType.ERROR -> MaterialTheme.colorScheme.onErrorContainer
        SnackbarType.WARNING -> MaterialTheme.colorScheme.onTertiaryContainer
        SnackbarType.INFO -> MaterialTheme.colorScheme.onSecondaryContainer
        SnackbarType.LOADING -> MaterialTheme.colorScheme.onSurfaceVariant
    }
}

/**
 * Get snackbar text color based on type
 */
@Composable
private fun getSnackbarTextColor(type: SnackbarType): Color {
    return when (type) {
        SnackbarType.SUCCESS -> MaterialTheme.colorScheme.onPrimaryContainer
        SnackbarType.ERROR -> MaterialTheme.colorScheme.onErrorContainer
        SnackbarType.WARNING -> MaterialTheme.colorScheme.onTertiaryContainer
        SnackbarType.INFO -> MaterialTheme.colorScheme.onSecondaryContainer
        SnackbarType.LOADING -> MaterialTheme.colorScheme.onSurfaceVariant
    }
}

/**
 * Get snackbar icon based on type
 */
private fun getSnackbarIcon(type: SnackbarType): ImageVector {
    return when (type) {
        SnackbarType.SUCCESS -> Icons.Outlined.CheckCircle
        SnackbarType.ERROR -> Icons.Outlined.Error
        SnackbarType.WARNING -> Icons.Outlined.Warning
        SnackbarType.INFO -> Icons.Outlined.Info
        SnackbarType.LOADING -> Icons.Outlined.HourglassEmpty
    }
}

/**
 * Animated snackbar with custom animation
 */
@Composable
fun AnimatedCustomSnackbar(
    message: String,
    type: SnackbarType = SnackbarType.INFO,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    visible: Boolean = true,
    modifier: Modifier = Modifier,
    onDismiss: (() -> Unit)? = null,
    enterAnimation: EnterTransition = slideInVertically() + fadeIn(),
    exitAnimation: ExitTransition = slideOutVertically() + fadeOut()
) {
    AnimatedVisibility(
        visible = visible,
        enter = enterAnimation,
        exit = exitAnimation,
        modifier = modifier
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(
                containerColor = getSnackbarColor(type)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Icon
                Icon(
                    imageVector = getSnackbarIcon(type),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = getSnackbarIconColor(type)
                )
                
                // Message
                Text(
                    text = message,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyMedium,
                    color = getSnackbarTextColor(type),
                    fontWeight = FontWeight.Medium
                )
                
                // Action button
                if (actionText != null && onActionClick != null) {
                    TextButton(
                        onClick = onActionClick,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = getSnackbarTextColor(type)
                        )
                    ) {
                        Text(actionText)
                    }
                }
                
                // Dismiss button
                if (onDismiss != null) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = "Dismiss",
                            modifier = Modifier.size(16.dp),
                            tint = getSnackbarTextColor(type)
                        )
                    }
                }
            }
        }
    }
}
