package com.managr.app.core.design.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.managr.app.core.design.animations.fadeIn
import com.managr.app.core.design.animations.pulseAnimation
import com.managr.app.core.design.error.AppError

/**
 * Generic error state component
 */
@Composable
fun GenericErrorState(
    message: String,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
    retryText: String = "Try Again"
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp)
            .fadeIn(enabled = true),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.ErrorOutline,
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .pulseAnimation(enabled = true),
            tint = MaterialTheme.colorScheme.error
        )
        
        Text(
            text = "Something went wrong",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.error
        )
        
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
        
        Button(
            onClick = onRetryClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Icon(Icons.Outlined.Refresh, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(retryText)
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
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp)
            .fadeIn(enabled = true),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.WifiOff,
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .pulseAnimation(enabled = true),
            tint = MaterialTheme.colorScheme.error
        )
        
        Text(
            text = "No Internet Connection",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.error
        )
        
        Text(
            text = "Please check your internet connection and try again",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
        
        Button(
            onClick = onRetryClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Icon(Icons.Outlined.Refresh, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Retry")
        }
    }
}

/**
 * Database error state
 */
@Composable
fun DatabaseErrorState(
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp)
            .fadeIn(enabled = true),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.Storage,
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .pulseAnimation(enabled = true),
            tint = MaterialTheme.colorScheme.error
        )
        
        Text(
            text = "Database Error",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.error
        )
        
        Text(
            text = "There was a problem accessing your data. Please try again",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
        
        Button(
            onClick = onRetryClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Icon(Icons.Outlined.Refresh, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Retry")
        }
    }
}

/**
 * Authentication error state
 */
@Composable
fun AuthenticationErrorState(
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp)
            .fadeIn(enabled = true),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.Lock,
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .pulseAnimation(enabled = true),
            tint = MaterialTheme.colorScheme.error
        )
        
        Text(
            text = "Authentication Required",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.error
        )
        
        Text(
            text = "Please log in to continue",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
        
        Button(
            onClick = onLoginClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Icon(Icons.Outlined.Login, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Login")
        }
    }
}

/**
 * Permission error state
 */
@Composable
fun PermissionErrorState(
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp)
            .fadeIn(enabled = true),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.Security,
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .pulseAnimation(enabled = true),
            tint = MaterialTheme.colorScheme.error
        )
        
        Text(
            text = "Permission Required",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.error
        )
        
        Text(
            text = "Please grant the required permissions to continue",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
        
        Button(
            onClick = onSettingsClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Icon(Icons.Outlined.Settings, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Settings")
        }
    }
}

/**
 * Validation error state
 */
@Composable
fun ValidationErrorState(
    message: String,
    field: String? = null,
    onFixClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp)
            .fadeIn(enabled = true),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.Warning,
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .pulseAnimation(enabled = true),
            tint = MaterialTheme.colorScheme.tertiary
        )
        
        Text(
            text = "Validation Error",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.tertiary
        )
        
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
        
        if (field != null) {
            Text(
                text = "Field: $field",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                textAlign = TextAlign.Center
            )
        }
        
        Button(
            onClick = onFixClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary
            )
        ) {
            Icon(Icons.Outlined.Edit, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Fix")
        }
    }
}

/**
 * Error state based on AppError type
 */
@Composable
fun ErrorStateFromAppError(
    error: AppError,
    onRetryClick: () -> Unit,
    onLoginClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onFixClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    when (error) {
        is AppError.NetworkError -> {
            NetworkErrorState(
                onRetryClick = onRetryClick,
                modifier = modifier
            )
        }
        is AppError.DatabaseError -> {
            DatabaseErrorState(
                onRetryClick = onRetryClick,
                modifier = modifier
            )
        }
        is AppError.AuthenticationError -> {
            AuthenticationErrorState(
                onLoginClick = onLoginClick,
                modifier = modifier
            )
        }
        is AppError.PermissionError -> {
            PermissionErrorState(
                onSettingsClick = onSettingsClick,
                modifier = modifier
            )
        }
        is AppError.ValidationError -> {
            ValidationErrorState(
                message = error.message,
                field = error.field,
                onFixClick = onFixClick,
                modifier = modifier
            )
        }
        is AppError.UnknownError -> {
            GenericErrorState(
                message = error.message,
                onRetryClick = onRetryClick,
                modifier = modifier
            )
        }
    }
}

/**
 * Error state with custom icon and message
 */
@Composable
fun CustomErrorState(
    title: String,
    message: String,
    icon: ImageVector = Icons.Outlined.ErrorOutline,
    onActionClick: () -> Unit,
    actionText: String = "Retry",
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp)
            .fadeIn(enabled = true),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .pulseAnimation(enabled = true),
            tint = MaterialTheme.colorScheme.error
        )
        
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.error
        )
        
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
        
        Button(
            onClick = onActionClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text(actionText)
        }
    }
}
