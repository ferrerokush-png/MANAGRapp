package com.managr.app.core.design.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.managr.app.core.design.animations.fadeIn
import com.managr.app.core.design.animations.pulseAnimation

/**
 * Empty state component with illustration and action
 * 
 * @param title Empty state title
 * @param description Empty state description
 * @param icon Icon to display
 * @param modifier Modifier
 * @param actionText Optional action button text
 * @param onActionClick Optional action button click handler
 */
@Composable
fun EmptyState(
    title: String,
    description: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp)
            .fadeIn(enabled = true),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .size(120.dp)
                .pulseAnimation(enabled = true),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
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
        
        // Action button
        if (actionText != null && onActionClick != null) {
            Spacer(modifier = Modifier.height(24.dp))
            
            PrimaryButton(
                text = actionText,
                onClick = onActionClick
            )
        }
    }
}

/**
 * Empty projects state
 */
@Composable
fun EmptyProjectsState(
    onCreateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    EmptyState(
        title = "No Projects Yet",
        description = "Create your first music release project to get started with MANAGR",
        icon = Icons.Outlined.LibraryMusic,
        modifier = modifier,
        actionText = "Create Project",
        onActionClick = onCreateClick
    )
}

/**
 * Empty tasks state
 */
@Composable
fun EmptyTasksState(
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    EmptyState(
        title = "No Tasks",
        description = "Add tasks to organize your release workflow",
        icon = Icons.Outlined.CheckCircle,
        modifier = modifier,
        actionText = "Add Task",
        onActionClick = onAddClick
    )
}

/**
 * Empty analytics state
 */
@Composable
fun EmptyAnalyticsState(
    onAddDataClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    EmptyState(
        title = "No Analytics Data",
        description = "Add your streaming and social media data to see insights",
        icon = Icons.Outlined.Analytics,
        modifier = modifier,
        actionText = "Add Data",
        onActionClick = onAddDataClick
    )
}

/**
 * Empty contacts state
 */
@Composable
fun EmptyContactsState(
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    EmptyState(
        title = "No Contacts",
        description = "Add playlist curators, bloggers, and industry contacts",
        icon = Icons.Outlined.Contacts,
        modifier = modifier,
        actionText = "Add Contact",
        onActionClick = onAddClick
    )
}
