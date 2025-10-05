package com.example.releaseflow.personal.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.releaseflow.personal.data.local.entity.Analytics
import com.example.releaseflow.personal.domain.repository.AnalyticsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

sealed class AnalyticsUiState {
    object Loading : AnalyticsUiState()
    data class Success(val analytics: List<Analytics>) : AnalyticsUiState()
    data class Error(val message: String) : AnalyticsUiState()
}

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val analyticsRepository: AnalyticsRepository
) : ViewModel() {

    private val _selectedProjectId = MutableStateFlow<Long?>(null)
    val selectedProjectId: StateFlow<Long?> = _selectedProjectId.asStateFlow()

    private val _uiState = MutableStateFlow<AnalyticsUiState>(AnalyticsUiState.Loading)
    val uiState: StateFlow<AnalyticsUiState> = _uiState.asStateFlow()

    fun selectProject(projectId: Long) {
        _selectedProjectId.value = projectId
        loadAnalytics(projectId)
    }

    private fun loadAnalytics(projectId: Long) {
        viewModelScope.launch {
            try {
                analyticsRepository.getAnalyticsForProject(projectId)
                    .catch { e ->
                        _uiState.value = AnalyticsUiState.Error(e.message ?: "Unknown error")
                    }
                    .collect { analytics ->
                        _uiState.value = AnalyticsUiState.Success(analytics)
                    }
            } catch (e: Exception) {
                _uiState.value = AnalyticsUiState.Error(e.message ?: "Failed to load analytics")
            }
        }
    }

    fun insertAnalytics(
        projectId: Long,
        platform: String,
        metricType: String,
        value: Double,
        date: Date = Date(),
        additionalData: String? = null
    ) {
        viewModelScope.launch {
            try {
                analyticsRepository.insertAnalytics(
                    Analytics(
                        projectId = projectId,
                        platform = platform,
                        metricType = metricType,
                        value = value,
                        date = date,
                        additionalData = additionalData
                    )
                )
            } catch (e: Exception) {
                _uiState.value = AnalyticsUiState.Error(e.message ?: "Failed to insert analytics")
            }
        }
    }

    fun updateAnalytics(analytics: Analytics) {
        viewModelScope.launch {
            try {
                analyticsRepository.updateAnalytics(analytics)
            } catch (e: Exception) {
                _uiState.value = AnalyticsUiState.Error(e.message ?: "Failed to update analytics")
            }
        }
    }

    fun deleteAnalytics(analytics: Analytics) {
        viewModelScope.launch {
            try {
                analyticsRepository.deleteAnalytics(analytics)
            } catch (e: Exception) {
                _uiState.value = AnalyticsUiState.Error(e.message ?: "Failed to delete analytics")
            }
        }
    }
}
