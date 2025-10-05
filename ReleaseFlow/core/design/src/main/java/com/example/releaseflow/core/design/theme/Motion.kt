package com.example.releaseflow.core.design.theme

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/**
 * Motion tokens for Release Flow
 * Defines animation durations and easing curves for 60fps performance
 */
object MotionTokens {
    /**
     * Duration tokens (in milliseconds)
     * Optimized for 60fps (16.67ms per frame)
     */
    object Duration {
        const val INSTANT = 0
        const val FAST = 150 // ~9 frames
        const val MEDIUM = 250 // ~15 frames
        const val SLOW = 350 // ~21 frames
        const val SLOWER = 500 // ~30 frames
        const val SLOWEST = 700 // ~42 frames
        
        // Specific use cases
        const val BUTTON_PRESS = 100
        const val CARD_EXPAND = 300
        const val SHEET_SLIDE = 400
        const val DIALOG_FADE = 200
        const val PAGE_TRANSITION = 350
        const val SHIMMER_CYCLE = 1500
    }

    /**
     * Easing curves for natural motion
     */
    object Easings {
        // Standard Material easing
        val Standard: Easing = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)
        val StandardDecelerate: Easing = CubicBezierEasing(0.0f, 0.0f, 0.2f, 1.0f)
        val StandardAccelerate: Easing = CubicBezierEasing(0.4f, 0.0f, 1.0f, 1.0f)
        
        // Custom easing for Release Flow
        val Emphasized: Easing = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)
        val EmphasizedDecelerate: Easing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1.0f)
        val EmphasizedAccelerate: Easing = CubicBezierEasing(0.3f, 0.0f, 0.8f, 0.15f)
        
        // Smooth curves for glass morphism
        val Smooth: Easing = CubicBezierEasing(0.4f, 0.0f, 0.6f, 1.0f)
        val SmoothIn: Easing = CubicBezierEasing(0.0f, 0.0f, 0.6f, 1.0f)
        val SmoothOut: Easing = CubicBezierEasing(0.4f, 0.0f, 1.0f, 1.0f)
        
        // Bouncy for playful interactions
        val Bounce: Easing = CubicBezierEasing(0.68f, -0.55f, 0.265f, 1.55f)
        
        // Standard system easings
        val FastOutSlowIn: Easing = FastOutSlowInEasing
        val Linear: Easing = LinearEasing
    }

    /**
     * Spring configurations for natural motion
     */
    object Springs {
        val Soft = spring<Float>(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
        
        val Medium = spring<Float>(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium
        )
        
        val Stiff = spring<Float>(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessHigh
        )
        
        val Bouncy = spring<Float>(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
    }

    /**
     * Pre-configured animation specs
     */
    object Specs {
        fun <T> fast(easing: Easing = Easings.Standard): AnimationSpec<T> =
            tween(durationMillis = Duration.FAST, easing = easing)
        
        fun <T> medium(easing: Easing = Easings.Standard): AnimationSpec<T> =
            tween(durationMillis = Duration.MEDIUM, easing = easing)
        
        fun <T> slow(easing: Easing = Easings.Standard): AnimationSpec<T> =
            tween(durationMillis = Duration.SLOW, easing = easing)
        
        fun <T> emphasized(easing: Easing = Easings.Emphasized): AnimationSpec<T> =
            tween(durationMillis = Duration.MEDIUM, easing = easing)
        
        fun <T> smooth(easing: Easing = Easings.Smooth): AnimationSpec<T> =
            tween(durationMillis = Duration.MEDIUM, easing = easing)
    }
}

/**
 * Get animation duration based on reduced motion preference
 */
@Composable
fun rememberAnimationDuration(
    normalDuration: Int,
    reducedDuration: Int = 0
): Int {
    val reducedMotion = LocalReducedMotion.current
    return remember(reducedMotion, normalDuration, reducedDuration) {
        if (reducedMotion) reducedDuration else normalDuration
    }
}

/**
 * Get animation spec based on reduced motion preference
 */
@Composable
fun <T> rememberAnimationSpec(
    normalSpec: AnimationSpec<T>,
    reducedSpec: AnimationSpec<T> = tween(durationMillis = 0)
): AnimationSpec<T> {
    val reducedMotion = LocalReducedMotion.current
    return remember(reducedMotion) {
        if (reducedMotion) reducedSpec else normalSpec
    }
}
