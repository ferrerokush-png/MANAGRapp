package com.managr.app.personal.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.managr.app.personal.data.local.entity.ReleaseProject
import com.managr.app.personal.ui.navigation.AppDestinations
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavHostController, viewModel: DashboardViewModel = hiltViewModel()) {
    val projects by viewModel.projects.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text(text = "My Releases", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold) }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Project")
            }
        }
    ) { innerPadding ->
        ProjectList(
            projects = projects,
            onProjectClick = { id -> navController.navigate(AppDestinations.projectDetailRoute(id)) },
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = innerPadding
        )

        if (showAddDialog) {
            AddProjectDialog(
                onDismiss = { showAddDialog = false },
                onSave = { title, artistName, type, dateMillis ->
                    viewModel.addProject(title, artistName, type, java.util.Date(dateMillis))
                    showAddDialog = false
                }
            )
        }
    }
}

@Composable
private fun ProjectList(
    projects: List<ReleaseProject>,
    onProjectClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(projects, key = { it.id }) { project ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onProjectClick(project.id) }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = project.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${project.releaseType.name} • ${formatDate(project.releaseDate.time)}",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
private fun AddProjectDialog(
    onDismiss: () -> Unit,
    onSave: (title: String, artistName: String, type: com.managr.app.personal.data.local.entity.ReleaseType, releaseDateMillis: Long) -> Unit
) {
    val context = LocalContext.current

    var title by remember { mutableStateOf("") }
    var artistName by remember { mutableStateOf("") }
    val typeOptions = listOf(
        com.managr.app.personal.data.local.entity.ReleaseType.SINGLE,
        com.managr.app.personal.data.local.entity.ReleaseType.EP,
        com.managr.app.personal.data.local.entity.ReleaseType.ALBUM
    )
    var typeExpanded by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf(typeOptions.first()) }

    val today = remember { LocalDate.now() }
    var releaseDateMillis by remember { mutableStateOf(today.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()) }

    val calendar = remember(releaseDateMillis) {
        Calendar.getInstance().apply { timeInMillis = releaseDateMillis }
    }

    val datePicker = remember(calendar) {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val picked = LocalDate.of(year, month + 1, dayOfMonth)
                releaseDateMillis = picked.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Project") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Project Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                
                OutlinedTextField(
                    value = artistName,
                    onValueChange = { artistName = it },
                    label = { Text("Artist Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))

                // Type dropdown
                Column(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = selectedType.name,
                        onValueChange = {},
                        label = { Text("Project type") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                    )
                    Box {
                        OutlinedButton(onClick = { typeExpanded = true }) { Text("Change Type") }
                        DropdownMenu(expanded = typeExpanded, onDismissRequest = { typeExpanded = false }) {
                            typeOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option.name) },
                                    onClick = {
                                        selectedType = option
                                        typeExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))

                // Release date picker
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Release date: ${formatDate(releaseDateMillis)}")
                    Spacer(Modifier.weight(1f))
                    Button(onClick = { datePicker.show() }) { Text("Pick Date") }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(title, artistName, selectedType, releaseDateMillis) },
                enabled = title.isNotBlank() && artistName.isNotBlank()
            ) { Text("Save") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

private fun formatDate(epochMillis: Long): String {
    val date = Instant.ofEpochMilli(epochMillis).atZone(ZoneId.systemDefault()).toLocalDate()
    return date.format(DateTimeFormatter.ofPattern("MMM d, yyyy"))
}
