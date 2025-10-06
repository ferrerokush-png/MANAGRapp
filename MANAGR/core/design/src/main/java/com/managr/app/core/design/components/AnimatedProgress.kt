package com.managr.app.core.design.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

/**
 * Animated circular progress indicator
 */
@Composable
fun AnimatedCircularProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    strokeWidth: Dp = 4.dp,
    color: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    animationDuration: Int = 1000
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(
            durationMillis = animationDuration,
            easing = FastOutSlowInEasing
        ),
        label = "circular_progress"
    )
    
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidthPx = strokeWidth.toPx()
            val radius = (size.toPx() - strokeWidthPx) / 2
            val center = androidx.compose.ui.geometry.Offset(
                this.size.width / 2,
                this.size.height / 2
            )
            
            // Background circle
            drawCircle(
                color = backgroundColor,
                radius = radius,
                center = center,
                style = Stroke(width = strokeWidthPx)
            )
            
            // Progress arc
            val sweepAngle = animatedProgress * 360f
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = center - androidx.compose.ui.geometry.Offset(radius, radius),
                size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
                style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
            )
        }
    }
}

/**
 * Animated circular progress with percentage text
 */
@Composable
fun AnimatedCircularProgressWithText(
    progress: Float,
    modifier: Modifier = Modifier,
    size: Dp = 80.dp,
    strokeWidth: Dp = 6.dp,
    color: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    animationDuration: Int = 1000
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(
            durationMillis = animationDuration,
            easing = FastOutSlowInEasing
        ),
        label = "circular_progress_text"
    )
    
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidthPx = strokeWidth.toPx()
            val radius = (size.toPx() - strokeWidthPx) / 2
            val center = androidx.compose.ui.geometry.Offset(
                this.size.width / 2,
                this.size.height / 2
            )
            
            // Background circle
            drawCircle(
                color = backgroundColor,
                radius = radius,
                center = center,
                style = Stroke(width = strokeWidthPx)
            )
            
            // Progress arc
            val sweepAngle = animatedProgress * 360f
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = center - androidx.compose.ui.geometry.Offset(radius, radius),
                size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
                style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
            )
        }
        
        Text(
            text = "${(animatedProgress * 100).toInt()}%",
            style = MaterialTheme.typography.labelLarge,
            color = textColor,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Animated linear progress indicator
 */
@Composable
fun AnimatedLinearProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    height: Dp = 4.dp,
    color: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    animationDuration: Int = 1000
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(
            durationMillis = animationDuration,
            easing = FastOutSlowInEasing
        ),
        label = "linear_progress"
    )
    
    LinearProgressIndicator(
        progress = animatedProgress,
        modifier = modifier.height(height),
        color = color,
        trackColor = backgroundColor
    )
}

/**
 * Animated linear progress with text
 */
@Composable
fun AnimatedLinearProgressWithText(
    progress: Float,
    text: String,
    modifier: Modifier = Modifier,
    height: Dp = 4.dp,
    color: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    animationDuration: Int = 1000
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor
            )
            
            Text(
                text = "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.bodySmall,
                color = textColor.copy(alpha = 0.7f)
            )
        }
        
        AnimatedLinearProgress(
            progress = progress,
            height = height,
            color = color,
            backgroundColor = backgroundColor,
            animationDuration = animationDuration
        )
    }
}

/**
 * Animated indeterminate progress indicator
 */
@Composable
fun AnimatedIndeterminateProgress(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    strokeWidth: Dp = 4.dp,
    color: Color = MaterialTheme.colorScheme.primary,
    animationDuration: Int = 1000
) {
    val infiniteTransition = rememberInfiniteTransition(label = "indeterminate_progress")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = animationDuration,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidthPx = strokeWidth.toPx()
            val radius = (size.toPx() - strokeWidthPx) / 2
            val center = androidx.compose.ui.geometry.Offset(
                this.size.width / 2,
                this.size.height / 2
            )
            
            // Draw rotating arc
            val sweepAngle = 90f
            val startAngle = rotation - 90f
            
            drawArc(
                color = color,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = center - androidx.compose.ui.geometry.Offset(radius, radius),
                size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
                style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
            )
        }
    }
}

/**
 * Animated dots loading indicator
 */
@Composable
fun AnimatedDotsLoading(
    modifier: Modifier = Modifier,
    dotSize: Dp = 8.dp,
    color: Color = MaterialTheme.colorScheme.primary,
    animationDuration: Int = 600
) {
    val infiniteTransition = rememberInfiniteTransition(label = "dots_loading")
    
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(3) { index ->
            val scale by infiniteTransition.animateFloat(
                initialValue = 0.5f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = animationDuration,
                        delayMillis = index * 200,
                        easing = FastOutSlowInEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "dot_scale_$index"
            )
            
            Box(
                modifier = Modifier
                    .size(dotSize),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = color,
                        radius = dotSize.toPx() / 2
                    )
                }
            }
        }
    }
}

/**
 * Animated pulse loading indicator
 */
@Composable
fun AnimatedPulseLoading(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    color: Color = MaterialTheme.colorScheme.primary,
    animationDuration: Int = 1000
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_loading")
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = animationDuration,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )
    
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = animationDuration,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )
    
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val radius = (size.toPx() * scale) / 2
            val center = androidx.compose.ui.geometry.Offset(
                this.size.width / 2,
                this.size.height / 2
            )
            
            drawCircle(
                color = color.copy(alpha = alpha),
                radius = radius,
                center = center
            )
        }
    }
}

/**
 * Animated wave loading indicator
 */
@Composable
fun AnimatedWaveLoading(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    color: Color = MaterialTheme.colorScheme.primary,
    animationDuration: Int = 1000
) {
    val infiniteTransition = rememberInfiniteTransition(label = "wave_loading")
    
    Row(
        modifier = modifier.height(size),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(5) { index ->
            val height by infiniteTransition.animateFloat(
                initialValue = 0.2f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = animationDuration,
                        delayMillis = index * 100,
                        easing = FastOutSlowInEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "wave_height_$index"
            )
            
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(size * height),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRect(
                        color = color,
                        size = androidx.compose.ui.geometry.Size(
                            width = 4.dp.toPx(),
                            height = (size * height).toPx()
                        )
                    )
                }
            }
        }
    }
}
