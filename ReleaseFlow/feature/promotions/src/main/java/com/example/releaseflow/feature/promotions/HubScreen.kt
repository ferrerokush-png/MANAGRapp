package com.example.releaseflow.feature.promotions

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.releaseflow.core.design.component.LoadingIndicator
import com.example.releaseflow.core.design.component.EmptyContactsState
import com.example.releaseflow.core.design.component.GenericErrorState
import com.example.releaseflow.core.design.component.RFCard
import com.example.releaseflow.core.design.component.PrimaryButton
import com.example.releaseflow.core.design.component.RFTextField
import com.example.releaseflow.core.design.component.RFTextButton
import com.example.releaseflow.core.design.component.GlassCard
import com.example.releaseflow.core.design.component.StatusChip
import com.example.releaseflow.core.design.component.StatusChipColor
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HubScreen(
    viewModel: HubViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedType by viewModel.selectedType.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Industry Hub") },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, "Add Contact")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, "Add Contact")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search bar
            RFTextField(
                value = searchQuery,
                onValueChange = viewModel::onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = "Search contacts...",
                leadingIcon = Icons.Default.Search,
                singleLine = true
            )

            // Contact type tabs
            ScrollableTabRow(
                selectedTabIndex = ContactType.entries.indexOf(selectedType),
                modifier = Modifier.fillMaxWidth(),
                edgePadding = 16.dp
            ) {
                ContactType.entries.forEach { type ->
                    Tab(
                        selected = selectedType == type,
                        onClick = { viewModel.selectType(type) },
                        text = { 
                            Text(
                                text = type.name.lowercase().replaceFirstChar { it.uppercase() },
                                fontWeight = if (selectedType == type) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            // Contact list
            when (val state = uiState) {
                is HubUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingIndicator(message = "Loading contacts...")
                    }
                }
                is HubUiState.Success -> {
                    if (state.contacts.isEmpty()) {
                        EmptyContactsList(onAddClick = { showAddDialog = true })
                    } else {
                        ContactList(
                            contacts = state.contacts,
                            onContactClick = { /* TODO */ },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                is HubUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        GenericErrorState(
                            message = state.message,
                            onRetryClick = viewModel::refreshContacts
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddContactDialog(
            onDismiss = { showAddDialog = false },
            onContactAdded = {
                showAddDialog = false
                viewModel.refreshContacts()
            }
        )
    }
}

@Composable
private fun ContactList(
    contacts: List<ContactItem>,
    onContactClick: (ContactItem) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(contacts) { contact ->
            ContactCard(
                contact = contact,
                onClick = { onContactClick(contact) }
            )
        }
    }
}

@Composable
private fun ContactCard(
    contact: ContactItem,
    onClick: () -> Unit
) {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    GlassCard(
        onClick = onClick,
        contentPadding = 16.dp
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Avatar
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = contact.name.first().toString(),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Name and title
                Text(
                    text = contact.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )

                if (contact.platform != null) {
                    Text(
                        text = contact.platform,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Genre tags
                if (contact.genreFocus != null) {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        contact.genreFocus.split(",").take(3).forEach { genre ->
                            StatusChip(
                                text = genre.trim(),
                                color = StatusChipColor.NEUTRAL
                            )
                        }
                    }
                }

                // Last contact and success rate
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (contact.lastContact != null) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = dateFormat.format(contact.lastContact),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    if (contact.successRate != null) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = if (contact.successRate >= 50) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${contact.successRate}%",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (contact.successRate >= 50) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Contact methods
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (contact.email != null) {
                        IconButton(onClick = { /* TODO: Open email */ }) {
                            Icon(Icons.Default.Email, "Email", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                    if (contact.submissionUrl != null) {
                        IconButton(onClick = { /* TODO: Open URL */ }) {
                            Icon(Icons.Default.Link, "Website", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AddContactDialog(
    onDismiss: () -> Unit,
    onContactAdded: () -> Unit,
    viewModel: HubViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(ContactType.CURATOR) }
    var email by remember { mutableStateOf("") }
    var platform by remember { mutableStateOf("") }
    var genreFocus by remember { mutableStateOf("") }
    var submissionUrl by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Contact") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                RFTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Name",
                    placeholder = "Contact name",
                    leadingIcon = Icons.Default.Person
                )

                // Type selector
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Type", style = MaterialTheme.typography.labelMedium)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        ContactType.entries.take(4).forEach { contactType ->
                            FilterChip(
                                selected = type == contactType,
                                onClick = { type = contactType },
                                label = { Text(contactType.name.take(3)) }
                            )
                        }
                    }
                }

                RFTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email",
                    placeholder = "contact@example.com",
                    leadingIcon = Icons.Default.Email
                )

                RFTextField(
                    value = platform,
                    onValueChange = { platform = it },
                    label = "Platform/Company",
                    placeholder = "Spotify, Blog name, etc."
                )

                RFTextField(
                    value = genreFocus,
                    onValueChange = { genreFocus = it },
                    label = "Genre Focus",
                    placeholder = "Hip-Hop, Rock, etc."
                )

                RFTextField(
                    value = submissionUrl,
                    onValueChange = { submissionUrl = it },
                    label = "Submission URL",
                    placeholder = "https://..."
                )
            }
        },
        confirmButton = {
            PrimaryButton(
                text = "Add",
                onClick = {
                    if (name.isNotBlank()) {
                        viewModel.addContact(
                            name = name,
                            type = type,
                            email = email.takeIf { it.isNotBlank() },
                            platform = platform.takeIf { it.isNotBlank() },
                            genreFocus = genreFocus.takeIf { it.isNotBlank() },
                            submissionUrl = submissionUrl.takeIf { it.isNotBlank() }
                        )
                        onContactAdded()
                    }
                },
                enabled = name.isNotBlank()
            )
        },
        dismissButton = {
            RFTextButton(text = "Cancel", onClick = onDismiss)
        }
    )
}

@Composable
private fun EmptyContactsList(onAddClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        EmptyContactsState(
            onAddClick = onAddClick
        )
    }
}