package com.managr.app.feature.analytics

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    // NOTE: Inject analytics repository
) : ViewModel() {

    private val _metrics = MutableStateFlow(AnalyticsMetrics())
    val metrics: StateFlow<AnalyticsMetrics> = _metrics.asStateFlow()

    init {
        loadMetrics(TimePeriod.DAYS_30)
    }

    fun loadMetrics(period: TimePeriod) {
        viewModelScope.launch {
            // Mock data for now
            _metrics.value = AnalyticsMetrics(
                totalStreams = 125_430,
                streamsChange = "+12.5%",
                totalFollowers = 5_234,
                followersChange = "+8.2%",
                engagementRate = 4.7,
                engagementChange = "+1.3%",
                revenue = 342.50,
                revenueChange = "+15.8%",
                platformData = listOf(
                    PlatformData("Spotify", 75_000, 60, "+10%", Color(0xFF1DB954)),
                    PlatformData("Apple Music", 30_000, 24, "+5%", Color(0xFFFA243C)),
                    PlatformData("YouTube", 15_000, 12, "+8%", Color(0xFFFF0000)),
                    PlatformData("Others", 5_430, 4, "+2%", Color(0xFF999999))
                )
            )
        }
    }
}
