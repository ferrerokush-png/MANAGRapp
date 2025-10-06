package com.managr.app.core.data.repository

import com.managr.app.core.data.local.dao.AnalyticsDao
import com.managr.app.core.data.local.entity.toEntity
import com.managr.app.core.data.local.entity.toDomain
import com.managr.app.core.domain.model.*
import com.managr.app.core.domain.repository.AnalyticsRepository
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import javax.inject.Inject

class AnalyticsRepositoryImpl @Inject constructor(
    private val analyticsDao: AnalyticsDao
) : AnalyticsRepository {

    override fun getStreamingMetricsByProject(projectId: Long): Flow<List<StreamingMetrics>> =
        analyticsDao.getStreamingMetricsByProject(projectId).map { it.map { e -> e.toDomain() } }

    override fun getStreamingMetricsByPlatform(platform: StreamingPlatform, projectId: Long?): Flow<List<StreamingMetrics>> =
        if (projectId != null) analyticsDao.getStreamingMetricsByPlatformAndProject(platform.name, projectId).map { it.map { e -> e.toDomain() } }
        else analyticsDao.getStreamingMetricsByPlatform(platform.name).map { it.map { e -> e.toDomain() } }

    override fun getStreamingMetricsByDateRange(startDate: LocalDate, endDate: LocalDate, projectId: Long?): Flow<List<StreamingMetrics>> =
        if (projectId != null) analyticsDao.getStreamingMetricsByProjectAndDateRange(projectId, startDate, endDate).map { it.map { e -> e.toDomain() } }
        else analyticsDao.getStreamingMetricsByDateRange(startDate, endDate).map { it.map { e -> e.toDomain() } }

    override fun getLatestStreamingMetrics(projectId: Long?): Flow<List<StreamingMetrics>> =
        if (projectId != null) analyticsDao.getLatestStreamingMetricsByProject(projectId).map { it.map { e -> e.toDomain() } }
        else analyticsDao.getLatestStreamingMetrics().map { it.map { e -> e.toDomain() } }

    override fun getStreamingAnalytics(projectId: Long?, startDate: LocalDate, endDate: LocalDate): Flow<StreamingAnalytics> =
        flow { emit(StreamingAnalytics()) }

    override suspend fun insertStreamingMetrics(metrics: StreamingMetrics): Long =
        analyticsDao.insertStreamingMetrics(metrics.toEntity())

    override suspend fun insertStreamingMetrics(metricsList: List<StreamingMetrics>): List<Long> =
        analyticsDao.insertStreamingMetrics(metricsList.map { it.toEntity() })

    override suspend fun updateStreamingMetrics(metrics: StreamingMetrics) =
        analyticsDao.updateStreamingMetrics(metrics.toEntity())

    override suspend fun deleteStreamingMetrics(metrics: StreamingMetrics) =
        analyticsDao.deleteStreamingMetrics(metrics.toEntity())

    override suspend fun getTotalStreams(projectId: Long): Long =
        analyticsDao.getTotalStreams(projectId) ?: 0

    override suspend fun getTotalStreamsForDateRange(startDate: LocalDate, endDate: LocalDate): Long =
        analyticsDao.getTotalStreamsForDateRange(startDate, endDate) ?: 0

    override fun getAllAnalytics(): Flow<List<StreamingMetrics>> =
        analyticsDao.getAllStreamingMetrics().map { entities -> entities.map { entity -> entity.toDomain() } }

    override suspend fun deleteAllAnalytics() =
        analyticsDao.deleteAllStreamingMetrics()
}
