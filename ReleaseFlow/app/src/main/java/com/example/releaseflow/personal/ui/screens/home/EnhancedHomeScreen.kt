package com.example.releaseflow.personal.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.releaseflow.core.design.component.*
import com.example.releaseflow.core.domain.model.Project
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedHomeScreen(
    onProjectsClick: () -> Unit,
    onAnalyticsClick: () -> Unit,
    onAIAssistantClick: () -> Unit,
    onCreateProjectClick: () -> Unit,
    onCalendarClick: () -> Unit = {},
    onHubClick: () -> Unit = {}
) {
    val greeting = when (LocalTime.now().hour) {
        in 0..11 -> "Good morning"
        in 12..17 -> "Good afternoon"
        else -> "Good evening"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.MusicNote, null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(8.dp))
                        Text("Release Flow", fontWeight = FontWeight.Bold)
                    }
                },
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Default.Search, "Search") }
                    IconButton(onClick = {}) { Icon(Icons.Default.Person, "Profile") }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    "$greeting, Artist",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                PrimaryButton(
                    text = "New Release",
                    onClick = onCreateProjectClick,
                    icon = Icons.Default.Add,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricCard("Releases", "3", Icons.Default.Album, Modifier.weight(1f), onProjectsClick)
                    MetricCard("Deadlines", "5", Icons.Default.Event, Modifier.weight(1f), onCalendarClick)
                }
            }

            item {
                Text("Active Projects", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(3) { index ->
                        ProjectThumbnail(
                            title = "Project ${index + 1}",
                            progress = (index + 1) * 25f
                        )
                    }
                }
            }

            item {
                GlassCard {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Lightbulb, null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(Modifier.width(8.dp))
                            Text("Quick Insight", fontWeight = FontWeight.SemiBold)
                        }
                        Text(
                            "Your latest single is performing well on Spotify. Consider creating more TikTok content to boost engagement.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            item {
                Text("Quick Actions", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ActionButton("AI Assistant", Icons.Default.SmartToy, onAIAssistantClick, Modifier.weight(1f))
                    ActionButton("Analytics", Icons.Default.Analytics, onAnalyticsClick, Modifier.weight(1f))
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ActionButton("Playlists", Icons.Default.QueueMusic, onHubClick, Modifier.weight(1f))
                    ActionButton("Revenue", Icons.Default.AttachMoney, onAnalyticsClick, Modifier.weight(1f))
                }
            }

            item {
                Text("Upcoming Deadlines", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }

            items(3) { index ->
                DeadlineCard(
                    title = "Upload to DistroKid",
                    date = "Oct ${9 + index}, 2025",
                    daysLeft = 3 - index
                )
            }
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    GlassCard(onClick = onClick, modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
            Column {
                Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text(title, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
private fun ProjectThumbnail(title: String, progress: Float) {
    GlassCard(
        modifier = Modifier
            .width(160.dp)
            .aspectRatio(1f),
        contentPadding = 0.dp
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondary
                            )
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                Text("${progress.toInt()}% complete", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.8f))
            }
        }
    }
}

@Composable
private fun ActionButton(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassCard(onClick = onClick, modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
            Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun DeadlineCard(title: String, date: String, daysLeft: Int) {
    RFCard(contentPadding = 12.dp) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                Text(date, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Surface(
                shape = CircleShape,
                color = when {
                    daysLeft <= 1 -> MaterialTheme.colorScheme.errorContainer
                    daysLeft <= 3 -> MaterialTheme.colorScheme.tertiaryContainer
                    else -> MaterialTheme.colorScheme.primaryContainer
                }
            ) {
                Text(
                    "${daysLeft}d",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
