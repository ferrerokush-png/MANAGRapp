package com.example.releaseflow.core.domain.usecase.analytics

import com.example.releaseflow.core.domain.model.StreamingMetrics
import com.example.releaseflow.core.domain.model.SocialMediaMetrics
import com.example.releaseflow.core.domain.util.Constants

class GetAnalyticsInsightsUseCase {
    /**
     * Generate insights from streaming metrics
     */
    fun generateStreamingInsights(metrics: StreamingMetrics): List<String> {
        val insights = mutableListOf<String>()
        
        if (metrics.isPerformingWell()) {
            insights.add("🎉 Great performance! Your track is resonating with listeners")
        }
        
        if (metrics.completionRate >= Constants.Analytics.GOOD_COMPLETION_RATE) {
            insights.add("✅ High completion rate - listeners are enjoying the full track")
        } else {
            insights.add("💡 Consider shortening the intro or adding a hook earlier")
        }
        
        if (metrics.skipRate > Constants.Analytics.HIGH_SKIP_RATE) {
            insights.add("⚠️ High skip rate - review the first 30 seconds of your track")
        }
        
        val streamsPerListener = metrics.streamsPerListener()
        if (streamsPerListener >= Constants.Analytics.GOOD_STREAMS_PER_LISTENER) {
            insights.add("🔁 Excellent replay value - listeners are coming back!")
        }
        
        return insights
    }
    
    /**
     * Generate insights from social media metrics
     */
    fun generateSocialInsights(metrics: SocialMediaMetrics): List<String> {
        val insights = mutableListOf<String>()
        
        val engagementRate = metrics.engagementRate()
        when {
            engagementRate >= Constants.Analytics.EXCELLENT_ENGAGEMENT_RATE -> {
                insights.add("🔥 Excellent engagement! Your content is highly engaging")
            }
            engagementRate >= Constants.Analytics.GOOD_ENGAGEMENT_RATE -> {
                insights.add("👍 Good engagement rate - keep up the great content")
            }
            else -> {
                insights.add("💡 Try posting more frequently or using trending sounds")
            }
        }
        
        val viralPotential = metrics.viralPotential()
        if (viralPotential >= Constants.Analytics.VIRAL_POTENTIAL_THRESHOLD) {
            insights.add("🚀 High viral potential - consider boosting this content")
        }
        
        return insights
    }
}
