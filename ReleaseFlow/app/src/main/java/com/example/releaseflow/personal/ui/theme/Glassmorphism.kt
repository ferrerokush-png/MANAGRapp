package com.example.releaseflow.personal.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

fun Modifier.glassmorphism(
    color: Color? = null,
    shadowElevation: Int = 8
): Modifier = composed {
    val glassColor = color ?: MaterialTheme.colorScheme.surface.copy(alpha = 0.1f)
    this
        .shadow(
            elevation = shadowElevation.dp,
            shape = RoundedCornerShape(20.dp),
            ambientColor = Color.Black.copy(alpha = 0.3f)
        )
        .background(
            color = glassColor,
            shape = RoundedCornerShape(20.dp)
        )
        .clip(RoundedCornerShape(20.dp))
}
