package com.managr.app.personal.ui.screens.tasks

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.managr.app.core.design.component.*
import com.managr.app.personal.data.local.entity.ReleaseTask
import com.managr.app.personal.data.local.entity.TaskCategory
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TaskListScreen(
    projectId: Long,
    onNavigateBack: () -> Unit,
    viewModel: TaskListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val filterCategory by viewModel.selectedCategory.collectAsState()
    val filterCompleted by viewModel.showCompleted.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var showFilters by remember { mutableStateOf(false) }

    LaunchedEffect(projectId) {
        viewModel.loadTasks(projectId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tasks") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showFilters = !showFilters }) {
                        Icon(Icons.Default.FilterList, "Filter")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, "Add Task")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Filters
            AnimatedVisibility(
                visible = showFilters,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                TaskFilters(
                    selectedCategory = filterCategory,
                    showCompleted = filterCompleted,
                    onCategoryChange = viewModel::filterByCategory,
                    onShowCompletedChange = viewModel::toggleShowCompleted
                )
            }

            // Task list
            when (val state = uiState) {
                is TaskListUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        LoadingIndicator(message = "Loading tasks...")
                    }
                }
                is TaskListUiState.Success -> {
                    if (state.tasks.isEmpty()) {
                        EmptyTasksListState(onAddClick = { showAddDialog = true })
                    } else {
                        TaskList(
                            tasks = state.tasks,
                            onTaskClick = { /* pending: open task detail */ },
                            onToggleComplete = viewModel::toggleTaskComplete,
                            onDeleteTask = viewModel::deleteTask,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                is TaskListUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        GenericErrorState(
                            message = state.message,
                            onRetryClick = { viewModel.loadTasks(projectId) }
                        )
                    }
                }
            }
        }
    }

    // Add task dialog
    if (showAddDialog) {
        AddTaskDialog(
            projectId = projectId,
            onDismiss = { showAddDialog = false },
            onTaskAdded = {
                showAddDialog = false
                viewModel.loadTasks(projectId)
            }
        )
    }
}

@Composable
private fun TaskFilters(
    selectedCategory: TaskCategory?,
    showCompleted: Boolean,
    onCategoryChange: (TaskCategory?) -> Unit,
    onShowCompletedChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Filter by Category", style = MaterialTheme.typography.labelMedium)
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            FilterChip(
                selected = selectedCategory == null,
                onClick = { onCategoryChange(null) },
                label = { Text("All") }
            )
            TaskCategory.entries.forEach { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { onCategoryChange(if (selectedCategory == category) null else category) },
                    label = { Text(category.name) }
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Show Completed", style = MaterialTheme.typography.bodyMedium)
            Switch(
                checked = showCompleted,
                onCheckedChange = onShowCompletedChange
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TaskList(
    tasks: List<ReleaseTask>,
    onTaskClick: (ReleaseTask) -> Unit,
    onToggleComplete: (ReleaseTask) -> Unit,
    onDeleteTask: (ReleaseTask) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(tasks, key = { it.id }) { task ->
            TaskCard(
                task = task,
                onClick = { onTaskClick(task) },
                onToggleComplete = { onToggleComplete(task) },
                onDelete = { onDeleteTask(task) },
                modifier = Modifier.animateItemPlacement()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskCard(
    task: ReleaseTask,
    onClick: () -> Unit,
    onToggleComplete: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    val daysUntilDue = TimeUnit.MILLISECONDS.toDays(task.dueDate.time - Date().time).toInt()
    
    val dueDateColor = when {
        task.isCompleted -> MaterialTheme.colorScheme.outline
        daysUntilDue < 0 -> MaterialTheme.colorScheme.error
        daysUntilDue <= 3 -> Color(0xFFF59E0B) // Orange
        else -> MaterialTheme.colorScheme.primary
    }

    var showDeleteConfirm by remember { mutableStateOf(false) }

    GlassCard(
        onClick = onClick,
        modifier = modifier,
        contentPadding = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Checkbox
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onToggleComplete() }
            )

            // Task content
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Title
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
                    color = if (task.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                )

                // Description
                if (!task.description.isNullOrBlank()) {
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Metadata row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Category badge
                    StatusChip(
                        text = task.category.name,
                        color = StatusChipColor.INFO
                    )

                    // Priority stars
                    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        repeat(task.priority) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                modifier = Modifier.size(12.dp),
                                tint = Color(0xFFFBBF24)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Due date
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = dueDateColor.copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = dateFormat.format(task.dueDate),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = dueDateColor
                        )
                    }
                }

                // Assigned to
                if (!task.assignedTo.isNullOrBlank()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = task.assignedTo,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Delete button
            IconButton(onClick = { showDeleteConfirm = true }) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }

    // Delete confirmation
    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete Task") },
            text = { Text("Are you sure you want to delete this task?") },
            confirmButton = {
                TextButton(onClick = {
                    onDelete()
                    showDeleteConfirm = false
                }) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun EmptyTasksListState(
    onAddClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        EmptyTasksState(
            onAddClick = onAddClick
        )
    }
}
