package com.example.releaseflow.personal.domain.repository

import com.example.releaseflow.personal.data.local.entity.Analytics
import kotlinx.coroutines.flow.Flow

interface AnalyticsRepository {
    fun getAnalyticsForProject(projectId: Long): Flow<List<Analytics>>
    fun getAnalyticsForProjectByPlatform(projectId: Long, platform: String): Flow<List<Analytics>>
    fun getAnalyticsById(id: Long): Flow<Analytics?>
    suspend fun insertAnalytics(analytics: Analytics): Long
    suspend fun insertAnalyticsList(analyticsList: List<Analytics>): List<Long>
    suspend fun updateAnalytics(analytics: Analytics): Int
    suspend fun deleteAnalytics(analytics: Analytics): Int
    suspend fun deleteAnalyticsById(id: Long): Int
}
