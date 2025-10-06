package com.managr.app.core.design.animations

import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

/**
 * Animation specifications for consistent motion throughout the app
 */
object AnimationSpecs {
    val FastOutSlowIn = FastOutSlowInEasing
    val LinearOutSlowIn = LinearOutSlowInEasing
    val FastOutLinearIn = FastOutLinearInEasing
    
    val ShortDuration = 200
    val MediumDuration = 300
    val LongDuration = 500
    
    val SpringStiffness = Spring.StiffnessMedium
    val SpringDampingRatio = Spring.DampingRatioMediumBouncy
}

/**
 * Smooth scale animation for interactive elements
 */
@Composable
fun Modifier.smoothScale(
    enabled: Boolean = true,
    scale: Float = 0.95f,
    duration: Int = AnimationSpecs.ShortDuration
): Modifier = composed {
    var isPressed by remember { mutableStateOf(false) }
    val scaleValue by animateFloatAsState(
        targetValue = if (isPressed && enabled) scale else 1f,
        animationSpec = tween(
            durationMillis = duration,
            easing = AnimationSpecs.FastOutSlowIn
        ),
        label = "scale"
    )
    
    this
        .scale(scaleValue)
        .pointerInput(enabled) {
            if (enabled) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    }
                )
            }
        }
}

/**
 * Shimmer effect for loading states
 */
@Composable
fun Modifier.shimmerEffect(
    enabled: Boolean = true,
    duration: Int = 1000
): Modifier = composed {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(duration, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer_alpha"
    )
    
    if (enabled) {
        this.graphicsLayer(alpha = alpha)
    } else {
        this
    }
}

/**
 * Fade in animation for content
 */
@Composable
fun Modifier.fadeIn(
    enabled: Boolean = true,
    duration: Int = AnimationSpecs.MediumDuration
): Modifier = composed {
    var isVisible by remember { mutableStateOf(!enabled) }
    
    LaunchedEffect(enabled) {
        if (enabled) {
            isVisible = true
        }
    }
    
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(
            durationMillis = duration,
            easing = AnimationSpecs.FastOutSlowIn
        ),
        label = "fade_alpha"
    )
    
    this.graphicsLayer(alpha = alpha)
}

/**
 * Slide in animation from bottom
 */
@Composable
fun Modifier.slideInFromBottom(
    enabled: Boolean = true,
    duration: Int = AnimationSpecs.MediumDuration
): Modifier = composed {
    var isVisible by remember { mutableStateOf(!enabled) }
    
    LaunchedEffect(enabled) {
        if (enabled) {
            isVisible = true
        }
    }
    
    val offsetY by animateFloatAsState(
        targetValue = if (isVisible) 0f else 100f,
        animationSpec = tween(
            durationMillis = duration,
            easing = AnimationSpecs.FastOutSlowIn
        ),
        label = "slide_offset"
    )
    
    this.graphicsLayer(translationY = offsetY)
}

/**
 * Slide in animation from right
 */
@Composable
fun Modifier.slideInFromRight(
    enabled: Boolean = true,
    duration: Int = AnimationSpecs.MediumDuration
): Modifier = composed {
    var isVisible by remember { mutableStateOf(!enabled) }
    
    LaunchedEffect(enabled) {
        if (enabled) {
            isVisible = true
        }
    }
    
    val offsetX by animateFloatAsState(
        targetValue = if (isVisible) 0f else 100f,
        animationSpec = tween(
            durationMillis = duration,
            easing = AnimationSpecs.FastOutSlowIn
        ),
        label = "slide_offset_x"
    )
    
    this.graphicsLayer(translationX = offsetX)
}

/**
 * Elevation animation for cards
 */
@Composable
fun Modifier.elevationAnimation(
    isSelected: Boolean = false,
    baseElevation: Float = 2f,
    selectedElevation: Float = 8f,
    duration: Int = AnimationSpecs.ShortDuration
): Modifier = composed {
    val elevation by animateFloatAsState(
        targetValue = if (isSelected) selectedElevation else baseElevation,
        animationSpec = tween(
            durationMillis = duration,
            easing = AnimationSpecs.FastOutSlowIn
        ),
        label = "elevation"
    )
    
    this.graphicsLayer(shadowElevation = elevation)
}

/**
 * Ripple effect animation
 */
@Composable
fun Modifier.rippleEffect(
    enabled: Boolean = true,
    color: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Unspecified,
    bounded: Boolean = true
): Modifier = composed {
    if (enabled) {
        this
    } else {
        this
    }
}

/**
 * Staggered animation for lists
 */
@Composable
fun Modifier.staggeredAnimation(
    index: Int,
    enabled: Boolean = true,
    delay: Int = 50,
    duration: Int = AnimationSpecs.MediumDuration
): Modifier = composed {
    var isVisible by remember { mutableStateOf(!enabled) }
    
    LaunchedEffect(enabled) {
        if (enabled) {
            kotlinx.coroutines.delay((index * delay).toLong())
            isVisible = true
        }
    }
    
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(
            durationMillis = duration,
            easing = AnimationSpecs.FastOutSlowIn
        ),
        label = "stagger_alpha"
    )
    
    val offsetY by animateFloatAsState(
        targetValue = if (isVisible) 0f else 30f,
        animationSpec = tween(
            durationMillis = duration,
            easing = AnimationSpecs.FastOutSlowIn
        ),
        label = "stagger_offset"
    )
    
    this
        .graphicsLayer(
            alpha = alpha,
            translationY = offsetY
        )
}

/**
 * Loading pulse animation
 */
@Composable
fun Modifier.pulseAnimation(
    enabled: Boolean = true,
    duration: Int = 1000
): Modifier = composed {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(duration, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )
    
    if (enabled) {
        this.scale(scale)
    } else {
        this
    }
}

/**
 * Rotation animation for loading indicators
 */
@Composable
fun Modifier.rotationAnimation(
    enabled: Boolean = true,
    duration: Int = 1000
): Modifier = composed {
    val infiniteTransition = rememberInfiniteTransition(label = "rotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(duration, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation_angle"
    )
    
    if (enabled) {
        this.graphicsLayer(rotationZ = rotation)
    } else {
        this
    }
}
