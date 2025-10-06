package com.managr.app.feature.projects

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.managr.app.core.design.component.*
import com.managr.app.core.domain.model.Task
import com.managr.app.core.domain.model.TaskPhase
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailScreen(
    projectId: Long,
    onNavigateBack: () -> Unit,
    viewModel: ProjectDetailViewModel = hiltViewModel()
) {
    val project by viewModel.project.collectAsState()
    val tasks by viewModel.tasks.collectAsState()

    LaunchedEffect(projectId) {
        viewModel.loadProject(projectId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(project?.title ?: "Project") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        project?.let { proj ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    ArtworkHeader(proj)
                }

                item {
                    ProgressCard(proj)
                }

                item {
                    Text(
                        "Tasks (${proj.completedTasks}/${proj.totalTasks})",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                val groupedTasks = tasks.groupBy { it.phase }
                TaskPhase.orderedPhases().forEach { phase ->
                    val phaseTasks = groupedTasks[phase] ?: emptyList()
                    if (phaseTasks.isNotEmpty()) {
                        item {
                            Text(
                                phase.displayName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        items(phaseTasks) { task ->
                            TaskCard(
                                task = task,
                                onToggleComplete = { viewModel.toggleTaskCompletion(task.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ArtworkHeader(project: com.managr.app.core.domain.model.Project) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(MaterialTheme.shapes.large)
    ) {
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
    }
}

@Composable
private fun ProgressCard(project: com.managr.app.core.domain.model.Project) {
    GlassCard {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Progress", style = MaterialTheme.typography.titleMedium)
                Text(
                    "${project.completionPercentage.toInt()}%",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            LinearProgressIndicator(
                progress = project.completionPercentage / 100f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoChip("Release", project.releaseDate.format(DateTimeFormatter.ofPattern("MMM dd")))
                InfoChip("Type", project.type.displayName())
                InfoChip("Tracks", "${project.trackCount}")
            }
        }
    }
}

@Composable
private fun InfoChip(label: String, value: String) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label, style = MaterialTheme.typography.labelSmall)
            Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun TaskCard(
    task: Task,
    onToggleComplete: () -> Unit
) {
    RFCard(
        onClick = onToggleComplete,
        contentPadding = 12.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Checkbox(
                checked = task.status == com.managr.app.core.domain.model.TaskStatus.COMPLETED,
                onCheckedChange = { onToggleComplete() }
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = task.dueDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (task.isOverdue()) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.errorContainer
                ) {
                    Text(
                        "Overdue",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
    }
}
