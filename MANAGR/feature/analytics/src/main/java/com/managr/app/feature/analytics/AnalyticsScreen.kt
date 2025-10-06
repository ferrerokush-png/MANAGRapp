package com.managr.app.feature.analytics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.managr.app.core.design.component.LoadingIndicator
import com.managr.app.core.design.component.EmptyAnalyticsState
import com.managr.app.core.design.component.GenericErrorState
import com.managr.app.core.design.component.RFCard
import com.managr.app.core.design.component.PrimaryButton
import com.managr.app.core.design.component.MetricCard
import com.managr.app.core.design.component.GlassCard
import com.managr.app.core.design.component.RFTextField
import com.managr.app.core.design.component.RFTextButton

sealed class AnalyticsUiState {
    object Loading : AnalyticsUiState()
    data class Success(val metrics: AnalyticsMetrics) : AnalyticsUiState()
    data class Error(val message: String) : AnalyticsUiState()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    onBackClick: () -> Unit = {},
    viewModel: AnalyticsViewModel = hiltViewModel()
) {
    var showAddDataDialog by remember { mutableStateOf(false) }
    var selectedTimePeriod by remember { mutableStateOf(TimePeriod.DAYS_30) }
    val metrics by viewModel.metrics.collectAsState()
    val uiState = AnalyticsUiState.Success(metrics)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Analytics") },
                actions = {
                    IconButton(onClick = { showAddDataDialog = true }) {
                        Icon(Icons.Default.Add, "Add Data")
                    }
                    IconButton(onClick = { /* Export */ }) {
                        Icon(Icons.Default.Download, "Export")
                    }
                }
            )
        }
    ) { padding ->
        when (uiState) {
            is AnalyticsUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingIndicator(message = "Loading analytics...")
                }
            }
            is AnalyticsUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    GenericErrorState(
                        message = uiState.message,
                        onRetryClick = { viewModel.loadMetrics(selectedTimePeriod) }
                    )
                }
            }
            is AnalyticsUiState.Success -> {
                AnalyticsContent(
                    metrics = uiState.metrics,
                    selectedTimePeriod = selectedTimePeriod,
                    onTimePeriodChange = { 
                        selectedTimePeriod = it
                        viewModel.loadMetrics(it)
                    },
                    onAddDataClick = { showAddDataDialog = true },
                    modifier = Modifier.fillMaxSize().padding(padding)
                )
            }
        }

        if (showAddDataDialog) {
            AddAnalyticsDataDialog(
                onDismiss = { showAddDataDialog = false },
                onDataAdded = {
                    showAddDataDialog = false
                    viewModel.loadMetrics(selectedTimePeriod)
                }
            )
        }
    }
}

@Composable
private fun AnalyticsContent(
    metrics: AnalyticsMetrics,
    selectedTimePeriod: TimePeriod,
    onTimePeriodChange: (TimePeriod) -> Unit,
    onAddDataClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Time period selector
            item {
                TimePeriodSelector(
                    selected = selectedTimePeriod,
                    onSelected = onTimePeriodChange
                )
            }

            // Key metrics
            item {
                Text(
                    "Key Metrics",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricCard(
                        title = "Total Streams",
                        value = metrics.totalStreams.formatNumber(),
                        subtitle = metrics.streamsChange,
                        icon = Icons.Default.PlayArrow,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Followers",
                        value = metrics.totalFollowers.formatNumber(),
                        subtitle = metrics.followersChange,
                        icon = Icons.Default.People,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricCard(
                        title = "Engagement",
                        value = "${metrics.engagementRate}%",
                        subtitle = metrics.engagementChange,
                        icon = Icons.Default.Favorite,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Revenue",
                        value = "$${metrics.revenue.formatNumber()}",
                        subtitle = metrics.revenueChange,
                        icon = Icons.Default.AttachMoney,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Platform breakdown
            item {
                Text(
                    "Platform Breakdown",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            items(metrics.platformData) { platform ->
                PlatformCard(platform = platform)
            }

            // Charts placeholder
            item {
                Text(
                    "Streams Over Time",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                GlassCard(contentPadding = 16.dp) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Chart visualization coming soon",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            if (metrics.isEmpty()) {
                item {
                    EmptyAnalyticsState(
                        onAddDataClick = onAddDataClick
                    )
                }
            }
        }
}

@Composable
private fun TimePeriodSelector(
    selected: TimePeriod,
    onSelected: (TimePeriod) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TimePeriod.entries.forEach { period ->
            FilterChip(
                selected = selected == period,
                onClick = { onSelected(period) },
                label = { Text(period.label) }
            )
        }
    }
}

@Composable
private fun PlatformCard(platform: PlatformData) {
    RFCard(contentPadding = 12.dp) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = platform.color.copy(alpha = 0.1f),
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = platform.name.first().toString(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = platform.color
                        )
                    }
                }

                Column {
                    Text(
                        text = platform.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "${platform.streams.formatNumber()} streams",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${platform.percentage}%",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = platform.color
                )
                Text(
                    text = platform.trend,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (platform.trend.startsWith("+")) Color(0xFF10B981) else MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun AddAnalyticsDataDialog(
    onDismiss: () -> Unit,
    onDataAdded: () -> Unit
) {
    var platform by remember { mutableStateOf("Spotify") }
    var metricType by remember { mutableStateOf("Streams") }
    var value by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Analytics Data") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                RFTextField(
                    value = platform,
                    onValueChange = { platform = it },
                    label = "Platform",
                    placeholder = "Spotify, Apple Music, etc."
                )
                RFTextField(
                    value = metricType,
                    onValueChange = { metricType = it },
                    label = "Metric Type",
                    placeholder = "Streams, Followers, etc."
                )
                RFTextField(
                    value = value,
                    onValueChange = { value = it },
                    label = "Value",
                    placeholder = "Enter number"
                )
            }
        },
        confirmButton = {
            PrimaryButton(
                text = "Add",
                onClick = onDataAdded,
                enabled = platform.isNotBlank() && value.isNotBlank()
            )
        },
        dismissButton = {
            RFTextButton(text = "Cancel", onClick = onDismiss)
        }
    )
}

enum class TimePeriod(val label: String) {
    DAYS_7("7D"),
    DAYS_30("30D"),
    DAYS_90("90D"),
    ALL_TIME("All")
}

data class AnalyticsMetrics(
    val totalStreams: Long = 0,
    val streamsChange: String = "+0%",
    val totalFollowers: Long = 0,
    val followersChange: String = "+0%",
    val engagementRate: Double = 0.0,
    val engagementChange: String = "+0%",
    val revenue: Double = 0.0,
    val revenueChange: String = "+0%",
    val platformData: List<PlatformData> = emptyList()
) {
    fun isEmpty() = totalStreams == 0L && totalFollowers == 0L
}

data class PlatformData(
    val name: String,
    val streams: Long,
    val percentage: Int,
    val trend: String,
    val color: Color
)

private fun Long.formatNumber(): String {
    return when {
        this >= 1_000_000 -> "%.1fM".format(this / 1_000_000.0)
        this >= 1_000 -> "%.1fK".format(this / 1_000.0)
        else -> this.toString()
    }
}

private fun Double.formatNumber(): String {
    return when {
        this >= 1_000_000 -> "%.1fM".format(this / 1_000_000.0)
        this >= 1_000 -> "%.1fK".format(this / 1_000.0)
        else -> "%.2f".format(this)
    }
}
