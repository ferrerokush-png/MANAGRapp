package com.managr.app.core.design.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Error state component with retry action
 * 
 * @param title Error title
 * @param description Error description
 * @param icon Icon to display
 * @param modifier Modifier
 * @param onRetryClick Retry button click handler
 */
@Composable
fun ErrorState(
    title: String,
    description: String,
    icon: ImageVector = Icons.Outlined.ErrorOutline,
    modifier: Modifier = Modifier,
    onRetryClick: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.6f)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Title
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Description
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        
        // Retry button
        if (onRetryClick != null) {
            Spacer(modifier = Modifier.height(24.dp))
            
            PrimaryButton(
                text = "Retry",
                onClick = onRetryClick,
                icon = Icons.Outlined.Refresh
            )
        }
    }
}

/**
 * Network error state
 */
@Composable
fun NetworkErrorState(
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ErrorState(
        title = "No Internet Connection",
        description = "Please check your internet connection and try again",
        icon = Icons.Outlined.WifiOff,
        modifier = modifier,
        onRetryClick = onRetryClick
    )
}

/**
 * Generic error state
 */
@Composable
fun GenericErrorState(
    message: String = "Something went wrong",
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ErrorState(
        title = "Oops!",
        description = message,
        icon = Icons.Outlined.ErrorOutline,
        modifier = modifier,
        onRetryClick = onRetryClick
    )
}

/**
 * Data load error state
 */
@Composable
fun DataLoadErrorState(
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ErrorState(
        title = "Failed to Load Data",
        description = "We couldn't load your data. Please try again",
        icon = Icons.Outlined.ErrorOutline,
        modifier = modifier,
        onRetryClick = onRetryClick
    )
}
