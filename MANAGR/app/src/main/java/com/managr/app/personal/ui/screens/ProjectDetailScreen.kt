package com.managr.app.personal.ui.screens

import android.app.DatePickerDialog
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.managr.app.personal.data.local.entity.ReleaseTask
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar

@Composable
fun ProjectDetailScreen(
    viewModel: ProjectDetailViewModel = hiltViewModel()
) {
    val project by viewModel.project.collectAsState()
    val tasks by viewModel.tasks.collectAsState()

    val pickMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.updateArtwork(uri.toString())
        }
    }

    val showAddDialog = remember { mutableStateOf(false) }
    val editTarget = remember { mutableStateOf<ReleaseTask?>(null) }
    val deleteTarget = remember { mutableStateOf<ReleaseTask?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog.value = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Task")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Header
            Text(
                text = project?.title ?: "Loading…",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.padding(top = 4.dp))
            Text(
                text = project?.let { "Release: ${formatDate(it.releaseDate.time)}" } ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Distributor upload deadline: 21 days before release
            project?.let { proj ->
                val zone = ZoneId.systemDefault()
                val releaseDate = Instant.ofEpochMilli(proj.releaseDate.time).atZone(zone).toLocalDate()
                val uploadDeadline = releaseDate.minusDays(21)
                val uploadDeadlineMillis = uploadDeadline.atStartOfDay(zone).toInstant().toEpochMilli()
                Spacer(Modifier.padding(top = 4.dp))
                Text(
                    text = "Distributor Upload Deadline: ${formatDate(uploadDeadlineMillis)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            // Artwork section
            Spacer(Modifier.padding(top = 12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AsyncImage(
                    model = project?.artworkPath,
                    contentDescription = "Project Artwork",
                    modifier = Modifier
                        .size(96.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Button(onClick = {
                    pickMediaLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }) {
                    Text(if (project?.artworkPath.isNullOrBlank()) "Add Artwork" else "Change Artwork")
                }
            }

            Spacer(Modifier.padding(top = 12.dp))
            Divider()
            Spacer(Modifier.padding(top = 12.dp))

            // Tasks list
            Text(
                text = "Tasks",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.padding(top = 8.dp))

            TaskList(
                tasks = tasks,
                onToggle = { task, checked -> viewModel.toggleTaskCompletion(task, checked) },
                onEdit = { task -> editTarget.value = task },
                onDelete = { task -> deleteTarget.value = task }
            )
        }

        if (showAddDialog.value) {
            AddTaskDialog(
                onDismiss = { showAddDialog.value = false },
                onSave = { title, desc, deadlineMillis ->
                    viewModel.addTask(title, desc, deadlineMillis)
                    showAddDialog.value = false
                }
            )
        }

        editTarget.value?.let { task ->
            EditTaskDialog(
                task = task,
                onDismiss = { editTarget.value = null },
                onSave = { newTitle, newDesc, newDeadline ->
                    viewModel.updateTask(task, newTitle, newDesc, newDeadline)
                    editTarget.value = null
                }
            )
        }

        deleteTarget.value?.let { task ->
            ConfirmDeleteDialog(
                task = task,
                onDismiss = { deleteTarget.value = null },
                onConfirm = {
                    viewModel.deleteTask(task)
                    deleteTarget.value = null
                }
            )
        }
    }
}

@Composable
private fun TaskList(
    tasks: List<ReleaseTask>,
    onToggle: (ReleaseTask, Boolean) -> Unit,
    onEdit: (ReleaseTask) -> Unit,
    onDelete: (ReleaseTask) -> Unit
) {
    if (tasks.isEmpty()) {
        Text("No tasks yet.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        return
    }
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(tasks, key = { it.id }) { task ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = { checked -> onToggle(task, checked) }
                )
                Spacer(Modifier.padding(start = 8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        task.title,
                        style = MaterialTheme.typography.bodyLarge,
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    )
                    Text(
                        text = "Due: ${formatDate(task.dueDate.time)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = { onEdit(task) }) { Icon(Icons.Filled.Edit, contentDescription = "Edit Task") }
                IconButton(onClick = { onDelete(task) }) { Icon(Icons.Filled.Delete, contentDescription = "Delete Task") }
            }
            Divider()
        }
    }
}

@Composable
private fun AddTaskDialog(
    onDismiss: () -> Unit,
    onSave: (title: String, description: String, deadlineMillis: Long) -> Unit
) {
    val context = LocalContext.current
    val title = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }

    val today = remember { LocalDate.now() }
    val todayStartMillis = remember { today.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() }
    val deadlineMillis = remember { mutableStateOf(todayStartMillis) }

    val calendar = remember(deadlineMillis.value) { Calendar.getInstance().apply { timeInMillis = deadlineMillis.value } }

    val datePicker = remember(calendar, todayStartMillis) {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val picked = LocalDate.of(year, month + 1, dayOfMonth)
                deadlineMillis.value = picked.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply { datePicker.minDate = todayStartMillis }
    }

    val isPast = deadlineMillis.value < todayStartMillis

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Task") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = title.value,
                    onValueChange = { title.value = it },
                    label = { Text("Task Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.padding(top = 12.dp))
                OutlinedTextField(
                    value = description.value,
                    onValueChange = { description.value = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.padding(top = 12.dp))
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Text("Deadline: ${formatDate(deadlineMillis.value)}")
                    Spacer(Modifier.weight(1f))
                    Button(onClick = { datePicker.show() }) { Text("Pick Date") }
                }
                if (isPast) {
                    Spacer(Modifier.padding(top = 8.dp))
                    Text("Deadline cannot be in the past", color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(title.value, description.value, deadlineMillis.value) },
                enabled = title.value.isNotBlank() && !isPast
            ) { Text("Save") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
private fun EditTaskDialog(
    task: ReleaseTask,
    onDismiss: () -> Unit,
    onSave: (title: String, description: String, deadlineMillis: Long) -> Unit
) {
    val context = LocalContext.current
    val title = remember { mutableStateOf(task.title) }
    val description = remember { mutableStateOf(task.description ?: "") }

    val today = remember { LocalDate.now() }
    val todayStartMillis = remember { today.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() }
    val deadlineMillis = remember { mutableStateOf(task.dueDate.time) }

    val calendar = remember(deadlineMillis.value) { Calendar.getInstance().apply { timeInMillis = deadlineMillis.value } }

    val datePicker = remember(calendar, todayStartMillis) {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val picked = LocalDate.of(year, month + 1, dayOfMonth)
                deadlineMillis.value = picked.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply { datePicker.minDate = todayStartMillis }
    }

    val isPast = deadlineMillis.value < todayStartMillis

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Task") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = title.value,
                    onValueChange = { title.value = it },
                    label = { Text("Task Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.padding(top = 12.dp))
                OutlinedTextField(
                    value = description.value,
                    onValueChange = { description.value = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.padding(top = 12.dp))
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Text("Deadline: ${formatDate(deadlineMillis.value)}")
                    Spacer(Modifier.weight(1f))
                    Button(onClick = { datePicker.show() }) { Text("Pick Date") }
                }
                if (isPast) {
                    Spacer(Modifier.padding(top = 8.dp))
                    Text("Deadline cannot be in the past", color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(title.value, description.value, deadlineMillis.value) },
                enabled = title.value.isNotBlank() && !isPast
            ) { Text("Save") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
private fun ConfirmDeleteDialog(
    task: ReleaseTask,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Task") },
        text = { Text("Are you sure you want to delete this task? This action cannot be undone.") },
        confirmButton = { TextButton(onClick = onConfirm) { Text("Delete") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

private fun formatDate(epochMillis: Long): String {
    val date = Instant.ofEpochMilli(epochMillis).atZone(ZoneId.systemDefault()).toLocalDate()
    return date.format(DateTimeFormatter.ofPattern("MMM d, yyyy"))
}
