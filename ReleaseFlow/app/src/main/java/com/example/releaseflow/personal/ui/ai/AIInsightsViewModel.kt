package com.example.releaseflow.personal.ui.ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.releaseflow.personal.ai.AIService
import com.example.releaseflow.personal.data.local.dao.AIQueryDao
import com.example.releaseflow.personal.data.local.entity.AIQuery
import com.example.releaseflow.personal.data.local.entity.AIQueryType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AIInsightsUiState {
    object Idle : AIInsightsUiState()
    object Loading : AIInsightsUiState()
    data class Success(val response: String) : AIInsightsUiState()
    data class Error(val message: String) : AIInsightsUiState()
}

@HiltViewModel
class AIInsightsViewModel @Inject constructor(
    private val aiService: AIService,
    private val aiQueryDao: AIQueryDao
) : ViewModel() {

    private val _uiState = MutableStateFlow<AIInsightsUiState>(AIInsightsUiState.Idle)
    val uiState: StateFlow<AIInsightsUiState> = _uiState.asStateFlow()

    private val _selectedQueryType = MutableStateFlow<AIQueryType?>(null)
    val selectedQueryType: StateFlow<AIQueryType?> = _selectedQueryType.asStateFlow()

    private val _remainingQueries = MutableStateFlow(10)
    val remainingQueries: StateFlow<Int> = _remainingQueries.asStateFlow()

    val queryHistory: StateFlow<List<AIQuery>> = aiQueryDao.getRecentQueries(10)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadRemainingQueries()
    }

    private fun loadRemainingQueries() {
        viewModelScope.launch {
            val (hasRemaining, remaining) = aiService.hasRemainingQueries()
            _remainingQueries.value = remaining
        }
    }

    fun selectQueryType(type: AIQueryType) {
        _selectedQueryType.value = type
        _uiState.value = AIInsightsUiState.Idle
    }

    fun submitQuery(queryType: AIQueryType, params: Map<String, String>) {
        viewModelScope.launch {
            _uiState.value = AIInsightsUiState.Loading

            try {
                val result = when (queryType) {
                    AIQueryType.ANALYTICS_INSIGHTS -> {
                        aiService.generateAnalyticsInsights(
                            projectTitle = params["projectTitle"] ?: "",
                            streamData = params["additionalInfo"] ?: "No stream data provided",
                            platformBreakdown = "Multiple platforms"
                        )
                    }
                    AIQueryType.TASK_SUGGESTIONS -> {
                        aiService.generateTaskSuggestions(
                            releaseType = params["releaseType"] ?: "Single",
                            weeksUntilRelease = 4,
                            genre = params["genre"]
                        )
                    }
                    AIQueryType.PLAYLIST_PITCH -> {
                        aiService.generatePlaylistPitch(
                            trackTitle = params["projectTitle"] ?: "Untitled",
                            artistName = params["artistName"] ?: "Artist",
                            genre = params["genre"] ?: "Music",
                            targetAudience = "Playlist curators",
                            trackDescription = params["additionalInfo"]
                        )
                    }
                    AIQueryType.RELEASE_STRATEGY -> {
                        // This requires a full project object
                        // For now, we'll use a simplified version
                        Result.failure(UnsupportedOperationException("Use project-specific release strategy"))
                    }
                }

                result.fold(
                    onSuccess = { response ->
                        _uiState.value = AIInsightsUiState.Success(response)
                        loadRemainingQueries()
                    },
                    onFailure = { error ->
                        _uiState.value = AIInsightsUiState.Error(
                            error.message ?: "Failed to generate insights"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = AIInsightsUiState.Error(
                    e.message ?: "An unexpected error occurred"
                )
            }
        }
    }

    fun submitFeedback(helpful: Boolean) {
        // TODO: Save feedback to last query
        reset()
    }

    fun reset() {
        _uiState.value = AIInsightsUiState.Idle
        _selectedQueryType.value = null
    }
}
