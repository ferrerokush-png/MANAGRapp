package com.managr.app.core.design.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.managr.app.core.design.animation.smoothScale

/**
 * MANAGR Card - Standard card with elevation variants
 *
 * @param modifier Modifier
 * @param onClick Optional click handler
 * @param elevation Card elevation
 * @param contentPadding Content padding
 * @param enabled Whether card is enabled for interaction
 * @param content Card content
 */
@Composable
fun RFCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    elevation: Dp = 4.dp,
    contentPadding: Dp = 16.dp,
    enabled: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = modifier.smoothScale(enabled = enabled),
            enabled = enabled,
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = elevation
            )
        ) {
            Column(
                modifier = Modifier.padding(contentPadding),
                content = content
            )
        }
    } else {
        Card(
            modifier = modifier,
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = elevation
            )
        ) {
            Column(
                modifier = Modifier.padding(contentPadding),
                content = content
            )
        }
    }
}

/**
 * Elevated card variant
 */
@Composable
fun ElevatedRFCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    contentPadding: Dp = 16.dp,
    enabled: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    RFCard(
        modifier = modifier,
        onClick = onClick,
        elevation = 8.dp,
        contentPadding = contentPadding,
        enabled = enabled,
        content = content
    )
}

/**
 * Flat card variant (no elevation)
 */
@Composable
fun FlatRFCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    contentPadding: Dp = 16.dp,
    enabled: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    RFCard(
        modifier = modifier,
        onClick = onClick,
        elevation = 0.dp,
        contentPadding = contentPadding,
        enabled = enabled,
        content = content
    )
}
