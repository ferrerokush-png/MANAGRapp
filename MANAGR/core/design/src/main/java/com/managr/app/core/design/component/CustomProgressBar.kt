package com.managr.app.core.design.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Circular progress bar with percentage display
 * 
 * @param percentage Progress percentage (0-100)
 * @param modifier Modifier
 * @param size Size of the circular progress
 * @param strokeWidth Width of the progress stroke
 * @param color Progress bar color
 * @param backgroundColor Background track color
 * @param showPercentage Whether to show percentage text
 * @param animated Whether to animate progress changes
 */
@Composable
fun CustomProgressBar(
    percentage: Float,
    modifier: Modifier = Modifier,
    size: Dp = 120.dp,
    strokeWidth: Dp = 12.dp,
    color: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    showPercentage: Boolean = true,
    animated: Boolean = true
) {
    val animatedPercentage by animateFloatAsState(
        targetValue = if (animated) percentage else percentage,
        animationSpec = tween(durationMillis = 1000),
        label = "progress"
    )
    
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val sweepAngle = (animatedPercentage / 100f) * 360f
            
            // Background track
            drawArc(
                color = backgroundColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
            
            // Progress arc
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }
        
        if (showPercentage) {
            Text(
                text = "${animatedPercentage.toInt()}%",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

/**
 * Linear progress bar with label
 */
@Composable
fun LinearProgressBar(
    percentage: Float,
    label: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    height: Dp = 8.dp,
    showPercentage: Boolean = true
) {
    val animatedPercentage by animateFloatAsState(
        targetValue = percentage,
        animationSpec = tween(durationMillis = 1000),
        label = "linearProgress"
    )
    
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            if (showPercentage) {
                Text(
                    text = "${animatedPercentage.toInt()}%",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
        ) {
            val width = size.width * (animatedPercentage / 100f)
            
            // Background
            drawRoundRect(
                color = backgroundColor,
                size = size.copy(height = height.toPx())
            )
            
            // Progress
            drawRoundRect(
                color = color,
                size = size.copy(width = width, height = height.toPx())
            )
        }
    }
}
