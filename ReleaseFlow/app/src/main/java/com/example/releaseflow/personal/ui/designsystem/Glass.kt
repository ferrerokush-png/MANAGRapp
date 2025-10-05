package com.example.releaseflow.personal.ui.designsystem

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.releaseflow.personal.ui.theme.LocalRFShapesTokens
import com.example.releaseflow.personal.ui.theme.RFBlur
import com.example.releaseflow.personal.ui.theme.RFElevation

@Composable
fun GlassSurface(
    modifier: Modifier = Modifier,
    tonalAlpha: Float = 0.12f,
    blurRadius: Dp = RFBlur.subtle,
    shadowElevation: Dp = RFElevation.low,
    content: @Composable () -> Unit,
) {
    val shapeTokens = LocalRFShapesTokens.current
    val shape = RoundedCornerShape(shapeTokens.largeRadius.dp)
    val glassColor = MaterialTheme.colorScheme.surface.copy(alpha = tonalAlpha)
    val borderBrush = Brush.linearGradient(
        listOf(
            Color.White.copy(alpha = 0.12f),
            Color.White.copy(alpha = 0.04f)
        )
    )

    Surface(
        modifier = modifier
            .clip(shape)
            .blur(blurRadius, edgeTreatment = BlurredEdgeTreatment.Unbounded)
            .border(1.dp, borderBrush, shape)
            .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
        color = glassColor,
        shadowElevation = shadowElevation,
        shape = shape,
    ) {
        Box { content() }
    }
}

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    tonalAlpha: Float = 0.14f,
    blurRadius: Dp = RFBlur.subtle,
    elevation: Dp = RFElevation.mid,
    content: @Composable () -> Unit,
) {
    val shapeTokens = LocalRFShapesTokens.current
    val shape = RoundedCornerShape(shapeTokens.mediumRadius.dp)
    val glassColor = MaterialTheme.colorScheme.surface.copy(alpha = tonalAlpha)

    Card(
        modifier = modifier
            .clip(shape)
            .blur(blurRadius, edgeTreatment = BlurredEdgeTreatment.Unbounded),
        colors = CardDefaults.cardColors(containerColor = glassColor),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        shape = shape,
    ) {
        content()
    }
}

