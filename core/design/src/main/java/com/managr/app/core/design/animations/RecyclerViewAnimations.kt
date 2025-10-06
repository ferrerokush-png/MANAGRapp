package com.managr.app.core.design.animations

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Animation specifications for RecyclerView item animations
 */
object RecyclerViewAnimations {
    val ItemAnimationDuration = 300
    val StaggerDelay = 50
    val FadeInDuration = 200
    val SlideInDuration = 300
    val ScaleInDuration = 250
}

/**
 * Animated visibility for list items with staggered entrance
 */
@Composable
fun AnimatedListItem(
    visible: Boolean = true,
    index: Int = 0,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val delay = (index * RecyclerViewAnimations.StaggerDelay).toLong()
    
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            animationSpec = tween(RecyclerViewAnimations.SlideInDuration, delayMillis = delay.toInt()),
            initialOffsetY = { it / 4 }
        ) + fadeIn(
            animationSpec = tween(RecyclerViewAnimations.FadeInDuration, delayMillis = delay.toInt())
        ),
        exit = slideOutVertically(
            animationSpec = tween(RecyclerViewAnimations.ItemAnimationDuration)
        ) + fadeOut(
            animationSpec = tween(RecyclerViewAnimations.ItemAnimationDuration)
        ),
        modifier = modifier
    ) {
        content()
    }
}

/**
 * Animated list item with scale and fade
 */
@Composable
fun AnimatedScaleListItem(
    visible: Boolean = true,
    index: Int = 0,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val delay = (index * RecyclerViewAnimations.StaggerDelay).toLong()
    
    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(
            animationSpec = tween(
                RecyclerViewAnimations.ScaleInDuration,
                delayMillis = delay.toInt()
            )
        ) + fadeIn(
            animationSpec = tween(
                RecyclerViewAnimations.FadeInDuration,
                delayMillis = delay.toInt()
            )
        ),
        exit = scaleOut() + fadeOut(),
        modifier = modifier
    ) {
        content()
    }
}

/**
 * Animated list item with slide from right
 */
@Composable
fun AnimatedSlideListItem(
    visible: Boolean = true,
    index: Int = 0,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val delay = (index * RecyclerViewAnimations.StaggerDelay).toLong()
    
    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(
            animationSpec = tween(RecyclerViewAnimations.SlideInDuration, delayMillis = delay.toInt()),
            initialOffsetX = { it }
        ) + fadeIn(
            animationSpec = tween(RecyclerViewAnimations.FadeInDuration, delayMillis = delay.toInt())
        ),
        exit = slideOutHorizontally(
            animationSpec = tween(RecyclerViewAnimations.ItemAnimationDuration)
        ) + fadeOut(
            animationSpec = tween(RecyclerViewAnimations.ItemAnimationDuration)
        ),
        modifier = modifier
    ) {
        content()
    }
}

/**
 * Animated list item with expand/collapse effect
 */
@Composable
fun AnimatedExpandListItem(
    visible: Boolean = true,
    index: Int = 0,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val delay = (index * RecyclerViewAnimations.StaggerDelay).toLong()
    
    AnimatedVisibility(
        visible = visible,
        enter = expandVertically(
            animationSpec = tween(RecyclerViewAnimations.ItemAnimationDuration),
            expandFrom = androidx.compose.ui.Alignment.Top
        ) + fadeIn(
            animationSpec = tween(RecyclerViewAnimations.FadeInDuration)
        ),
        exit = shrinkVertically(
            animationSpec = tween(RecyclerViewAnimations.ItemAnimationDuration)
        ) + fadeOut(
            animationSpec = tween(RecyclerViewAnimations.ItemAnimationDuration)
        ),
        modifier = modifier
    ) {
        content()
    }
}

/**
 * Animated list item with rotation effect
 */
@Composable
fun AnimatedRotateListItem(
    visible: Boolean = true,
    index: Int = 0,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val delay = (index * RecyclerViewAnimations.StaggerDelay).toLong()
    
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            animationSpec = tween(RecyclerViewAnimations.SlideInDuration),
            initialOffsetY = { it / 2 }
        ) + fadeIn(
            animationSpec = tween(RecyclerViewAnimations.FadeInDuration)
        ),
        exit = slideOutVertically(
            animationSpec = tween(RecyclerViewAnimations.ItemAnimationDuration)
        ) + fadeOut(
            animationSpec = tween(RecyclerViewAnimations.ItemAnimationDuration)
        ),
        modifier = modifier
    ) {
        content()
    }
}

/**
 * Animated list item with bounce effect
 */
@Composable
fun AnimatedBounceListItem(
    visible: Boolean = true,
    index: Int = 0,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val delay = (index * RecyclerViewAnimations.StaggerDelay).toLong()
    
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ),
            initialOffsetY = { it }
        ) + fadeIn(
            animationSpec = tween(RecyclerViewAnimations.FadeInDuration)
        ),
        exit = slideOutVertically(
            animationSpec = tween(RecyclerViewAnimations.ItemAnimationDuration)
        ) + fadeOut(
            animationSpec = tween(RecyclerViewAnimations.ItemAnimationDuration)
        ),
        modifier = modifier
    ) {
        content()
    }
}

/**
 * Animated list item with elastic effect
 */
@Composable
fun AnimatedElasticListItem(
    visible: Boolean = true,
    index: Int = 0,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val delay = (index * RecyclerViewAnimations.StaggerDelay).toLong()
    
    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessMedium
            )
        ) + fadeIn(
            animationSpec = tween(RecyclerViewAnimations.FadeInDuration)
        ),
        exit = scaleOut() + fadeOut(),
        modifier = modifier
    ) {
        content()
    }
}

/**
 * Animated list item with wave effect
 */
@Composable
fun AnimatedWaveListItem(
    visible: Boolean = true,
    index: Int = 0,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val delay = (index * RecyclerViewAnimations.StaggerDelay).toLong()
    
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            animationSpec = tween(RecyclerViewAnimations.SlideInDuration, delayMillis = delay.toInt()),
            initialOffsetY = { (it * 0.3f).toInt() }
        ) + fadeIn(
            animationSpec = tween(RecyclerViewAnimations.FadeInDuration, delayMillis = delay.toInt())
        ),
        exit = slideOutVertically(
            animationSpec = tween(RecyclerViewAnimations.ItemAnimationDuration)
        ) + fadeOut(
            animationSpec = tween(RecyclerViewAnimations.ItemAnimationDuration)
        ),
        modifier = modifier
    ) {
        content()
    }
}

/**
 * Animated list item with zoom effect
 */
@Composable
fun AnimatedZoomListItem(
    visible: Boolean = true,
    index: Int = 0,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val delay = (index * RecyclerViewAnimations.StaggerDelay).toLong()
    
    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(
            animationSpec = tween(RecyclerViewAnimations.ScaleInDuration, delayMillis = delay.toInt()),
            initialScale = 0.8f
        ) + fadeIn(
            animationSpec = tween(RecyclerViewAnimations.FadeInDuration, delayMillis = delay.toInt())
        ),
        exit = scaleOut(
            animationSpec = tween(RecyclerViewAnimations.ItemAnimationDuration)
        ) + fadeOut(
            animationSpec = tween(RecyclerViewAnimations.ItemAnimationDuration)
        ),
        modifier = modifier
    ) {
        content()
    }
}

/**
 * Animated list item with flip effect
 */
@Composable
fun AnimatedFlipListItem(
    visible: Boolean = true,
    index: Int = 0,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val delay = (index * RecyclerViewAnimations.StaggerDelay).toLong()
    
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            animationSpec = tween(RecyclerViewAnimations.SlideInDuration, delayMillis = delay.toInt()),
            initialOffsetY = { -it / 2 }
        ) + fadeIn(
            animationSpec = tween(RecyclerViewAnimations.FadeInDuration, delayMillis = delay.toInt())
        ),
        exit = slideOutVertically(
            animationSpec = tween(RecyclerViewAnimations.ItemAnimationDuration)
        ) + fadeOut(
            animationSpec = tween(RecyclerViewAnimations.ItemAnimationDuration)
        ),
        modifier = modifier
    ) {
        content()
    }
}

/**
 * Animated list item with slide from bottom
 */
@Composable
fun AnimatedSlideUpListItem(
    visible: Boolean = true,
    index: Int = 0,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val delay = (index * RecyclerViewAnimations.StaggerDelay).toLong()
    
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            animationSpec = tween(RecyclerViewAnimations.SlideInDuration, delayMillis = delay.toInt()),
            initialOffsetY = { it }
        ) + fadeIn(
            animationSpec = tween(RecyclerViewAnimations.FadeInDuration, delayMillis = delay.toInt())
        ),
        exit = slideOutVertically(
            animationSpec = tween(RecyclerViewAnimations.ItemAnimationDuration)
        ) + fadeOut(
            animationSpec = tween(RecyclerViewAnimations.ItemAnimationDuration)
        ),
        modifier = modifier
    ) {
        content()
    }
}

/**
 * Animated list item with slide from top
 */
@Composable
fun AnimatedSlideDownListItem(
    visible: Boolean = true,
    index: Int = 0,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val delay = (index * RecyclerViewAnimations.StaggerDelay).toLong()
    
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            animationSpec = tween(RecyclerViewAnimations.SlideInDuration, delayMillis = delay.toInt()),
            initialOffsetY = { -it }
        ) + fadeIn(
            animationSpec = tween(RecyclerViewAnimations.FadeInDuration, delayMillis = delay.toInt())
        ),
        exit = slideOutVertically(
            animationSpec = tween(RecyclerViewAnimations.ItemAnimationDuration)
        ) + fadeOut(
            animationSpec = tween(RecyclerViewAnimations.ItemAnimationDuration)
        ),
        modifier = modifier
    ) {
        content()
    }
}
