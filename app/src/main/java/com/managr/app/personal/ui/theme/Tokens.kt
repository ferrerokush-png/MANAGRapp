package com.managr.app.personal.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object RFSpacing {
    val xxs: Dp = 4.dp
    val xs: Dp = 8.dp
    val sm: Dp = 12.dp
    val md: Dp = 16.dp
    val lg: Dp = 20.dp
    val xl: Dp = 24.dp
    val xxl: Dp = 32.dp
}

object RFRadii {
    val sm: Dp = 8.dp
    val md: Dp = 12.dp
    val lg: Dp = 16.dp
    val xl: Dp = 20.dp
    val pill: Dp = 999.dp
}

object RFElevation {
    val low: Dp = 2.dp
    val mid: Dp = 6.dp
    val high: Dp = 12.dp
}

object RFBlur {
    val none: Dp = 0.dp
    val subtle: Dp = 6.dp
    val medium: Dp = 12.dp
    val heavy: Dp = 18.dp
}

object RFColors {
    val primary: Color
        @Composable get() = MaterialTheme.colorScheme.primary
    val onPrimary: Color
        @Composable get() = MaterialTheme.colorScheme.onPrimary
    val background: Color
        @Composable get() = MaterialTheme.colorScheme.background
    val surface: Color
        @Composable get() = MaterialTheme.colorScheme.surface
    val surfaceVariant: Color
        @Composable get() = MaterialTheme.colorScheme.surfaceVariant
    val onSurface: Color
        @Composable get() = MaterialTheme.colorScheme.onSurface
    val onSurfaceVariant: Color
        @Composable get() = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
    val success: Color
        @Composable get() = MaterialTheme.colorScheme.tertiary
    val warning: Color
        @Composable get() = MaterialTheme.colorScheme.secondary
    val error: Color
        @Composable get() = MaterialTheme.colorScheme.error
}
