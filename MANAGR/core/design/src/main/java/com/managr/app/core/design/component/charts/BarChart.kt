package com.managr.app.core.design.component.charts

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun BarChart(
    data: List<Pair<String, Float>>,
    modifier: Modifier = Modifier,
    colors: List<Color> = listOf(MaterialTheme.colorScheme.primary),
    title: String? = null
) {
    var animationPlayed by remember { mutableStateOf(false) }
    val animationProgress by animateFloatAsState(
        targetValue = if (animationPlayed) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "bar_chart_animation"
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

            val maxValue = data.maxOf { it.second }
            val barWidth = (size.width / data.size) * 0.6f
            val spacing = size.width / data.size

            data.forEachIndexed { index, (_, value) ->
                val barHeight = (value / maxValue) * size.height * 0.8f * animationProgress
                val x = index * spacing + (spacing - barWidth) / 2
                val y = size.height - barHeight

                val color = colors.getOrElse(index % colors.size) { colors.first() }

                drawRoundRect(
                    color = color,
                    topLeft = Offset(x, y),
                    size = Size(barWidth, barHeight),
                    cornerRadius = CornerRadius(8.dp.toPx())
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            data.forEach { (label, _) ->
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}
