package com.managr.app.core.design.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Status chip colors
 */
enum class StatusChipColor {
    SUCCESS, WARNING, ERROR, INFO, NEUTRAL
}

/**
 * Status chip component - Colored chips for project/task status
 * 
 * @param text Status text
 * @param color Chip color variant
 * @param modifier Modifier
 */
@Composable
fun StatusChip(
    text: String,
    color: StatusChipColor = StatusChipColor.INFO,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor) = when (color) {
        StatusChipColor.SUCCESS -> Color(0xFF10B981) to Color.White
        StatusChipColor.WARNING -> Color(0xFFF59E0B) to Color.White
        StatusChipColor.ERROR -> Color(0xFFEF4444) to Color.White
        StatusChipColor.INFO -> MaterialTheme.colorScheme.primary to MaterialTheme.colorScheme.onPrimary
        StatusChipColor.NEUTRAL -> MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        shadowElevation = 2.dp
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )
    }
}

/**
 * Status chip with custom colors
 */
@Composable
fun StatusChip(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        shadowElevation = 2.dp
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )
    }
}
