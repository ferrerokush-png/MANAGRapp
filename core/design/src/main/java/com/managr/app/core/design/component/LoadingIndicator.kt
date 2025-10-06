package com.managr.app.core.design.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.managr.app.core.design.theme.MotionTokens

/**
 * Loading indicator with optional message
 * 
 * @param modifier Modifier
 * @param message Optional loading message
 */
@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier,
    message: String? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 4.dp
        )
        
        if (message != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Shimmer loading effect for placeholders
 */
@Composable
fun ShimmerBox(
    modifier: Modifier = Modifier,
    shape: androidx.compose.ui.graphics.Shape = RoundedCornerShape(8.dp)
) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = MotionTokens.Duration.SHIMMER_CYCLE,
                easing = MotionTokens.Easings.Linear
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )
    
    val shimmerColors = listOf(
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    )
    
    Box(
        modifier = modifier
            .clip(shape)
            .background(
                brush = Brush.horizontalGradient(
                    colors = shimmerColors,
                    startX = translateAnim - 1000f,
                    endX = translateAnim
                )
            )
    )
}

/**
 * Shimmer card placeholder
 */
@Composable
fun ShimmerCard(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Title shimmer
        ShimmerBox(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(24.dp)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Subtitle shimmer
        ShimmerBox(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(16.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Content shimmer
        ShimmerBox(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )
    }
}

/**
 * Shimmer list item
 */
@Composable
fun ShimmerListItem(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar shimmer
        ShimmerBox(
            modifier = Modifier.size(48.dp),
            shape = CircleShape
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            // Title shimmer
            ShimmerBox(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(20.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Subtitle shimmer
            ShimmerBox(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(16.dp)
            )
        }
    }
}
