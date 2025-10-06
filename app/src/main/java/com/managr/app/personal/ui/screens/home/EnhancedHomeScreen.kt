package com.managr.app.personal.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.managr.app.core.design.component.GlassCard
import com.managr.app.core.design.component.PrimaryButton
import com.managr.app.core.design.component.LoadingIndicator
import com.managr.app.core.design.component.GenericErrorState
import com.managr.app.core.design.component.EmptyProjectsState
import com.managr.app.core.design.component.RFCard
import com.managr.app.core.domain.model.Project
import com.managr.app.core.domain.model.Task
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedHomeScreen(
    onProjectsClick: () -> Unit,
    onAnalyticsClick: () -> Unit,
    onAIAssistantClick: () -> Unit,
    onCreateProjectClick: () -> Unit,
    onCalendarClick: () -> Unit = {},
    onHubClick: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val greeting = when (LocalTime.now().hour) {
        in 0..11 -> "Good morning"
        in 12..17 -> "Good afternoon"
        else -> "Good evening"
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.MusicNote, null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(8.dp))
                        Text("MANAGR", fontWeight = FontWeight.Bold)
                    }
                },
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Default.Search, "Search") }
                }
            )
        }
    ) { padding ->
        when {
            uiState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    LoadingIndicator(message = "Loading...")
                }
            }
            uiState.error != null -> {
                Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    GenericErrorState(
                        message = uiState.error!!,
                        onRetryClick = { viewModel.refreshData() }
                    )
                }
            }
            else -> {
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
                            MetricCard(
                                "Releases", 
                                "${uiState.activeProjects.size}", 
                                Icons.Default.Album, 
                                Modifier.weight(1f), 
                                onProjectsClick
                            )
                            MetricCard(
                                "Tasks", 
                                "${uiState.pendingTasksCount}", 
                                Icons.Default.Event, 
                                Modifier.weight(1f), 
                                onCalendarClick
                            )
                        }
                    }

                    if (uiState.activeProjects.isNotEmpty()) {
                        item {
                            Text("Active Projects", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        }

                        item {
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(uiState.activeProjects, key = { it.id }) { project ->
                                    ProjectThumbnail(
                                        project = project,
                                        modifier = Modifier
                                    )
                                }
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
                            ActionButton("Playlists", Icons.AutoMirrored.Filled.QueueMusic, onHubClick, Modifier.weight(1f))
                            ActionButton("Projects", Icons.Default.Folder, onProjectsClick, Modifier.weight(1f))
                        }
                    }

                    if (uiState.upcomingDeadlines.isNotEmpty()) {
                        item {
                            Text("Upcoming Deadlines", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        }

                        items(uiState.upcomingDeadlines) { task ->
                            DeadlineCard(task = task)
                        }
                    }

                    if (uiState.activeProjects.isEmpty() && uiState.upcomingDeadlines.isEmpty()) {
                        item {
                            EmptyProjectsState(
                                onCreateClick = onCreateProjectClick
                            )
                        }
                    }
                }
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
private fun ProjectThumbnail(
    project: Project,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier
            .width(160.dp)
            .aspectRatio(1f),
        contentPadding = 0.dp
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (project.artworkUri != null) {
                AsyncImage(
                    model = project.artworkUri,
                    contentDescription = project.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
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
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {
                Text(
                    project.title, 
                    style = MaterialTheme.typography.titleMedium, 
                    fontWeight = FontWeight.Bold, 
                    color = Color.White,
                    maxLines = 1
                )
                Text(
                    project.type.name, 
                    style = MaterialTheme.typography.bodySmall, 
                    color = Color.White.copy(alpha = 0.8f)
                )
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
private fun DeadlineCard(task: Task) {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    val daysLeft = java.time.temporal.ChronoUnit.DAYS.between(java.time.LocalDate.now(), task.dueDate).toInt()
    
    RFCard(contentPadding = 12.dp) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(task.title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                Text(
                    dateFormat.format(
                        Date.from(
                            task.dueDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()
                        )
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
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
