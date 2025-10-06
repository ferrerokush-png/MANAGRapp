package com.managr.app.personal.ui.screens.tasks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.managr.app.core.design.component.*
import com.managr.app.personal.data.local.entity.TaskCategory
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(
    projectId: Long,
    onDismiss: () -> Unit,
    onTaskAdded: () -> Unit,
    viewModel: AddTaskViewModel = hiltViewModel()
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf<Date?>(null) }
    var priority by remember { mutableStateOf(2) } // Medium
    var category by remember { mutableStateOf(TaskCategory.PRODUCTION) }
    var assignedTo by remember { mutableStateOf("") }
    
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Task") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title
                RFTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = "Task Title",
                    placeholder = "Enter task title",
                    leadingIcon = Icons.Default.CheckCircle
                )

                // Description
                RFTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = "Description (Optional)",
                    placeholder = "Add details...",
                    leadingIcon = Icons.Default.Description,
                    singleLine = false,
                    maxLines = 3
                )

                // Due date
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Due Date", style = MaterialTheme.typography.labelMedium)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        SecondaryButton(
                            text = dueDate?.let { dateFormat.format(it) } ?: "Select Date",
                            onClick = {
                                // Simple date selection - add 7 days for demo
                                val calendar = Calendar.getInstance()
                                calendar.add(Calendar.DAY_OF_YEAR, 7)
                                dueDate = calendar.time
                            },
                            icon = Icons.Default.CalendarToday
                        )
                        if (dueDate != null) {
                            RFTextButton(
                                text = "Clear",
                                onClick = { dueDate = null }
                            )
                        }
                    }
                }

                // Priority
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Priority", style = MaterialTheme.typography.labelMedium)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(
                            1 to "High",
                            2 to "Medium",
                            3 to "Low"
                        ).forEach { (value, label) ->
                            FilterChip(
                                selected = priority == value,
                                onClick = { priority = value },
                                label = { Text(label) }
                            )
                        }
                    }
                }

                // Category
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Category", style = MaterialTheme.typography.labelMedium)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TaskCategory.entries.forEach { cat ->
                            FilterChip(
                                selected = category == cat,
                                onClick = { category = cat },
                                label = { Text(cat.name.take(4)) }
                            )
                        }
                    }
                }

                // Assigned to
                RFTextField(
                    value = assignedTo,
                    onValueChange = { assignedTo = it },
                    label = "Assigned To (Optional)",
                    placeholder = "Person name",
                    leadingIcon = Icons.Default.Person
                )
            }
        },
        confirmButton = {
            PrimaryButton(
                text = "Add Task",
                onClick = {
                    if (title.isNotBlank() && dueDate != null) {
                        viewModel.addTask(
                            projectId = projectId,
                            title = title,
                            description = description.takeIf { it.isNotBlank() },
                            dueDate = dueDate!!,
                            priority = priority,
                            category = category,
                            assignedTo = assignedTo.takeIf { it.isNotBlank() }
                        )
                        onTaskAdded()
                    }
                },
                enabled = title.isNotBlank() && dueDate != null
            )
        },
        dismissButton = {
            RFTextButton(
                text = "Cancel",
                onClick = onDismiss
            )
        }
    )
}
