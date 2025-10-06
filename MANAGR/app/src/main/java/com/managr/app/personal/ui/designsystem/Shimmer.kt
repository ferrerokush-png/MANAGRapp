package com.managr.app.personal.ui.designsystem

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.managr.app.personal.ui.theme.LocalRFShapesTokens

@Composable
fun ShimmerBox(
    modifier: Modifier = Modifier,
    height: Dp = 16.dp,
    baseColor: Color? = null,
    highlightColor: Color? = null,
    cornerRadius: Dp? = null,
    shimmerDurationMillis: Int = 1300,
) {
    val resolvedBase = baseColor ?: MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
    val resolvedHighlight = highlightColor ?: Color.White.copy(alpha = 0.35f)
    val resolvedCorner = cornerRadius ?: LocalRFShapesTokens.current.smallRadius.dp

    val shape = RoundedCornerShape(resolvedCorner)
    var size by remember { mutableStateOf(IntSize.Zero) }
    val transition = rememberInfiniteTransition(label = "shimmer")
    val anim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = shimmerDurationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer-fraction"
    )

    val widthPx = with(LocalDensity.current) { size.width.toFloat() }
    val startX = -widthPx
    val endX = widthPx * 2f
    val currentX = startX + (endX - startX) * anim

    val brush = Brush.linearGradient(
        colors = listOf(
            resolvedBase,
            resolvedHighlight,
            resolvedBase
        ),
        start = Offset(currentX - widthPx, 0f),
        end = Offset(currentX, 0f)
    )

    Box(
        modifier = modifier
            .onGloballyPositioned { size = it.size }
            .fillMaxWidth()
            .height(height)
            .clip(shape)
            .background(brush = brush, shape = shape)
    )
}

@Composable
fun ShimmerListItem(
    modifier: Modifier = Modifier,
) {
    Box(modifier) {
        ShimmerBox(height = 72.dp, cornerRadius = LocalRFShapesTokens.current.mediumRadius.dp)
    }
}

@Composable
fun ShimmerImage(
    modifier: Modifier = Modifier,
    size: Dp = 96.dp,
) {
    Box(
        modifier = modifier
            .height(size)
            .clip(RoundedCornerShape(LocalRFShapesTokens.current.mediumRadius.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
    ) {
        ShimmerBox(height = size)
    }
}
