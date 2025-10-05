package com.example.releaseflow.personal.data.repository

import com.example.releaseflow.personal.data.local.dao.AnalyticsDao
import com.example.releaseflow.personal.data.local.entity.Analytics
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsRepositoryImpl @Inject constructor(
    private val analyticsDao: AnalyticsDao
) {
    fun getAnalyticsForProject(projectId: Long): Flow<List<Analytics>> = 
        analyticsDao.getAnalyticsForProject(projectId)

    fun getAnalyticsForProjectByPlatform(projectId: Long, platform: String): Flow<List<Analytics>> = 
        analyticsDao.getAnalyticsForProjectByPlatform(projectId, platform)

    fun getAnalyticsById(id: Long): Flow<Analytics?> = analyticsDao.getAnalyticsById(id)

    suspend fun insertAnalytics(analytics: Analytics): Long = analyticsDao.insertAnalytics(analytics)

    suspend fun insertAnalyticsList(analyticsList: List<Analytics>): List<Long> = 
        analyticsDao.insertAnalyticsList(analyticsList)

    suspend fun updateAnalytics(analytics: Analytics): Int = analyticsDao.updateAnalytics(analytics)

    suspend fun deleteAnalytics(analytics: Analytics): Int = analyticsDao.deleteAnalytics(analytics)

    suspend fun deleteAnalyticsById(id: Long): Int = analyticsDao.deleteAnalyticsById(id)
}
