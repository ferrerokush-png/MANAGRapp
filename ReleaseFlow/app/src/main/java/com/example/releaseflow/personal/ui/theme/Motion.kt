package com.example.releaseflow.personal.ui.theme

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf

@Immutable
data class RFMotionDurations(
    val veryShort: Int = 120,
    val short: Int = 180,
    val medium: Int = 240,
    val long: Int = 300,
)

@Immutable
data class RFMotionEasing(
    val standard: Easing = FastOutSlowInEasing,
    val decelerate: Easing = LinearOutSlowInEasing,
    val accelerateDecelerate: Easing = FastOutSlowInEasing,
    val linear: Easing = LinearEasing,
)

val LocalRFMotionDurations = staticCompositionLocalOf { RFMotionDurations() }
val LocalRFMotionEasing = staticCompositionLocalOf { RFMotionEasing() }
val LocalRFReducedMotion = staticCompositionLocalOf { false }

@Composable
fun RFMotionProvider(
    durations: RFMotionDurations = RFMotionDurations(),
    easing: RFMotionEasing = RFMotionEasing(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalRFMotionDurations provides durations,
        LocalRFMotionEasing provides easing,
        content = content,
    )
}

@Composable
fun RFMotionProviderWithReducedMotion(
    reduceMotion: Boolean,
    content: @Composable () -> Unit,
) {
    val durations = if (reduceMotion) RFMotionDurations(veryShort = 0, short = 0, medium = 0, long = 0) else RFMotionDurations()
    val easing = if (reduceMotion) RFMotionEasing(linear = LinearEasing, standard = FastOutSlowInEasing, decelerate = LinearOutSlowInEasing, accelerateDecelerate = FastOutSlowInEasing) else RFMotionEasing()

    CompositionLocalProvider(LocalRFReducedMotion provides reduceMotion) {
        RFMotionProvider(durations = durations, easing = easing) {
            content()
        }
    }
}

@Composable
fun rfEnterSlideFade(): EnterTransition {
    val reduced = LocalRFReducedMotion.current
    val d = LocalRFMotionDurations.current
    val e = LocalRFMotionEasing.current
    return if (reduced) {
        fadeIn(animationSpec = tween(d.veryShort))
    } else {
        slideInHorizontally(
            animationSpec = tween(durationMillis = d.short, easing = e.decelerate),
            initialOffsetX = { full -> (full * 0.12f).toInt() }
        ) + fadeIn(animationSpec = tween(d.short))
    }
}

@Composable
fun rfExitSlideFade(): ExitTransition {
    val reduced = LocalRFReducedMotion.current
    val d = LocalRFMotionDurations.current
    val e = LocalRFMotionEasing.current
    return if (reduced) {
        fadeOut(animationSpec = tween(d.veryShort))
    } else {
        slideOutHorizontally(
            animationSpec = tween(durationMillis = d.short, easing = e.standard),
            targetOffsetX = { full -> -(full * 0.06f).toInt() }
        ) + fadeOut(animationSpec = tween(d.short))
    }
}

@Composable
fun rfEnterFade(): EnterTransition = fadeIn(animationSpec = tween(LocalRFMotionDurations.current.veryShort))

@Composable
fun rfExitFade(): ExitTransition = fadeOut(animationSpec = tween(LocalRFMotionDurations.current.veryShort))

@Composable
fun rfEnterScaleFade(): EnterTransition {
    val reduced = LocalRFReducedMotion.current
    val d = LocalRFMotionDurations.current
    return if (reduced) {
        fadeIn(tween(d.veryShort))
    } else {
        scaleIn(animationSpec = tween(d.short), initialScale = 0.96f) + fadeIn(tween(d.short))
    }
}

@Composable
fun rfExitScaleFade(): ExitTransition {
    val reduced = LocalRFReducedMotion.current
    val d = LocalRFMotionDurations.current
    return if (reduced) {
        fadeOut(tween(d.veryShort))
    } else {
        scaleOut(animationSpec = tween(d.short), targetScale = 0.98f) + fadeOut(tween(d.short))
    }
}

@Composable
fun rfSharedIn(): EnterTransition {
    val reduced = LocalRFReducedMotion.current
    val d = LocalRFMotionDurations.current
    return if (reduced) {
        fadeIn(tween(d.veryShort))
    } else {
        scaleIn(animationSpec = tween((d.short * 0.8f).toInt()), initialScale = 0.98f) +
                fadeIn(animationSpec = tween((d.short * 0.8f).toInt()))
    }
}

@Composable
fun rfSharedOut(): ExitTransition {
    val reduced = LocalRFReducedMotion.current
    val d = LocalRFMotionDurations.current
    return if (reduced) {
        fadeOut(tween(d.veryShort))
    } else {
        scaleOut(animationSpec = tween((d.short * 0.8f).toInt()), targetScale = 1.02f) +
                fadeOut(animationSpec = tween((d.short * 0.8f).toInt()))
    }
}
