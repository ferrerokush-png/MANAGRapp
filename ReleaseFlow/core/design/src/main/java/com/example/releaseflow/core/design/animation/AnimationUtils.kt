package com.example.releaseflow.core.design.animation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import com.example.releaseflow.core.design.theme.MotionTokens
import com.example.releaseflow.core.design.theme.rememberAnimationSpec

/**
 * Smooth scale animation on press
 * Perfect for buttons and interactive elements
 */
fun Modifier.smoothScale(
    targetScale: Float = 0.95f,
    enabled: Boolean = true
): Modifier = composed {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) targetScale else 1f,
        animationSpec = rememberAnimationSpec(
            normalSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        ),
        label = "scale"
    )

    if (enabled) {
        this
            .scale(scale)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    }
                )
            }
    } else {
        this
    }
}

/**
 * Shimmer effect for loading states
 */
fun Modifier.shimmerEffect(
    enabled: Boolean = true,
    durationMillis: Int = MotionTokens.Duration.SHIMMER_CYCLE
): Modifier = composed {
    if (!enabled) return@composed this

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = MotionTokens.Easings.Linear
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )

    this.graphicsLayer {
        translationX = translateAnim
    }
}

/**
 * Fade in animation
 */
fun fadeIn(
    durationMillis: Int = MotionTokens.Duration.MEDIUM
): EnterTransition = fadeIn(
    animationSpec = tween(
        durationMillis = durationMillis,
        easing = MotionTokens.Easings.Standard
    )
)

/**
 * Fade out animation
 */
fun fadeOut(
    durationMillis: Int = MotionTokens.Duration.MEDIUM
): ExitTransition = fadeOut(
    animationSpec = tween(
        durationMillis = durationMillis,
        easing = MotionTokens.Easings.Standard
    )
)

/**
 * Slide in from bottom
 */
fun slideInFromBottom(
    durationMillis: Int = MotionTokens.Duration.MEDIUM
): EnterTransition = slideInVertically(
    animationSpec = tween(
        durationMillis = durationMillis,
        easing = MotionTokens.Easings.EmphasizedDecelerate
    ),
    initialOffsetY = { it }
)

/**
 * Slide out to bottom
 */
fun slideOutToBottom(
    durationMillis: Int = MotionTokens.Duration.MEDIUM
): ExitTransition = slideOutVertically(
    animationSpec = tween(
        durationMillis = durationMillis,
        easing = MotionTokens.Easings.EmphasizedAccelerate
    ),
    targetOffsetY = { it }
)

/**
 * Slide in from top
 */
fun slideInFromTop(
    durationMillis: Int = MotionTokens.Duration.MEDIUM
): EnterTransition = slideInVertically(
    animationSpec = tween(
        durationMillis = durationMillis,
        easing = MotionTokens.Easings.EmphasizedDecelerate
    ),
    initialOffsetY = { -it }
)

/**
 * Slide out to top
 */
fun slideOutToTop(
    durationMillis: Int = MotionTokens.Duration.MEDIUM
): ExitTransition = slideOutVertically(
    animationSpec = tween(
        durationMillis = durationMillis,
        easing = MotionTokens.Easings.EmphasizedAccelerate
    ),
    targetOffsetY = { -it }
)

/**
 * Scale and fade in
 */
fun scaleIn(
    durationMillis: Int = MotionTokens.Duration.MEDIUM,
    initialScale: Float = 0.8f
): EnterTransition = scaleIn(
    animationSpec = tween(
        durationMillis = durationMillis,
        easing = MotionTokens.Easings.EmphasizedDecelerate
    ),
    initialScale = initialScale
) + fadeIn(durationMillis)

/**
 * Scale and fade out
 */
fun scaleOut(
    durationMillis: Int = MotionTokens.Duration.MEDIUM,
    targetScale: Float = 0.8f
): ExitTransition = scaleOut(
    animationSpec = tween(
        durationMillis = durationMillis,
        easing = MotionTokens.Easings.EmphasizedAccelerate
    ),
    targetScale = targetScale
) + fadeOut(durationMillis)

/**
 * Expand vertically
 */
fun expandVertically(
    durationMillis: Int = MotionTokens.Duration.MEDIUM
): EnterTransition = expandVertically(
    animationSpec = tween(
        durationMillis = durationMillis,
        easing = MotionTokens.Easings.EmphasizedDecelerate
    )
)

/**
 * Shrink vertically
 */
fun shrinkVertically(
    durationMillis: Int = MotionTokens.Duration.MEDIUM
): ExitTransition = shrinkVertically(
    animationSpec = tween(
        durationMillis = durationMillis,
        easing = MotionTokens.Easings.EmphasizedAccelerate
    )
)

/**
 * Staggered list item animation
 */
@Composable
fun rememberStaggeredAnimationDelay(
    index: Int,
    baseDelay: Int = 50
): Int {
    return remember(index, baseDelay) {
        index * baseDelay
    }
}
