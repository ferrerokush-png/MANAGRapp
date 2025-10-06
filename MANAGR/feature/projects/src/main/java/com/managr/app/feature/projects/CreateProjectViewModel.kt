package com.managr.app.feature.projects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.managr.app.core.domain.model.*
import com.managr.app.core.domain.usecase.project.CreateProjectUseCase
import com.managr.app.core.domain.usecase.project.GenerateProjectTemplateUseCase
import com.managr.app.core.domain.usecase.task.CreateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

/**
 * ViewModel for Create Project flow
 */
@HiltViewModel
class CreateProjectViewModel @Inject constructor(
    private val createProjectUseCase: CreateProjectUseCase,
    private val generateTemplateUseCase: GenerateProjectTemplateUseCase,
    private val createTaskUseCase: CreateTaskUseCase
) : ViewModel() {

    // Form state
    private val _formState = MutableStateFlow(CreateProjectFormState())
    val formState: StateFlow<CreateProjectFormState> = _formState.asStateFlow()

    // Current step (0-4)
    private val _currentStep = MutableStateFlow(0)
    val currentStep: StateFlow<Int> = _currentStep.asStateFlow()

    // UI state
    private val _uiState = MutableStateFlow<CreateProjectUiState>(CreateProjectUiState.Editing)
    val uiState: StateFlow<CreateProjectUiState> = _uiState.asStateFlow()

    // Generated tasks preview
    private val _generatedTasks = MutableStateFlow<List<Task>>(emptyList())
    val generatedTasks: StateFlow<List<Task>> = _generatedTasks.asStateFlow()

    /**
     * Update form field
     */
    fun updateTitle(title: String) {
        _formState.update { it.copy(title = title) }
    }

    fun updateArtistName(artistName: String) {
        _formState.update { it.copy(artistName = artistName) }
    }

    fun updateReleaseType(type: ReleaseType) {
        _formState.update { it.copy(releaseType = type) }
        generateTasksPreview()
    }

    fun updateReleaseDate(date: LocalDate) {
        _formState.update { it.copy(releaseDate = date) }
        generateTasksPreview()
    }

    fun updateArtworkUri(uri: String?) {
        _formState.update { it.copy(artworkUri = uri) }
    }

    fun updateGenre(genre: String) {
        _formState.update { it.copy(genre = genre) }
    }

    fun updateTrackCount(count: Int) {
        _formState.update { it.copy(trackCount = count) }
    }

    fun updateDistributor(distributor: DistributorType) {
        _formState.update { it.copy(distributorType = distributor) }
        generateTasksPreview()
    }

    /**
     * Generate tasks preview
     */
    private fun generateTasksPreview() {
        val state = _formState.value
        if (state.releaseType != null && state.releaseDate != null) {
            val tasks = generateTemplateUseCase(
                title = state.title.ifBlank { "Untitled" },
                artistName = state.artistName.ifBlank { "Artist" },
                type = state.releaseType,
                releaseDate = state.releaseDate,
                genre = state.genre.takeIf { it.isNotBlank() },
                distributorType = state.distributorType
            )
            _generatedTasks.value = tasks
        }
    }

    /**
     * Navigate to next step
     */
    fun nextStep() {
        if (_currentStep.value < 4) {
            _currentStep.value += 1
        }
    }

    /**
     * Navigate to previous step
     */
    fun previousStep() {
        if (_currentStep.value > 0) {
            _currentStep.value -= 1
        }
    }

    /**
     * Go to specific step
     */
    fun goToStep(step: Int) {
        if (step in 0..4) {
            _currentStep.value = step
        }
    }

    /**
     * Validate current step
     */
    fun canProceedToNextStep(): Boolean {
        val state = _formState.value
        return when (_currentStep.value) {
            0 -> state.title.isNotBlank() && state.releaseType != null
            1 -> state.releaseDate != null
            2 -> true // Artwork is optional
            3 -> true // Genre is optional
            4 -> true // Review step
            else -> false
        }
    }

    /**
     * Create project with tasks
     */
    fun createProject() {
        viewModelScope.launch {
            try {
                _uiState.value = CreateProjectUiState.Creating

                val state = _formState.value
                
                // Validate
                if (state.title.isBlank() || state.releaseType == null || state.releaseDate == null) {
                    _uiState.value = CreateProjectUiState.Error("Please fill in all required fields")
                    return@launch
                }

                // Create project
                val project = Project(
                    title = state.title,
                    artistName = state.artistName.ifBlank { "Unknown Artist" },
                    type = state.releaseType,
                    releaseDate = state.releaseDate,
                    artworkUri = state.artworkUri,
                    genre = state.genre.takeIf { it.isNotBlank() },
                    trackCount = state.trackCount,
                    distributorType = state.distributorType,
                    uploadDeadline = state.releaseDate.minusDays(state.distributorType.minUploadDays.toLong()),
                    status = ProjectStatus.PLANNING
                )

                createProjectUseCase(project).onSuccess { projectId ->
                    // Create tasks
                    val tasks = _generatedTasks.value.map { it.copy(projectId = projectId) }
                    createTaskUseCase.createTasks(tasks).onSuccess {
                        _uiState.value = CreateProjectUiState.Success(projectId)
                    }.onFailure { error ->
                        _uiState.value = CreateProjectUiState.Error(error.message ?: "Failed to create tasks")
                    }
                }.onFailure { error ->
                    _uiState.value = CreateProjectUiState.Error(error.message ?: "Failed to create project")
                }

            } catch (e: Exception) {
                _uiState.value = CreateProjectUiState.Error(e.message ?: "An error occurred")
            }
        }
    }

    /**
     * Reset form
     */
    fun resetForm() {
        _formState.value = CreateProjectFormState()
        _currentStep.value = 0
        _generatedTasks.value = emptyList()
        _uiState.value = CreateProjectUiState.Editing
    }
}

/**
 * Form state for create project
 */
data class CreateProjectFormState(
    val title: String = "",
    val artistName: String = "",
    val releaseType: ReleaseType? = null,
    val releaseDate: LocalDate? = null,
    val artworkUri: String? = null,
    val genre: String = "",
    val trackCount: Int = 1,
    val distributorType: DistributorType = DistributorType.DISTROKID
)

/**
 * UI state for create project
 */
sealed class CreateProjectUiState {
    object Editing : CreateProjectUiState()
    object Creating : CreateProjectUiState()
    data class Success(val projectId: Long) : CreateProjectUiState()
    data class Error(val message: String) : CreateProjectUiState()
}
