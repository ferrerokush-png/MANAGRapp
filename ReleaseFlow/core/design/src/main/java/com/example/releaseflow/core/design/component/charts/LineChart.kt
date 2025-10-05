package com.example.releaseflow.core.design.component.charts

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun LineChart(
    data: List<Float>,
    labels: List<String>,
    modifier: Modifier = Modifier,
    lineColor: Color = MaterialTheme.colorScheme.primary,
    title: String? = null
) {
    var animationPlayed by remember { mutableStateOf(false) }
    val animationProgress by animateFloatAsState(
        targetValue = if (animationPlayed) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "line_chart_animation"
    )

    LaunchedEffect(Unit) {
        animationPlayed = true
    }

    Column(modifier = modifier) {
        if (title != null) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            if (data.isEmpty()) return@Canvas

            val maxValue = data.maxOrNull() ?: 1f
            val minValue = data.minOrNull() ?: 0f
            val range = maxValue - minValue
            val spacing = size.width / (data.size - 1).coerceAtLeast(1)

            val path = Path()
            data.forEachIndexed { index, value ->
                val x = index * spacing
                val y = size.height - ((value - minValue) / range.coerceAtLeast(0.1f)) * size.height * 0.8f - size.height * 0.1f

                if (index == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
            }

            drawPath(
                path = path,
                color = lineColor,
                style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round),
                alpha = animationProgress
            )

            data.forEachIndexed { index, value ->
                val x = index * spacing
                val y = size.height - ((value - minValue) / range.coerceAtLeast(0.1f)) * size.height * 0.8f - size.height * 0.1f

                drawCircle(
                    color = lineColor,
                    radius = 6.dp.toPx(),
                    center = Offset(x, y),
                    alpha = animationProgress
                )
            }
        }
    }
}
