package com.managr.app.feature.projects

import androidx.compose.animation.*
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.managr.app.core.design.component.LoadingIndicator
import com.managr.app.core.design.component.RFTextField
import com.managr.app.core.design.component.RFCard
import com.managr.app.core.design.component.PrimaryButton
import com.managr.app.core.design.component.SecondaryButton
import com.managr.app.core.design.component.GlassCard
import com.managr.app.core.design.component.GlassStyle
import com.managr.app.core.design.component.RFTextButton
import com.managr.app.core.domain.model.DistributorType
import com.managr.app.core.domain.model.ReleaseType
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Create Project Screen with multi-step form
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProjectScreen(
    onNavigateBack: () -> Unit,
    onProjectCreated: (Long) -> Unit,
    viewModel: CreateProjectViewModel = hiltViewModel()
) {
    val formState by viewModel.formState.collectAsState()
    val currentStep by viewModel.currentStep.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val generatedTasks by viewModel.generatedTasks.collectAsState()

    // Handle success
    LaunchedEffect(uiState) {
        if (uiState is CreateProjectUiState.Success) {
            val projectId = (uiState as CreateProjectUiState.Success).projectId
            onProjectCreated(projectId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Project") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Progress indicator
            StepProgressIndicator(
                currentStep = currentStep,
                totalSteps = 5,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            // Step content
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                AnimatedContent(
                    targetState = currentStep,
                    transitionSpec = {
                        if (targetState > initialState) {
                            slideInHorizontally { it } + fadeIn() togetherWith
                                    slideOutHorizontally { -it } + fadeOut()
                        } else {
                            slideInHorizontally { -it } + fadeIn() togetherWith
                                    slideOutHorizontally { it } + fadeOut()
                        }
                    },
                    label = "step_transition"
                ) { step ->
                    when (step) {
                        0 -> BasicInfoStep(formState, viewModel)
                        1 -> ReleaseDateStep(formState, viewModel)
                        2 -> ArtworkStep(formState, viewModel)
                        3 -> GenreStep(formState, viewModel)
                        4 -> ReviewStep(formState, generatedTasks, viewModel)
                    }
                }

                // Loading overlay
                if (uiState is CreateProjectUiState.Creating) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingIndicator(message = "Creating project...")
                    }
                }
            }

            // Navigation buttons
            NavigationButtons(
                currentStep = currentStep,
                canProceed = viewModel.canProceedToNextStep(),
                onPrevious = viewModel::previousStep,
                onNext = viewModel::nextStep,
                onCreate = viewModel::createProject,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }

    // Error snackbar
    if (uiState is CreateProjectUiState.Error) {
        val message = (uiState as CreateProjectUiState.Error).message
        LaunchedEffect(message) {
            // Show snackbar or handle error
        }
    }
}

@Composable
private fun StepProgressIndicator(
    currentStep: Int,
    totalSteps: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(totalSteps) { index ->
            val isComplete = index < currentStep
            val isCurrent = index == currentStep
            
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        when {
                            isComplete -> MaterialTheme.colorScheme.primary
                            isCurrent -> MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }
                    )
            )
        }
    }
}

@Composable
private fun BasicInfoStep(
    formState: CreateProjectFormState,
    viewModel: CreateProjectViewModel
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                "Basic Information",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            RFTextField(
                value = formState.title,
                onValueChange = viewModel::updateTitle,
                label = "Project Title",
                placeholder = "My Awesome Single",
                leadingIcon = Icons.Default.MusicNote
            )
        }

        item {
            RFTextField(
                value = formState.artistName,
                onValueChange = viewModel::updateArtistName,
                label = "Artist Name",
                placeholder = "Your Name",
                leadingIcon = Icons.Default.Person
            )
        }

        item {
            Text(
                "Release Type",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ReleaseType.entries.forEach { type ->
                    ReleaseTypeCard(
                        type = type,
                        isSelected = formState.releaseType == type,
                        onClick = { viewModel.updateReleaseType(type) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        item {
            RFTextField(
                value = formState.trackCount.toString(),
                onValueChange = { viewModel.updateTrackCount(it.toIntOrNull() ?: 1) },
                label = "Track Count",
                placeholder = "1"
            )
        }
    }
}

@Composable
private fun ReleaseTypeCard(
    type: ReleaseType,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icon = when (type) {
        ReleaseType.SINGLE -> Icons.Default.MusicNote
        ReleaseType.EP -> Icons.Default.LibraryMusic
        ReleaseType.ALBUM -> Icons.Default.Album
    }

    Surface(
        onClick = onClick,
        modifier = modifier.aspectRatio(1f),
        shape = MaterialTheme.shapes.large,
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
        tonalElevation = if (isSelected) 8.dp else 2.dp,
        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = type.displayName(),
                    modifier = Modifier.size(48.dp),
                    tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = type.displayName(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${type.typicalTaskCount()} tasks",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ReleaseDateStep(
    formState: CreateProjectFormState,
    viewModel: CreateProjectViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Release Date",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Text(
            "When do you plan to release this ${formState.releaseType?.displayName() ?: "project"}?",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Simple date picker (you can enhance this with a calendar UI)
        GlassCard(contentPadding = 16.dp) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Selected Date:", style = MaterialTheme.typography.labelMedium)
                Text(
                    text = formState.releaseDate?.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"))
                        ?: "Not selected",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Quick date buttons
                Text("Quick Select:", style = MaterialTheme.typography.labelMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SecondaryButton(
                        text = "30 Days",
                        onClick = { viewModel.updateReleaseDate(LocalDate.now().plusDays(30)) }
                    )
                    SecondaryButton(
                        text = "60 Days",
                        onClick = { viewModel.updateReleaseDate(LocalDate.now().plusDays(60)) }
                    )
                    SecondaryButton(
                        text = "90 Days",
                        onClick = { viewModel.updateReleaseDate(LocalDate.now().plusDays(90)) }
                    )
                }
            }
        }

        // Upload deadline info
        if (formState.releaseDate != null) {
            val uploadDeadline = formState.releaseDate.minusDays(formState.distributorType.minUploadDays.toLong())
            InfoCard(
                title = "Upload Deadline",
                description = "You'll need to upload by ${uploadDeadline.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))} to ${formState.distributorType.displayName}",
                icon = Icons.Default.Upload
            )
        }
    }
}

@Composable
private fun ArtworkStep(
    formState: CreateProjectFormState,
    viewModel: CreateProjectViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Artwork",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Text(
            "Add cover artwork for your ${formState.releaseType?.displayName() ?: "project"}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (formState.artworkUri != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(MaterialTheme.shapes.large)
            ) {
                coil.compose.AsyncImage(
                    model = formState.artworkUri,
                    contentDescription = "Artwork preview",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            }
        }

        GlassCard(contentPadding = 16.dp) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Recommended: 1500x1500px (square)", style = MaterialTheme.typography.bodyMedium)
                
                SecondaryButton(
                    text = if (formState.artworkUri != null) "Change Artwork" else "Upload Artwork",
                    onClick = { viewModel.updateArtworkUri("placeholder_artwork_uri") },
                    icon = Icons.Default.Image
                )
                
                RFTextButton(
                    text = "Skip for now",
                    onClick = { viewModel.nextStep() }
                )
            }
        }
    }
}

@Composable
private fun GenreStep(
    formState: CreateProjectFormState,
    viewModel: CreateProjectViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Genre & Details",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        RFTextField(
            value = formState.genre,
            onValueChange = viewModel::updateGenre,
            label = "Genre",
            placeholder = "Pop, Hip-Hop, Rock, etc.",
            leadingIcon = Icons.Default.MusicNote
        )
    }
}

@Composable
private fun ReviewStep(
    formState: CreateProjectFormState,
    generatedTasks: List<com.managr.app.core.domain.model.Task>,
    viewModel: CreateProjectViewModel
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                "Review & Confirm",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            GlassCard(contentPadding = 16.dp) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ReviewItem("Title", formState.title)
                    ReviewItem("Artist", formState.artistName.ifBlank { "Unknown Artist" })
                    ReviewItem("Type", formState.releaseType?.displayName() ?: "")
                    ReviewItem("Release Date", formState.releaseDate?.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) ?: "")
                    ReviewItem("Genre", formState.genre.ifBlank { "Not specified" })
                    ReviewItem("Tracks", formState.trackCount.toString())
                }
            }
        }

        item {
            Text(
                "Generated Tasks (${generatedTasks.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        items(generatedTasks.take(5)) { task ->
            TaskPreviewCard(task)
        }

        if (generatedTasks.size > 5) {
            item {
                Text(
                    "...and ${generatedTasks.size - 5} more tasks",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ReviewItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun TaskPreviewCard(task: com.managr.app.core.domain.model.Task) {
    RFCard(
        contentPadding = 12.dp,
        elevation = 2.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircleOutline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = task.dueDate.format(DateTimeFormatter.ofPattern("MMM dd")),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun InfoCard(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    GlassCard(
        style = GlassStyle.LIGHT,
        contentPadding = 16.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun NavigationButtons(
    currentStep: Int,
    canProceed: Boolean,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onCreate: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (currentStep > 0) {
            SecondaryButton(
                text = "Previous",
                onClick = onPrevious,
                modifier = Modifier.weight(1f)
            )
        }

        if (currentStep < 4) {
            PrimaryButton(
                text = "Next",
                onClick = onNext,
                enabled = canProceed,
                modifier = Modifier.weight(1f),
                icon = Icons.Default.ArrowForward
            )
        } else {
            PrimaryButton(
                text = "Create Project",
                onClick = onCreate,
                enabled = canProceed,
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Check
            )
        }
    }
}
