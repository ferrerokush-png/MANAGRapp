package com.managr.app.personal.ui.ai

import androidx.compose.animation.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.managr.app.core.design.component.*
import com.managr.app.personal.data.local.entity.AIQueryType
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIInsightsScreen(
    onNavigateBack: () -> Unit,
    viewModel: AIInsightsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val queryHistory by viewModel.queryHistory.collectAsState()
    val remainingQueries by viewModel.remainingQueries.collectAsState()
    var selectedQueryType by remember { mutableStateOf<AIQueryType?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Assistant") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Query limit indicator
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                color = if (remainingQueries > 3) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.errorContainer
                }
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = if (remainingQueries > 3) {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                MaterialTheme.colorScheme.onErrorContainer
                            }
                        )
                        Text(
                            "$remainingQueries queries remaining this month",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Query type selector
            Text(
                "Select AI Assistance Type",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AIQueryType.entries.forEach { type ->
                    FilterChip(
                        selected = selectedQueryType == type,
                        onClick = {
                            selectedQueryType = if (selectedQueryType == type) null else type
                            viewModel.selectQueryType(type)
                        },
                        label = { Text(type.displayName()) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Content
            when (val state = uiState) {
                is AIInsightsUiState.Idle -> {
                    if (selectedQueryType == null) {
                        AIIdleState()
                    } else {
                        AIPromptForm(
                            queryType = selectedQueryType!!,
                            onSubmit = viewModel::submitQuery,
                            remainingQueries = remainingQueries
                        )
                    }
                }
                is AIInsightsUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingIndicator(message = "Generating insights...")
                    }
                }
                is AIInsightsUiState.Success -> {
                    AIResponseCard(
                        response = state.response,
                        onNewQuery = viewModel::reset,
                        onFeedback = { helpful ->
                            viewModel.submitFeedback(helpful)
                        }
                    )
                }
                is AIInsightsUiState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        GenericErrorState(
                            message = state.message,
                            onRetryClick = viewModel::reset
                        )
                    }
                }
            }

            // Query history
            if (queryHistory.isNotEmpty() && uiState is AIInsightsUiState.Idle) {
                Text(
                    "Recent Queries",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(queryHistory.take(5)) { query ->
                        QueryHistoryCard(query = query)
                    }
                }
            }
        }
    }
}

@Composable
private fun AIIdleState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                Icons.Default.SmartToy,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
            )
            Text(
                "Select an AI assistance type to get started",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun AIPromptForm(
    queryType: AIQueryType,
    onSubmit: (AIQueryType, Map<String, String>) -> Unit,
    remainingQueries: Int
) {
    var projectTitle by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var additionalInfo by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            GlassCard(contentPadding = 16.dp) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Lightbulb, null, tint = MaterialTheme.colorScheme.primary)
                        Text(
                            queryType.displayName(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        queryType.description(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item {
            RFTextField(
                value = projectTitle,
                onValueChange = { projectTitle = it },
                label = "Project/Track Title",
                placeholder = "Enter project name",
                leadingIcon = Icons.Default.MusicNote
            )
        }

        item {
            RFTextField(
                value = genre,
                onValueChange = { genre = it },
                label = "Genre",
                placeholder = "e.g., Hip-Hop, Pop, Rock",
                leadingIcon = Icons.Default.Category
            )
        }

        item {
            RFTextField(
                value = additionalInfo,
                onValueChange = { additionalInfo = it },
                label = "Additional Context",
                placeholder = "Any other relevant details...",
                leadingIcon = Icons.Default.Info,
                singleLine = false,
                maxLines = 4
            )
        }

        item {
            PrimaryButton(
                text = "Generate Insights",
                onClick = {
                    onSubmit(
                        queryType,
                        mapOf(
                            "projectTitle" to projectTitle,
                            "genre" to genre,
                            "additionalInfo" to additionalInfo
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = projectTitle.isNotBlank() && remainingQueries > 0,
                icon = Icons.Default.AutoAwesome
            )
        }
    }
}

@Composable
private fun AIResponseCard(
    response: String,
    onNewQuery: () -> Unit,
    onFeedback: (Boolean) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            GlassCard(contentPadding = 16.dp) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "AI Insights",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        response,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.fillMaxWidth()
                    )

                    HorizontalDivider()

                    Text(
                        "Was this helpful?",
                        style = MaterialTheme.typography.labelMedium
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        SecondaryButton(
                            text = "Yes",
                            onClick = { onFeedback(true) },
                            icon = Icons.Default.ThumbUp
                        )
                        SecondaryButton(
                            text = "No",
                            onClick = { onFeedback(false) },
                            icon = Icons.Default.ThumbDown
                        )
                    }
                }
            }
        }

        item {
            PrimaryButton(
                text = "New Query",
                onClick = onNewQuery,
                modifier = Modifier.fillMaxWidth(),
                icon = Icons.Default.Add
            )
        }
    }
}

@Composable
private fun QueryHistoryCard(query: com.managr.app.personal.data.local.entity.AIQuery) {
    val dateFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())

    RFCard(contentPadding = 12.dp) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    query.queryType.displayName(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    dateFormat.format(query.createdAt),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                query.response.take(100) + if (query.response.length > 100) "..." else "",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )
        }
    }
}

private fun AIQueryType.displayName(): String = when (this) {
    AIQueryType.ANALYTICS_INSIGHTS -> "Analytics Insights"
    AIQueryType.TASK_SUGGESTIONS -> "Task Suggestions"
    AIQueryType.PLAYLIST_PITCH -> "Playlist Pitch"
    AIQueryType.RELEASE_STRATEGY -> "Release Strategy"
}

private fun AIQueryType.description(): String = when (this) {
    AIQueryType.ANALYTICS_INSIGHTS -> "Get AI-powered insights from your streaming and social media data"
    AIQueryType.TASK_SUGGESTIONS -> "Discover additional marketing tasks tailored to your release timeline"
    AIQueryType.PLAYLIST_PITCH -> "Generate professional playlist pitches for curators"
    AIQueryType.RELEASE_STRATEGY -> "Create a comprehensive release strategy for your project"
}
