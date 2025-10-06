package com.managr.app.core.design.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Animated loading indicator with smooth rotation
 */
@Composable
fun AnimatedLoadingIndicator(
    message: String = "Loading...",
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(48.dp),
            strokeWidth = 4.dp,
            color = MaterialTheme.colorScheme.primary
        )
        
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

/**
 * Shimmer loading effect for cards
 */
@Composable
fun ShimmerCard(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.1f),
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)
                        )
                    )
                )
        )
    }
}

/**
 * Animated empty state with illustration
 */
@Composable
fun AnimatedEmptyState(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector = Icons.Outlined.Inbox,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .size(80.dp)
,
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
        )
        
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        
        if (actionText != null && onActionClick != null) {
            Button(
                onClick = onActionClick
            ) {
                Text(actionText)
            }
        }
    }
}

/**
 * Animated error state with retry button
 */
@Composable
fun AnimatedErrorState(
    message: String,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.ErrorOutline,
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
,
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
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        
        Button(
            onClick = onRetryClick
        ) {
            Icon(Icons.Outlined.Refresh, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Try Again")
        }
    }
}

/**
 * Animated FAB with show/hide animation
 */
@Composable
fun AnimatedFAB(
    onClick: () -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector = Icons.Outlined.Add,
    modifier: Modifier = Modifier,
    visible: Boolean = true
) {
    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        ) + fadeIn(),
        exit = scaleOut() + fadeOut()
    ) {
        FloatingActionButton(
            onClick = onClick,
            modifier = modifier
        ) {
            Icon(icon, contentDescription = null)
        }
    }
}

/**
 * Animated card with hover effects
 */
@Composable
fun AnimatedCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    
    Card(
        onClick = onClick,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

/**
 * Animated list with staggered items
 */
@Composable
fun <T> AnimatedLazyColumn(
    items: List<T>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(8.dp),
    itemContent: @Composable (T, Int) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = verticalArrangement
    ) {
        itemsIndexed(items) { index, item ->
            Box(
                modifier = Modifier
            ) {
                itemContent(item, index)
            }
        }
    }
}

/**
 * Animated list with staggered items
 */
@Composable
fun <T> AnimatedLazyRow(
    items: List<T>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(8.dp),
    itemContent: @Composable (T, Int) -> Unit
) {
    LazyRow(
        modifier = modifier,
        contentPadding = contentPadding,
        horizontalArrangement = horizontalArrangement
    ) {
        itemsIndexed(items) { index, item ->
            Box(
                modifier = Modifier
            ) {
                itemContent(item, index)
            }
        }
    }
}

/**
 * Animated progress bar
 */
@Composable
fun AnimatedProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        ),
        label = "progress"
    )
    
    LinearProgressIndicator(
        progress = animatedProgress,
        modifier = modifier,
        color = color
    )
}

/**
 * Animated counter text
 */
@Composable
fun AnimatedCounterText(
    count: Int,
    modifier: Modifier = Modifier,
    style: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.headlineMedium
) {
    val animatedCount by animateIntAsState(
        targetValue = count,
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        ),
        label = "counter"
    )
    
    Text(
        text = animatedCount.toString(),
        modifier = modifier,
        style = style
    )
}

/**
 * Animated badge with scale animation
 */
@Composable
fun AnimatedBadge(
    count: Int,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.error
) {
    val scale by animateFloatAsState(
        targetValue = if (count > 0) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "badge_scale"
    )
    
    if (count > 0) {
        Surface(
            modifier = modifier,
            shape = CircleShape,
            color = color
        ) {
            Text(
                text = count.toString(),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelSmall,
                color = Color.White
            )
        }
    }
}
