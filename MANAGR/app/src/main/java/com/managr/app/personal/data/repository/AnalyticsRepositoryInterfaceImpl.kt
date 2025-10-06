package com.managr.app.personal.data.repository

import com.managr.app.personal.data.local.entity.Analytics
import com.managr.app.personal.domain.repository.AnalyticsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsRepositoryInterfaceImpl @Inject constructor(
    private val analyticsRepositoryImpl: AnalyticsRepositoryImpl
) : AnalyticsRepository {
    
    override fun getAnalyticsForProject(projectId: Long): Flow<List<Analytics>> = 
        analyticsRepositoryImpl.getAnalyticsForProject(projectId)
    
    override fun getAnalyticsForProjectByPlatform(projectId: Long, platform: String): Flow<List<Analytics>> = 
        analyticsRepositoryImpl.getAnalyticsForProjectByPlatform(projectId, platform)
    
    override fun getAnalyticsById(id: Long): Flow<Analytics?> = 
        analyticsRepositoryImpl.getAnalyticsById(id)
    
    override suspend fun insertAnalytics(analytics: Analytics): Long = 
        analyticsRepositoryImpl.insertAnalytics(analytics)
    
    override suspend fun insertAnalyticsList(analyticsList: List<Analytics>): List<Long> = 
        analyticsRepositoryImpl.insertAnalyticsList(analyticsList)
    
    override suspend fun updateAnalytics(analytics: Analytics): Int = 
        analyticsRepositoryImpl.updateAnalytics(analytics)
    
    override suspend fun deleteAnalytics(analytics: Analytics): Int = 
        analyticsRepositoryImpl.deleteAnalytics(analytics)
    
    override suspend fun deleteAnalyticsById(id: Long): Int = 
        analyticsRepositoryImpl.deleteAnalyticsById(id)
}
